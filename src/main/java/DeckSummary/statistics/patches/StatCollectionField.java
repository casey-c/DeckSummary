package DeckSummary.statistics.patches;

import DeckSummary.statistics.StatCollection;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(clz = CardGroup.class, method = SpirePatch.CLASS)
public class StatCollectionField {
    public static SpireField<StatCollection> field = new SpireField<>(() -> null);

    public static StatCollection get(CardGroup group) {
        return field.get(group);
    }
    public static void set(CardGroup group, StatCollection stats) {
        field.set(group, stats);
    }
}
