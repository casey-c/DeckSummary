package DeckSummary.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;

public class NinePatchWidget extends AbstractWidget<NinePatchWidget> {
    private NinePatch np;

    private float prefWidth;
    private float prefHeight;

    private Color renderColor = Color.WHITE;

    public NinePatchWidget(TextureRegion texture, float width, float height) {
        this.np = new NinePatch(texture, 32, 32, 32, 32);

        this.prefWidth = width;
        this.prefHeight = height;
    }

    public NinePatchWidget(Texture texture, float width, float height) {
        this.np = new NinePatch(texture, 32, 32, 32, 32);

        this.prefWidth = width;
        this.prefHeight = height;
    }

    public NinePatchWidget withColor(Color renderColor) {
        this.renderColor = renderColor;
        return this;
    }

    @Override public float getPreferredContentWidth() { return prefWidth; }
    @Override public float getPreferredContentHeight() { return prefHeight; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(renderColor);
        np.draw(sb,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getPreferredContentWidth() * Settings.xScale,
                getPreferredContentHeight() * Settings.yScale);
    }
}
