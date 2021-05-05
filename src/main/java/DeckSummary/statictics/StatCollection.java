package DeckSummary.statictics;

import DeckSummary.statictics.entries.AbstractStatisticEntry;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class StatCollection {
    private ArrayList<AbstractStatisticEntry> entries;
    private CardGroup cardGroup;
    public ArrayList<StatOutput> stats = new ArrayList<>();

    public StatCollection(CardGroup cardGroup, ArrayList<AbstractStatisticEntry> entries) {
        this.cardGroup = cardGroup;
        this.entries = entries;
    }

    public void update() {
        stats = entries.stream().map((entry) -> entry.count(cardGroup)).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
    }
}
