package org.talend.sdk.component.api.configuration.dynamic.elements;

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class PrimitivForm extends ElementForm {

    protected PrimitivForm(PrimitivForm.Builder builder) {
        super(builder);
    }

    /*@Override
    //public JsonObjectBuilder toJson() {
        return null;
    }*/

    public static class Builder extends ElementForm.Builder<PrimitivForm, PrimitivForm.Builder> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PrimitivForm build() {
            return new PrimitivForm(this);
        }
    }

    public static PrimitivForm.Builder stringElement(final String name) {
        return new PrimitivForm.Builder().withType("STRING").withName(name);
    }
}
