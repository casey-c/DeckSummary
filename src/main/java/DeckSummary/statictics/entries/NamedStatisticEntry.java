package DeckSummary.statictics.entries;

import DeckSummary.statictics.StatOutput;
import DeckSummary.statictics.cardlibrary.CardStatsLibrary;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.Optional;

public class NamedStatisticEntry extends AbstractStatisticEntry {
    public String displayName;
    public String tag;

    public NamedStatisticEntry(String displayName, String tag) {
        this.displayName = displayName;
        this.tag = tag;
    }

    @Override
    public Optional<StatOutput> count(CardGroup cardGroup) {
        int count = cardGroup.group.stream().map((card) -> CardStatsLibrary.hasStatForCard(card.cardID, tag) ?
                CardStatsLibrary.getStatForCard(card.cardID, tag).ofCard(card) : 0).reduce(0, Integer::sum);

        return count > 0 ? Optional.of(new StatOutput(displayName, count)) : Optional.empty();
    }
}
