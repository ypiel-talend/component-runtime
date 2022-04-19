package org.talend.sdk.component.api.configuration.dynamic.elements;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.talend.sdk.component.api.configuration.condition.ActiveIf;
import org.talend.sdk.component.api.configuration.condition.ActiveIfs;

public class ConditionBuilder {

    static class SimpleConditionBuilder extends ConditionBuilder {
        private String target;

        private List<String> values = new ArrayList<>(3);

        private boolean negate = false;

        private ActiveIf.EvaluationStrategy evaluationStrategy =  ActiveIf.EvaluationStrategy.DEFAULT;

        private List<ActiveIf.EvaluationStrategyOption> options =  new ArrayList<>(3);

        @Override
        public Annotation build() {
            return new ActiveIf() {
                @Override
                public String target() {
                    return SimpleConditionBuilder.this.target;
                }

                @Override
                public String[] value() {
                    return SimpleConditionBuilder.this.values.toArray(new String[]{});
                }

                @Override
                public boolean negate() {
                    return SimpleConditionBuilder.this.negate;
                }

                @Override
                public EvaluationStrategy evaluationStrategy() {
                    return SimpleConditionBuilder.this.evaluationStrategy;
                }

                @Override
                public EvaluationStrategyOption[] evaluationStrategyOptions() {
                    return SimpleConditionBuilder.this.options.toArray(new EvaluationStrategyOption[0]);
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return ActiveIf.class;
                }
            };
        }

        public SimpleConditionBuilder withTarget(final String target) {
            this.target = target;
            return this;
        }

        public SimpleConditionBuilder withNegate(final boolean negate) {
            this.negate = negate;
            return this;
        }

        public SimpleConditionBuilder addValue(final String value) {
            this.values.add(value);
            return this;
        }

        public SimpleConditionBuilder withEvaluationStrategy(final ActiveIf.EvaluationStrategy evaluationStrategy) {
            this.evaluationStrategy = evaluationStrategy;
            return this;
        }


        public SimpleConditionBuilder addEvaluationStrategyOption(final String name, final String value) {
            ActiveIf.EvaluationStrategyOption option = new ActiveIf.EvaluationStrategyOption() {
                @Override
                public String name() {
                    return name;
                }

                @Override
                public String value() {
                    return value;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return ActiveIf.EvaluationStrategyOption.class;
                }
            };
            this.options.add(option);
            return this;
        }


    }

    private ActiveIfs.Operator operator = ActiveIfs.Operator.AND;

    private List<ConditionBuilder> conditions = new ArrayList<>(2);


    public Annotation build() {
        if (this.conditions.size() == 1) {
            return this.conditions.iterator().next().build();
        }
        if (conditions.size() > 1) {
            return new ActiveIfs() {
                @Override
                public Operator operator() {
                    return ConditionBuilder.this.operator;
                }

                @Override
                public ActiveIf[] value() {
                    return ConditionBuilder.this.conditions.stream()
                            .map(ConditionBuilder::build)
                            .map(ActiveIf.class::cast)
                            .collect(Collectors.toList())
                            .toArray(new ActiveIf[0]);
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return ActiveIfs.class;
                }
            };
        }
        return null;
    }

}
