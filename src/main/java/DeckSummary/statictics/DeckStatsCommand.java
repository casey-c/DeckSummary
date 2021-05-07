package DeckSummary.statictics;

import DeckSummary.statictics.patches.StatCollectionField;
import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DeckStatsCommand extends ConsoleCommand {
    public DeckStatsCommand() {
        minExtraTokens = 0;
        maxExtraTokens = 0;

        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] _args, int _depth) {
        StatCollection statCollection = StatCollectionField.get(AbstractDungeon.player.masterDeck);
        statCollection.update();
        statCollection.stats.forEach((stat) -> DevConsole.log(stat.name + ": " + stat.value));
    }
}
