package DeckSummary.statistics.cardlibrary;

import DeckSummary.statictics.ast.StatValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestStatValue {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer())
            .create();

    @Test
    public void testNumberDeserialization() {
        assertEquals(new StatValue(1, StatValue.ValueType.CONSTANT), gson.fromJson("1", StatValue.class));
    }

    @Test
    public void testEnumDeserialization() {
        assertEquals(new StatValue(0, StatValue.ValueType.NONE), gson.fromJson("NONE", StatValue.class));
    }

    @Test
    public void testObjectDeserialization() {
        assertEquals(new StatValue(3, StatValue.ValueType.MULTIPLE_OF_BLOCK),
                gson.fromJson("{\"value\": 3, \"type\": \"MULTIPLE_OF_BLOCK\"}", StatValue.class));
    }
}
