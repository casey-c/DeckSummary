package DeckSummary.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;

public class NinePatchBaseTrimWidget extends AbstractWidget<NinePatchBaseTrimWidget> {
    private NinePatch baseNp;
    private NinePatch trimNp;

    private float prefWidth;
    private float prefHeight;

    private Color baseColor = Color.WHITE;
    private Color trimColor = Color.WHITE;

    private static final int PATCH_SIZE = 32;

    public NinePatchBaseTrimWidget(TextureRegion baseTexture, TextureRegion trimTexture, float width, float height) {
        this.baseNp = new NinePatch(baseTexture, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE);
        this.trimNp = new NinePatch(trimTexture, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE);

        this.prefWidth = width;
        this.prefHeight = height;
    }

    public NinePatchBaseTrimWidget(Texture baseTexture, Texture trimTexture, float width, float height) {
        this.baseNp = new NinePatch(baseTexture, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE);
        this.trimNp = new NinePatch(trimTexture, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE, PATCH_SIZE);

        this.prefWidth = width;
        this.prefHeight = height;
    }

    public NinePatchBaseTrimWidget withColors(Color baseColor, Color trimColor) {
        this.baseColor = baseColor;
        this.trimColor = trimColor;
        return this;
    }

    @Override public float getPreferredContentWidth() { return prefWidth; }
    @Override public float getPreferredContentHeight() { return prefHeight; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(baseColor);
        baseNp.draw(sb,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getPreferredContentWidth() * Settings.xScale,
                getPreferredContentHeight() * Settings.yScale);

        sb.setColor(trimColor);
        trimNp.draw(sb,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getPreferredContentWidth() * Settings.xScale,
                getPreferredContentHeight() * Settings.yScale);
    }
}
