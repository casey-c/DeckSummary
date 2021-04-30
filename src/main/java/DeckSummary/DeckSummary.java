package DeckSummary;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class DeckSummary implements PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger();
    public static void initialize() { new DeckSummary(); }

    public DeckSummary() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("DeckSummary init");
    }
}
