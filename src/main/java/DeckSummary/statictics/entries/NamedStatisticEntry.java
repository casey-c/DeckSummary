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
        int conservative = cardGroup.group.stream().map((card) -> CardStatsLibrary.hasEstimateForCard(card.cardID, tag) ?
                CardStatsLibrary.getEstimateForCard(card, tag).conservativeEstimate.evaluate(card) : 0)
                .reduce(0, Integer::sum);

        int optimistic = cardGroup.group.stream().map((card) -> CardStatsLibrary.hasEstimateForCard(card.cardID, tag) ?
                CardStatsLibrary.getEstimateForCard(card, tag).optimisticEstimate.evaluate(card) : 0)
                .reduce(0, Integer::sum);

        return (conservative > 0 || optimistic > 0) ?
                Optional.of(new StatOutput(displayName, conservative, optimistic)) : Optional.empty();
    }
}
