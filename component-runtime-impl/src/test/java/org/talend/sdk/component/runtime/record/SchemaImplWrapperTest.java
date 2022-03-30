package org.talend.sdk.component.runtime.record;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.talend.sdk.component.api.record.Schema;

class SchemaImplWrapperTest {

    @Test
    void test() {
        RecordBuilderFactoryImpl factory = new RecordBuilderFactoryImpl("test");
        final Schema.Entry field1 = factory.newEntryBuilder()
                .withName("field1")
                .withType(Schema.Type.STRING)
                .withNullable(true)
                .build();
        final Schema.Entry field2 = factory.newEntryBuilder()
                .withName("field2")
                .withType(Schema.Type.STRING)
                .withNullable(true)
                .build();
        final Schema schema = factory.newSchemaBuilder(Schema.Type.RECORD)
                .withEntry(field1)
                .withEntry(field2)
                .build();

        final Schema.Entry field3 = factory.newEntryBuilder()
                .withName("field3")
                .withType(Schema.Type.STRING)
                .withNullable(true)
                .build();
        Schema schema1 = schema.addEntries(Arrays.asList(field3));
        Assertions.assertNotEquals(schema, schema1);
        Assertions.assertNull(schema.getEntry("field3"));
        Assertions.assertSame(field3, schema1.getEntry("field3"));
    }

}