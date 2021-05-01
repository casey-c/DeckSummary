package DeckSummary.ui;

import DeckSummary.DeckSummary;
import DeckSummary.utils.ExtraColors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PieChartWidget extends AbstractWidget<PieChartWidget> {
    private float width;
    private float height;

    private ArrayList<Pair<Float, Color>> distribution = new ArrayList<>();

    public PieChartWidget() {
        this.width = 100;
        this.height = 100;

        List<Pair<Integer, Color>> vals = Arrays.asList(
                Pair.of(6, ExtraColors.ATTACK_DARK),
                Pair.of(5, ExtraColors.SKILL_DARK),
                Pair.of(0, ExtraColors.POWER_DARK),
                Pair.of(1, ExtraColors.CURSE_DARK)
        );

        float sum = vals.stream().map(a -> a.getLeft()).reduce((a, b)-> a + b).get();
        DeckSummary.logger.info("Sum: " + sum);

        for (Pair<Integer, Color> pair : vals)
            distribution.add(Pair.of((float)pair.getLeft() / sum, pair.getRight()));

        DeckSummary.logger.info("Distr: " + distribution);

    }

    @Override public float getPreferredContentWidth() { return width; }
    @Override public float getPreferredContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        // Pause the spritebatch
        sb.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setAutoShapeType(true);

        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.setTransformMatrix(sb.getTransformMatrix());

        // TODO: need some manual anti-aliasing to make it look non pixelated (decrease opacity, increase radius by 1px,
        //  and re-render. Repeat multiple (4+?) times)
        float radius = width;
        final float r = 1.0f;
        final float g = 0.0f;
        final float b = 0.0f;
        float a = 1.0f;
        final int NUM_SAMPLES = 16;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

//        for (int i = 0; i < NUM_SAMPLES; ++i) {
//            sr.setColor(new Color(r, g, b, a));
//            sr.circle(getContentCenterX(), getContentCenterY(), radius);
//
//            a *= 0.5f;
//            radius += 0.1f;
//        }

        float start = 0;
        for (Pair<Float, Color> slice : distribution) {
            float degrees = slice.getLeft() * 360;
            Color color = slice.getRight();

            sr.setColor(color);
            sr.arc(getContentCenterX(), getContentCenterY(), width, start, degrees);

            start += degrees;
        }

        sr.end();

        // Borders
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(Color.WHITE);
        Vector2 center = new Vector2(getContentCenterX(), getContentCenterY());

        float radians = 0.0f;
        for (Pair<Float, Color> slice : distribution) {
            Vector2 target = new Vector2((float)Math.cos(radians), (float)Math.sin(radians))
                    .scl(radius)
                    .add(center);

            sr.rectLine(center, target, 4);

            radians += slice.getLeft() * (float)Math.PI * 2.0f;
        }

        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.circle(getContentCenterX(), getContentCenterY(), radius);
        sr.end();

        // Resume the spritebatch
        sb.begin();
    }
}
