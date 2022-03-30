package org.talend.sdk.component.runtime.record;

import java.util.HashMap;
import java.util.Map;

import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.record.Schema;

public class RecordImplWrapper implements Record {

    private final Record wrapped;

    private final Map<String, Object> newValues = new HashMap<>();

    public RecordImplWrapper(Record wrapped, Map<String, Object> values) {
        if (wrapped instanceof RecordImplWrapper) {
            this.wrapped = ((RecordImplWrapper) wrapped).wrapped;
            this.newValues.putAll(((RecordImplWrapper) wrapped).newValues);
        }
        else {
            this.wrapped = wrapped;
        }
        this.newValues.putAll(values);
    }

    @Override
    public Schema getSchema() {
        return this.wrapped.getSchema();
    }

    @Override
    public Record put(Map<String, Object> newValues) {
        return new RecordImplWrapper(this, newValues);
    }

    @Override
    public <T> T get(Class<T> expectedType, String name) {
        if (this.newValues.containsKey(name)) {
            Object o = this.newValues.get(name);
            if (o != null && expectedType.isInstance(o)) {
                return expectedType.cast(o);
            }
            return null;
        }
        return this.wrapped.get(expectedType, name);
    }
}
