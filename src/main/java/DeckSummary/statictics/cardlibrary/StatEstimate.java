package DeckSummary.statictics.cardlibrary;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class StatEstimate {
    @SerializedName("conservative")
    public StatValue conservativeEstimate;
    @SerializedName("optimistic")
    public StatValue optimisticEstimate;

    public StatEstimate(StatValue conservativeEstimate, StatValue optimisticEstimate) {
        this.conservativeEstimate = conservativeEstimate;
        this.optimisticEstimate = optimisticEstimate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatEstimate)) return false;
        StatEstimate that = (StatEstimate) o;
        return this.conservativeEstimate.equals(that.conservativeEstimate) &&
                this.optimisticEstimate.equals(that.optimisticEstimate);
    }

    public static class Deserializer implements JsonDeserializer<StatEstimate> {
        @Override
        // The stat estimate can be serialized on two main ways to make entering values easier.
        // First, it can be represented by a singular StatValue, which will be deserialized as a StatValue and copied
        // for both the conservative and optimistic estimate.
        // Second, it can be an object containing both the conservative and optimistic estimate in StatValue, in which
        // case it will be deserialized as a StatEstimate object directly.
        public StatEstimate deserialize(JsonElement jsonElement, Type _type, JsonDeserializationContext context)
                throws JsonParseException {
            // Yes, I can collapse the two if statements into one, but repeatedly using getAsJsonObject is not pretty.
            if (jsonElement.isJsonObject()) {
                JsonObject object = jsonElement.getAsJsonObject();
                // If the json element contains both a conservative and optimistic field, it will be considered a
                // StatEstimate and deserialized directly.
                if (object.has("conservative") && object.has("optimistic")) {
                    return new StatEstimate(context.deserialize(object.get("conservative"), StatValue.class),
                            context.deserialize(object.get("optimistic"), StatValue.class));
                }
            }

            // Otherwise it will be deserialized as a StatValue
            StatValue neutralEstimate = context.deserialize(jsonElement, StatValue.class);
            return new StatEstimate(neutralEstimate, neutralEstimate);
        }
    }
}
