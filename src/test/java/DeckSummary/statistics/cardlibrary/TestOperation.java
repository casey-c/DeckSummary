package DeckSummary.statistics.cardlibrary;

import DeckSummary.statistics.ast.Operation;
import DeckSummary.statistics.ast.StatValue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOperation {
    @Test
    public void testArithmetics() {
        Operation op = new Operation(Operation.Operator.ADD, new ArrayList<>(Arrays.asList(
                new StatValue(2, StatValue.ValueType.CONSTANT),
                new StatValue(3, StatValue.ValueType.CONSTANT)
        )));
        assertEquals(5, op.evaluate(null));

        op.operator = Operation.Operator.SUB;
        assertEquals(-1, op.evaluate(null));

        op.operator = Operation.Operator.MUL;
        assertEquals(6, op.evaluate(null));

        op.operator = Operation.Operator.DIV;
        assertEquals(2 / 3, op.evaluate(null));
    }

    @Test
    public void testChainedArithmetics() {
        Operation op = new Operation(Operation.Operator.ADD, new ArrayList<>(Arrays.asList(
                new StatValue(1, StatValue.ValueType.CONSTANT),
                new StatValue(2, StatValue.ValueType.CONSTANT),
                new StatValue(3, StatValue.ValueType.CONSTANT)
        )));
        assertEquals(6, op.evaluate(null));

        op.operator = Operation.Operator.SUB;
        assertEquals(-4, op.evaluate(null));

        op.operator = Operation.Operator.MUL;
        assertEquals(6, op.evaluate(null));

        op.operator = Operation.Operator.DIV;
        assertEquals(1 / 6, op.evaluate(null));
    }
}
