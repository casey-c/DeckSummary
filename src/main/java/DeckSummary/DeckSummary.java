package DeckSummary;

import DeckSummary.statistics.DeckStatsCommand;
import DeckSummary.statistics.cardlibrary.CardStatsLibrary;
import basemod.BaseMod;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AnchorPosition;
import easel.ui.text.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class DeckSummary implements PostInitializeSubscriber, RenderSubscriber {
    public static final Logger logger = LogManager.getLogger();
    public static void initialize() { new DeckSummary(); }

    public DeckSummary() {
        BaseMod.subscribe(this);
    }

    Label widget;

    @Override
    public void receivePostInitialize() {
        logger.info("DeckSummary init");

        CardStatsLibrary.load();
        ConsoleCommand.addCommand("deckstats", DeckStatsCommand.class);

        widget = new Label("Hello World");
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        widget.anchoredAt(InputHelper.mX, InputHelper.mY - 30, AnchorPosition.CENTER, 20)
                .render(sb);
    }
}
