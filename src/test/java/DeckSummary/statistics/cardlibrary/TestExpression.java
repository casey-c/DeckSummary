package DeckSummary.statistics.cardlibrary;

import DeckSummary.statictics.ast.Expression;
import DeckSummary.statictics.ast.Operation;
import DeckSummary.statictics.ast.StatValue;
import DeckSummary.statictics.cardlibrary.StatEstimate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestExpression {
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatEstimate.class, new StatEstimate.Deserializer())
            .registerTypeAdapter(Expression.class, new Expression.Deserializer())
            .registerTypeAdapter(Operation.Operator.class, new Operation.Operator.Deserializer())
            .registerTypeAdapter(StatValue.class, new StatValue.Deserializer())
            .create();

    @Test
    public void testValueDeserializer() {
        assertEquals(new StatValue(1, StatValue.ValueType.CONSTANT), gson.fromJson("1", Expression.class));
        assertEquals(new StatValue(0, StatValue.ValueType.NONE), gson.fromJson("NONE", Expression.class));
        assertEquals(new StatValue(3, StatValue.ValueType.MULTIPLE_OF_BLOCK),
                gson.fromJson("{\"value\": 3, \"type\": \"MULTIPLE_OF_BLOCK\"}", Expression.class));
    }

    @Test
    public void testOperationDeserializer() {
        assertEquals(new Operation(Operation.Operator.ADD, new ArrayList<>(Arrays.asList(
                new StatValue(2, StatValue.ValueType.CONSTANT),
                new StatValue(3, StatValue.ValueType.CONSTANT)
        ))), gson.fromJson("{\"operator\": \"+\", \"values\": [2, 3]}", Expression.class));
    }
}
