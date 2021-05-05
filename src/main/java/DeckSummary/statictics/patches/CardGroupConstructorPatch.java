package DeckSummary.statictics.patches;


import DeckSummary.statictics.StatCollection;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { CardGroup.CardGroupType.class })
public class CardGroupConstructorPatch {
    @SpirePostfixPatch
    public static void initialiseStatCollectionField(CardGroup cardGroup) {
        StatCollection stats = new StatCollection(cardGroup);
        stats.update();
        StatCollectionField.set(cardGroup, stats);
    }
}
