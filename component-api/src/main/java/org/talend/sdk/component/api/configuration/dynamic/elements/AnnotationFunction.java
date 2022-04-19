package org.talend.sdk.component.api.configuration.dynamic.elements;

import java.lang.annotation.Annotation;
import java.util.List;

import lombok.RequiredArgsConstructor;

public interface AnnotationFunction {

    @RequiredArgsConstructor
    class Value {
        public final String name;

        public final String value;
    }

    List<Value> scanAnnotation(final Annotation annotation);
}
