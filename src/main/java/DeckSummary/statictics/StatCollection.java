package DeckSummary.statictics;

import DeckSummary.statictics.entries.AbstractStatisticEntry;
import DeckSummary.statictics.entries.NamedStatisticEntry;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.Arrays;
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

    public StatCollection(CardGroup cardGroup) {
        this(cardGroup, new ArrayList<>(Arrays.asList(
                new NamedStatisticEntry("Damage", "damage"),
                new NamedStatisticEntry("Block", "block"),
                new NamedStatisticEntry("Draw", "draw"),
                new NamedStatisticEntry("Energy", "energy"),
                // Silent
                new NamedStatisticEntry("Poison", "poison"),
                // Defect
                new NamedStatisticEntry("Lightning", "lightning"),
                new NamedStatisticEntry("Frost", "frost"),
                new NamedStatisticEntry("Dark", "dark"),
                new NamedStatisticEntry("Plasma", "plasma"),
                // Watcher
                new NamedStatisticEntry("Calm", "calm"),
                new NamedStatisticEntry("Wrath", "wrath"),
                new NamedStatisticEntry("Exit", "empty"),
                new NamedStatisticEntry("Divinity", "divinity"),
                new NamedStatisticEntry("Mantra", "mantra"),
                new NamedStatisticEntry("Mark", "mark"),
                new NamedStatisticEntry("Scry Instances", "scry_instance"),
                new NamedStatisticEntry("Scry Potential", "scry_potential"),
                // Colourless
                new NamedStatisticEntry("Heal", "heal")
        )));
    }

    public void update() {
        stats = entries.stream().map((entry) -> entry.count(cardGroup)).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
    }
}
