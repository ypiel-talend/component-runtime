package org.talend.sdk.component.api.configuration.dynamic.elements;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class CompositeForm extends ElementForm {

    private final Map<String, ElementForm> innerForms = new HashMap<>();

    private CompositeForm(CompositeForm.Builder builder) {
        super(builder);
        builder.innerFormsBuilder.forEach(
                (String key, ElementForm.Builder value) -> this.innerForms.put(key, value.build())
        );
    }

    @Override
    public JsonObjectBuilder toJson(final AnnotationFunction fct) {
        final JsonObjectBuilder builder = super.toJson(fct);
        this.innerForms.forEach(
                (String key, ElementForm form) -> builder.add(key, form.toJson(fct))
        );
        return builder;
    }

    public static CompositeForm.Builder builder() {
        return new CompositeForm.Builder().withType("OBJECT");
    }

    public static class Builder extends ElementForm.Builder<CompositeForm, CompositeForm.Builder> {

        private final Map<String, ElementForm.Builder> innerFormsBuilder = new HashMap<>();

        @Override
        protected CompositeForm.Builder self() {
            return this;
        }

        @Override
        public CompositeForm build() {
            return new CompositeForm(this.self());
        }

        public CompositeForm.Builder add(final String name, final ElementForm.Builder builder) {
            this.innerFormsBuilder.put(name, builder);
            return this.self();
        }
    }
}
