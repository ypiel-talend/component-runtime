package org.talend.sdk.component.api.configuration.dynamic.elements;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.talend.sdk.component.api.configuration.constraint.Pattern;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.ui.widget.Credential;

public class Metadatas implements Serializable {

    private final List<Annotation> annotations = new ArrayList<>();

    private Metadatas(Metadatas.Builder builder) {
        this.annotations.addAll(builder.annotations);
        if (builder.conditionBuilder != null) {
            this.annotations.add(builder.conditionBuilder.build());
        }
    }

    public <T> Stream<T> map(final Function<Annotation, T> transform) {
        return this.annotations.stream().map(transform);
    }

    public JsonObjectBuilder toJson(final AnnotationFunction fct) {
        if (this.annotations.isEmpty()) {
            return null;
        }
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        this.annotations.stream()
                .flatMap((Annotation a) -> fct.scanAnnotation(a).stream())
                .forEach((AnnotationFunction.Value v) -> objectBuilder.add(v.name, v.value));
        return objectBuilder;
    }

    public static Metadatas.Builder builder() {
        return new Metadatas.Builder();
    }

    public static class Builder {
        private static final Annotation required = new Required() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Required.class;
            }
        };

        private static final Annotation credentials  = new Credential() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Credential.class;
            }
        };

        private final List<Annotation> annotations = new ArrayList<>();

        private ConditionBuilder conditionBuilder = null;

        private Metadatas.Builder add(final Annotation annotation) {
            this.annotations.add(annotation);
            return this;
        }

        public Metadatas.Builder addRequired() {
            return this.add(Builder.required);
        }

        public Metadatas.Builder addCredential() {
            return this.add(Builder.credentials);
        }

        public Metadatas.Builder addPattern(final String pattern) {
            final Annotation patternAnnotation = new Pattern() {
                @Override
                public String value() {
                    return pattern;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Pattern.class;
                }
            };
            return this.add(patternAnnotation);
        }

        public Metadatas.Builder withConditionBuilder(final ConditionBuilder condition) {
            this.conditionBuilder = condition;
            return this;
        }

        public Metadatas build() {
            return new Metadatas(this);
        }
    }
}
