/**
 * Copyright (C) 2006-2022 Talend Inc. - www.talend.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.talend.sdk.component.form.internal.converter;

import java.util.Collection;
import java.util.stream.Stream;

import org.talend.sdk.component.server.front.model.SimplePropertyDefinition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PropertyContext<T> {

    private final SimplePropertyDefinition property;

    private final T rootContext;

    private final Configuration configuration;

    public boolean isRequired() {
        return property.getValidation() != null && property.getValidation().getRequired() != null
                && property.getValidation().getRequired();
    }

    public boolean isDirectChild(final SimplePropertyDefinition child) {
        final String prefix = property.getPath() + ".";
        return child.getPath().startsWith(prefix) && child.getPath().indexOf('.', prefix.length()) < 0
                && !child.getPath().endsWith("[]");
    }

    public Stream<SimplePropertyDefinition> findDirectChild(final Collection<SimplePropertyDefinition> properties) {
        return properties.stream().filter(this::isDirectChild);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Configuration {

        private boolean includeDocumentationMetadata;
    }
}
