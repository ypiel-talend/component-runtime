/**
 * Copyright (C) 2006-2021 Talend Inc. - www.talend.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.talend.sdk.component.runtime.beam.spi.record;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.record.Schema;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;
import org.talend.sdk.component.runtime.record.RecordBuilderFactoryImpl;

public class AvroRecordBenchTest {

    private static final int nbeField = 10;

    private static final int nbeRecord = 10_000;

    @Test
    void perfCurrentRecord() {
        final RecordBuilderFactory factory = new RecordBuilderFactoryImpl("test");
        final Schema schema = this.buildSchema(factory);
        final List<Record> records = new ArrayList<>(nbeRecord);
        for (int i = 0; i < nbeRecord; i++) {
            final Record record = this.buildRecord(factory, schema, "Record_" + i + "_Field_e");
            records.add(record);
        }
        final List<AvroRecord> avroRecords = new ArrayList<>(nbeRecord);
        final long start = System.currentTimeMillis();
        for (Record record : records) {
            final AvroRecord avroRecord = new AvroRecord(record);
            avroRecords.add(avroRecord);
        }
        final long duration = System.currentTimeMillis() - start;
        Assertions.assertEquals(nbeRecord, avroRecords.size());
        System.out.println("duration current : " + duration);
    }

    @Test
    void perfCurrentRecordOptim() {
        final RecordBuilderFactory factory = new RecordBuilderFactoryImpl("test");
        final Schema schema = this.buildSchema(factory);
        final List<Record> records = new ArrayList<>(nbeRecord);
        for (int i = 0; i < nbeRecord; i++) {
            final Record record = this.buildRecord(factory, schema, "Record_" + i + "_Field_e");
            records.add(record);
        }
        final List<AvroRecordOptim> avroRecords = new ArrayList<>(nbeRecord);
        final long start = System.currentTimeMillis();
        for (Record record : records) {
            final AvroRecordOptim avroRecord = new AvroRecordOptim(record);
            avroRecords.add(avroRecord);
        }
        final long duration = System.currentTimeMillis() - start;
        Assertions.assertEquals(nbeRecord, avroRecords.size());
        System.out.println("duration optim : " + duration);
    }

    private Record buildRecord(final RecordBuilderFactory factory, final Schema schema, final String valuePrefix) {
        final Record.Builder builder = factory.newRecordBuilder(schema);
        for (int i = 0; i < nbeField; i++) {
            builder.withString("e" + i, valuePrefix + i);
        }
        return builder.build();
    }

    private Schema buildSchema(final RecordBuilderFactory factory) {
        final List<Schema.Entry> fields = new ArrayList<>(nbeField);
        for (int i = 0; i < nbeField; i++) {
            final Schema.Entry entry = factory
                    .newEntryBuilder()
                    .withType(Schema.Type.STRING) //
                    .withNullable(true) //
                    .withName("e" + i) //
                    .build();
            fields.add(entry);
        }

        final Schema.Builder builder = factory.newSchemaBuilder(Schema.Type.RECORD);
        fields.forEach(builder::withEntry);
        return builder.build();
    }
}
