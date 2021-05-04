package DeckSummary.statictics.entries;

import DeckSummary.statictics.StatOutput;
import DeckSummary.statictics.cardlibrary.CardStatsLibrary;
import com.megacrit.cardcrawl.cards.CardGroup;

public class NamedStatisticEntry extends AbstractStatisticEntry {
    public String displayName;
    public String tag;

    public NamedStatisticEntry(String displayName, String tag) {
        this.displayName = displayName;
        this.tag = tag;
    }

    @Override
    public StatOutput count(CardGroup cardGroup) {
        int count = cardGroup.group.stream().map((card) -> {
            if (CardStatsLibrary.hasStatForCard(card.cardID, tag)) {
                return CardStatsLibrary.getStatForCard(card.cardID, tag).ofCard(card);
            } else {
                return 0;
            }
        }).reduce(0, Integer::sum);

        return new StatOutput(count > 0, displayName, count);
    }
}
