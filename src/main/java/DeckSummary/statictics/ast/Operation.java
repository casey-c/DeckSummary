package DeckSummary.statictics.ast;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class Operation implements Expression {
    public Operator operator;
    public ArrayList<Expression> values;
    public Operation(Operator operator, ArrayList<Expression> values) {
        this.operator = operator;
        this.values = values;
    }

    public enum Operator {
        ADD, SUB, MUL, DIV;

        public int apply(int a, int b) {
            switch (this) {
                case ADD:
                    return a + b;
                case SUB:
                    return a - b;
                case MUL:
                    return a * b;
                case DIV:
                    return a / b;
                default:
                    return 0;
            }
        }

        public static class Deserializer implements JsonDeserializer<Operator> {
            @Override
            public Operator deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
                    throws JsonParseException {
                switch (jsonElement.getAsString()) {
                    case "+":
                        return ADD;
                    case "-":
                        return SUB;
                    case "*":
                        return MUL;
                    case "/":
                        return DIV;
                }

                return null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return operator == operation.operator && Objects.equals(values, operation.values);
    }

    @Override
    public int evaluate(AbstractCard card) {
        return values.stream().map((expr) -> expr.evaluate(card)).reduce(operator::apply).orElse(0);
    }
}
