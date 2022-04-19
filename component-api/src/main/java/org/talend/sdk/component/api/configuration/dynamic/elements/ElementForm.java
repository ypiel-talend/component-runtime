package org.talend.sdk.component.api.configuration.dynamic.elements;

import java.io.Console;
import java.io.Serializable;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public abstract class ElementForm implements Serializable {

    private final String name;

    private final String displayName;

    private final String placeHolder;

    private final String type;

    private final Metadatas metadatas;

    protected ElementForm(final ElementForm.Builder builder) {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.placeHolder = builder.placeHolder;
        this.type = builder.type;
        this.metadatas = builder.metadatasBuilder.build();
    }

    public JsonObjectBuilder toJson(final AnnotationFunction fct) {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        this.addField(objectBuilder, "name", this.name);
        this.addField(objectBuilder, "placeholder", this.placeHolder);
        this.addField(objectBuilder, "displayName", this.displayName);
        this.addField(objectBuilder, "type", this.type);

        //objectBuilder.add("metadata", this.metadatas.);

        return objectBuilder;
    }

    private void addField(JsonObjectBuilder objectBuilder, String name, String value) {
        if (value != null) {
            objectBuilder.add(name, value);
        }
    }

    public static abstract class Builder<T extends ElementForm, B extends ElementForm.Builder<T, B>> {

        private String name = null;

        private String displayName = null;

        private String type = null;

        private String placeHolder;

        private final Metadatas.Builder metadatasBuilder = new Metadatas.Builder();

        public B withName(final String name) {
            this.name = name;
            return this.self();
        }

        public B withDisplayName(final String displayName) {
            this.displayName = displayName;
            return this.self();
        }

        public B withPlaceHolder(final String placeHolder) {
            this.placeHolder = placeHolder;
            return this.self();
        }

        public B withType(final String type) {
            this.type = type;
            return this.self();
        }

        public B withMetadata(Consumer<Metadatas.Builder> fct) {
            fct.accept(this.metadatasBuilder);
            return this.self();
        }

        protected abstract B self();

        public abstract T build();
    }
}
