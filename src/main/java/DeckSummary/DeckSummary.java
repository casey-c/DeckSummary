package DeckSummary;

import DeckSummary.ui.AnchorPosition;
import DeckSummary.ui.NinePatchBaseTrimWidget;
import DeckSummary.ui.PieChartWidget;
import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class DeckSummary implements PostInitializeSubscriber, RenderSubscriber {
    public static final Logger logger = LogManager.getLogger();
    public static void initialize() { new DeckSummary(); }

    private NinePatchBaseTrimWidget w;
    private PieChartWidget pieChartWidget;

    public DeckSummary() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("DeckSummary init");

        w = new NinePatchBaseTrimWidget( new Texture("DeckSummary/tooltips/base.png"),
                                         new Texture("DeckSummary/tooltips/trim.png"),
                                   524, 300)
                .withColors(Color.FOREST, Color.GOLD)
                .anchorCenteredOnScreen();


        pieChartWidget = new PieChartWidget().anchorCenteredOnScreen();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        // Follow the mouse, but don't let it render outside the screen
//        w.anchoredAt(InputHelper.mX, InputHelper.mY, AnchorPosition.CENTER, false)
//         .snapIntoScreen(10)
//         .render(sb);

//        w.render(sb);

        pieChartWidget.render(sb);
    }
}
