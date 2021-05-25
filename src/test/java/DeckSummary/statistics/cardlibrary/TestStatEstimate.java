package DeckSummary.statistics.cardlibrary;

import DeckSummary.statistics.ast.Expression;
import DeckSummary.statistics.ast.Operation;
import DeckSummary.statistics.ast.StatValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatEstimate {
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatEstimate.class, new StatEstimate.Deserializer())
            .registerTypeAdapter(Expression.class, new Expression.Deserializer())
            .registerTypeAdapter(Operation.Operator.class, new Operation.Operator.Deserializer())
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer())
            .create();

    @Test
    public void testStatValueDeserialization() {
        assertEquals(new StatEstimate(new StatValue(1, StatValue.ValueType.CONSTANT),
                new StatValue(1, StatValue.ValueType.CONSTANT)), gson.fromJson("1", StatEstimate.class));
    }

    @Test
    public void testStatEstimateDeserialization() {
        assertEquals(new StatEstimate(new StatValue(2, StatValue.ValueType.CONSTANT),
                new StatValue(3, StatValue.ValueType.MULTIPLE_OF_DAMAGE)),
                gson.fromJson("{\"conservative\": 2, \"optimistic\": {\"value\": 3, type: \"MULTIPLE_OF_DAMAGE\"}}",
                StatEstimate.class));
    }
}
