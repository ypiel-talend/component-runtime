package org.talend.sdk.component.runtime.record;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.record.Schema;

class RecordImplWrapperTest {

    @Test
    void put() {
        RecordBuilderFactoryImpl factory = new RecordBuilderFactoryImpl("test");
        Schema.Entry field1 = factory.newEntryBuilder()
                .withName("field1")
                .withType(Schema.Type.STRING)
                .withNullable(true)
                .build();
        Schema.Entry field2 = factory.newEntryBuilder()
                .withName("field2")
                .withType(Schema.Type.STRING)
                .withNullable(true)
                .build();
        Schema schema = factory.newSchemaBuilder(Schema.Type.RECORD)
                .withEntry(field1)
                .withEntry(field2)
                .build();

        final Record record1 = factory.newRecordBuilder(schema)
                .withString(field1, "hello")
                .build();

        final Map<String, Object> values = new HashMap<>();
        values.put("field2", "value2");
        final Record record2 = record1.put(values);
        values.clear();

        Assertions.assertNotEquals(record1, record2);
        Assertions.assertEquals("hello", record2.getString("field1"));
        Assertions.assertEquals("value2", record2.getString("field2"));
        Assertions.assertNull( record1.getString("field2"));

        Record next = record2;
        for (int i = 1; i <= 5; i++) {
            values.put("field1", "f1_value" + i);
            values.put("field2", "f2_value" + i);
            final Record rec = next.put(values);

            Assertions.assertNotEquals("f1_value" + i, next.getString("field1"));
            Assertions.assertNotEquals("f2_value" + i, next.getString("field2"));

            Assertions.assertEquals("f1_value" + i, rec.getString("field1"));
            Assertions.assertEquals("f2_value" + i, rec.getString("field2"));
            next = rec;
        }
    }
}