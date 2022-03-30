package org.talend.sdk.component.runtime.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.talend.sdk.component.api.record.Schema;

public class SchemaImplWrapper implements Schema {

    private final Schema wrappedSchema;

    private final List<Entry> newEntries = new ArrayList<>();

    private final List<Entry> newMetaEntries = new ArrayList<>();

    public SchemaImplWrapper(Schema wrappedSchemaInit, Iterable<Entry> newEntries) {
        if (wrappedSchemaInit instanceof SchemaImplWrapper) {
            final SchemaImplWrapper wrapper = (SchemaImplWrapper) wrappedSchemaInit;
            this.wrappedSchema = wrapper.wrappedSchema;
            this.newEntries.addAll(wrapper.newEntries);
            this.newMetaEntries.addAll(wrapper.newMetaEntries);
        }
        else {
            this.wrappedSchema = wrappedSchemaInit;
        }
        newEntries.forEach((Entry e) -> {
            if (!e.isMetadata()) this.newEntries.add(e);
        });
        newEntries.forEach((Entry e) -> {
            if (e.isMetadata()) this.newMetaEntries.add(e);
        });
    }

    @Override
    public Type getType() {
        return Type.RECORD;
    }

    @Override
    public Schema getElementSchema() {
        return null;
    }

    @Override
    public List<Entry> getEntries() {
        return Stream.concat(this.wrappedSchema.getEntries().stream(),
                this.newEntries.stream()).collect(Collectors.toList());
    }

    @Override
    public List<Entry> getMetadata() {
        return Stream.concat(this.wrappedSchema.getMetadata().stream(),
                this.newMetaEntries.stream()).collect(Collectors.toList());
    }

    @Override
    public Schema addEntries(final Iterable<Entry> newEntries) {
        return new SchemaImplWrapper(this, newEntries);
    }

    @Override
    public Stream<Entry> getAllEntries() {
        return Stream.concat(this.wrappedSchema.getAllEntries(),
                Stream.concat(this.newEntries.stream(),
                this.newMetaEntries.stream()));
    }

    @Override
    public Map<String, String> getProps() {
        return this.wrappedSchema.getProps();
    }

    @Override
    public String getProp(String property) {
        return this.wrappedSchema.getProp(property);
    }
}
