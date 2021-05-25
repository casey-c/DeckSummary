package DeckSummary.statistics.ast;

import com.google.gson.*;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.lang.reflect.Type;

public interface Expression {
    int evaluate(AbstractCard card);

    class Deserializer implements JsonDeserializer<Expression> {
        @Override
        public Expression deserialize(JsonElement jsonElement, Type _type, JsonDeserializationContext context)
                throws JsonParseException {
            if (jsonElement.isJsonObject()) {
                JsonObject object = jsonElement.getAsJsonObject();
                // if it contains an operator and values, deserialise it as an operation.
                if (object.has("operator") && object.has("values")) {
                    return context.deserialize(object, Operation.class);
                }
            }
            return context.deserialize(jsonElement, StatValue.class);
        }
    }
}
