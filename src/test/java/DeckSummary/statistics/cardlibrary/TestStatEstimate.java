package DeckSummary.statistics.cardlibrary;

import DeckSummary.statictics.cardlibrary.StatEstimate;
import DeckSummary.statictics.ast.StatValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatEstimate {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer())
            .registerTypeAdapter(StatEstimate.class, new StatEstimate.Deserializer())
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
