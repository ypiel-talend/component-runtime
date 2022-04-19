package org.talend.sdk.component.api.configuration.dynamic;

import javax.json.JsonObject;

import org.talend.sdk.component.api.configuration.dynamic.elements.AnnotationFunction;
import org.talend.sdk.component.api.configuration.dynamic.elements.CompositeForm;

public class Form {

    private CompositeForm objectForm;

    public JsonObject toJson(AnnotationFunction fct) {
        return objectForm.toJson(fct).build();
    }
}
