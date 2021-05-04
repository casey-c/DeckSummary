package DeckSummary.statictics.entries;

import DeckSummary.statictics.StatOutput;
import com.megacrit.cardcrawl.cards.CardGroup;

public abstract class AbstractStatisticEntry {
    public abstract StatOutput count(CardGroup cardGroup);
}
