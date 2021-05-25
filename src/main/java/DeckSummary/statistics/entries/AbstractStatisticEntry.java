package DeckSummary.statistics.entries;

import DeckSummary.statistics.StatOutput;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.Optional;

public abstract class AbstractStatisticEntry {
    public abstract Optional<StatOutput> count(CardGroup cardGroup);
}
