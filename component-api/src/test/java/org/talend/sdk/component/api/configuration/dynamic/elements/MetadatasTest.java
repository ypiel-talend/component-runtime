package org.talend.sdk.component.api.configuration.dynamic.elements;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.talend.sdk.component.api.configuration.constraint.Pattern;
import org.talend.sdk.component.api.configuration.constraint.Required;

class MetadatasTest {

    @Test
    void buildTest() {
        final Metadatas metadatas = Metadatas.builder()
                .addRequired()
                .addPattern("^[A-Z]$")
                .build();
        Assertions.assertEquals(2, metadatas.map(this::isOK).filter(Boolean.TRUE::equals).count());
    }

    boolean isOK(Annotation annotation) {
        if (annotation instanceof Required) {
            return true;
        }
        if (annotation instanceof Pattern) {
            Pattern p = (Pattern) annotation;
            Assertions.assertEquals("^[A-Z]$", p.value());
            return true;
        }
        return false;
    }

}