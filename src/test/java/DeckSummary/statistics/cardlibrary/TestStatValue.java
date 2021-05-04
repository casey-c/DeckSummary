package DeckSummary.statistics.cardlibrary;

import DeckSummary.statictics.cardlibrary.StatValue;
import DeckSummary.statictics.cardlibrary.ValueType;
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
        assertEquals(gson.fromJson("1", StatValue.class), new StatValue(1, ValueType.CONSTANT));
    }

    @Test
    public void testEnumDeserialization() {
        assertEquals(gson.fromJson("NONE", StatValue.class), new StatValue(0, ValueType.NONE));
    }

    @Test
    public void testObjectDeserialization() {
        assertEquals(gson.fromJson("{\"value\": 3, \"type\": \"MULTIPLE_OF_BLOCK\"}", StatValue.class),
                new StatValue(3, ValueType.MULTIPLE_OF_BLOCK));
    }
}
