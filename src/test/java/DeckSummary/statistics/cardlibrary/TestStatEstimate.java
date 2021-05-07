package DeckSummary.statistics.cardlibrary;

import DeckSummary.statictics.cardlibrary.StatEstimate;
import DeckSummary.statictics.cardlibrary.StatValue;
import DeckSummary.statictics.cardlibrary.ValueType;
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
        assertEquals(gson.fromJson("1", StatEstimate.class), new StatEstimate(
                new StatValue(1, ValueType.CONSTANT), new StatValue(1, ValueType.CONSTANT)));
    }

    @Test
    public void testStatEstimateDeserialization() {
        assertEquals(gson.fromJson("{\"conservative\": 2, \"optimistic\": {\"value\": 3, type: \"MULTIPLE_OF_DAMAGE\"}}",
                StatEstimate.class), new StatEstimate(new StatValue(2, ValueType.CONSTANT),
                new StatValue(3, ValueType.MULTIPLE_OF_DAMAGE)));
    }
}