package DeckSummary.ui;

/*
  Widgets can define the width/height of their content area dynamically - there is built in margin code to make padding
  a bit easier, but widgets are mostly concerned about where their getContentLeft() etc. are located for rendering calls.
  Following convention, all floats are considered unscaled and refer to 1080p space; and should be scaled during the
  final rendering calls only to make calculations and offsets easier.

e.g.
  getLeft()                    getContentRight()
  |                            |
  v                            v
  +---------------------------------+
  |         MARGINS                 |
  |    +-----------------------+    |
  |    |    CONTENT            |    |
  |    +-----------------------+    |
  |                                 |
  +---------------------------------+
  ^
  |
  (x,y) is always bottom left in internal memory, but widgets won't access these values directly - (they use the
  getContent_() calls)

  The general pattern I follow to work with widgets is based on a two step process, loosely modeled off of the IMGUI
  design pattern (https://en.wikipedia.org/wiki/Immediate_mode_GUI). Generally speaking, widgets maintain their
  localized sizing hints (they know they are W pixels wide and H pixels tall) and how to render themselves and any
  children. E.g. they may need to know to render a string of text 16 pixels to the right of getContentLeft(), but they
  don't need to (re)compute where that relative spot might be in the render loop, as the updates occur in a different
  step.

  Widgets DON'T (typically) manage their absolute locations - it's usually relative to where their parents put them. So,
  in this style widgets only care about rendering relatively from a "fixed" offset position - usually one of the four
  corners, or some sort of centered location using their preferred widths/heights.

  The two step process I try to follow is:

  Step 1: Widget Creation using constructors
    - Widgets are built with enough information to render themselves in a void. If a widget is more dynamic in size and
      requires more hints, usually this info should be passed in through the constructor if it can be helped; if it
      relies on the sizes of child widgets, these should be constructed during the constructor as well, so that once the
      parent is finished building, it knows everything it needs to. The idea is mostly that once the constructor
      finishes, using functions like getPreferredWidth or getPreferredHeight are complete/accurate.
  Step 2: Dynamic positioning using anchors
    - Once the widgets know their widths/heights/ and have built their children successfully, the next step is to anchor
      them onto the scene. This is used to update the positions of the getContentLeft() etc. family of functions
      necessary for rendering to put them "in the right spot". You should 100% ensure that any widget.render() calls
      occur AFTER at least one .anchoredAt() call, as the renderWidget() will rely on the relative coordinate system
      constructed by the anchoredAt. For most widgets that are immovable/fixed, this anchor is only needed once, and
      the "proper" pattern is to anchor right after the constructor. E.g.:

          Widget labelWidget = new Widget()
                               .anchoredAt(bottomLeftX, bottomLeftY, AnchorPosition.LEFT_BOTTOM);

      For widgets that need dynamic position (e.g. tooltips that follow the mouse), the general pattern I follow is to
      perform the anchoredAt() right before each render call.

      E.g., in a widget that manages two children, its widget.renderWidget() could look something like:

          // Use current mouse position
          float x = InputHelper.mX;
          float y = InputHelper.mY;

          child1.anchoredAt(x, y, AnchorPosition.LEFT_TOP);
          child1.render(sb);

          child2.anchoredAt(x, y, AnchorPosition.RIGHT_BOTTOM);
          child2.render(sb);

  --------------------

  Additional information:

    * The abstract widget class has some self-referencing generic shenanigans (I forget the exact name of this pattern
      off the top of my head) - i.e. when you make a new widget, you need to extend the AbstractWidget with the name of
      the new class passed in:

          public class MyCoolWidget extends AbstractWidget<MyCoolWidget> {...}
                                                           ~~~~~~~~~~~~ <- don't forget this!

      This ensures that the builder functions can be chained effectively and don't need casting when using. E.g.

          MyCoolWidget w = new MyCoolWidget().anchoredAt(...);

      instead of the more verbose:

          MyCoolWidget w = (MyCoolWidget)(new MyCoolWidget().anchoredAt(...));

       This pattern can save a lot of clutter casts if you use some of the other builder methods like .withMargins()
       etc.

    * anchoredAt() has an "instant" boolean parameter (true by default), if instant, the widget will be instantly moved
      into the passed position and will be rendered there next render frame. If you pass in false to this parameter, it
      instead uses some basic linear interpolation to shift it into position over the next few frames, letting it easily
      be animated into the spot it needs to be in.
    * Don't make custom widgets each subscribe to BaseMod's subscribers. Only the top most widget of the hierarchy
      should subscribe. If a widget has children, it just passes the render calls down the tree in its own render call.
      Similar idea for update(), show(), hide() etc.

 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;

import java.util.List;

public abstract class AbstractWidget<T extends AbstractWidget<T>> {
    private float marginLeft, marginRight, marginTop, marginBottom;

    private float x, y;
    private float targetX, targetY;

    public abstract float getPreferredContentWidth();
    public abstract float getPreferredContentHeight();

    // --------------------------------------------------------------------------------
    // Some basic builder methods. (Widgets will probably add some of their own)
    // --------------------------------------------------------------------------------

    public void setMargins(float all) { this.marginLeft = this.marginRight = this.marginTop = this.marginBottom = all; }
    public T withMargins(float all) {
        this.setMargins(all);
        return (T)this;
    }

    public void setMargins(float horizontal, float vertical) {
        this.marginLeft = this.marginRight = horizontal;
        this.marginBottom = this.marginTop = vertical;
    }
    public T withMargins(float horizontal, float vertical) {
        this.setMargins(horizontal, vertical);
        return (T)this;
    }

    public void setMargins(float left, float right, float bottom, float top) {
        this.marginLeft = left;
        this.marginRight = right;
        this.marginBottom = bottom;
        this.marginTop = top;
    }

    public T withMargins(float left, float right, float bottom, float top) {
        this.setMargins(left, right, bottom, top);
        return (T)this;
    }

    // --------------------------------------------------------------------------------
    // The final step of the pseudo-builder pattern. Required for rendering.
    // --------------------------------------------------------------------------------
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition) {
        return anchoredAt(x, y, anchorPosition, true);
    }

    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, boolean instant) {
        this.targetX = anchorPosition.isLeft() ? x : (anchorPosition.isCenterX() ? x - 0.5f * getWidth() : x - getWidth());
        this.targetY = anchorPosition.isBottom() ? y : (anchorPosition.isCenterY() ? y - 0.5f * getHeight() : y - getHeight());

        if (instant) {
            this.x = targetX;
            this.y = targetY;
        }

        return (T)this;
    }

    public T anchorCenteredOnScreen() {
        return anchorCenteredOnScreen(true);
    }

    public T anchorCenteredOnScreen(boolean instant) {
        float screenCenterX = (Settings.WIDTH / 2) / Settings.xScale;
        float screenCenterY = (Settings.HEIGHT / 2) / Settings.yScale;

        this.targetX = screenCenterX - (0.5f * getWidth());
        this.targetY = screenCenterY - (0.5f * getHeight());

        if (instant) {
            this.x = targetX;
            this.y = targetY;
        }

        return (T)this;
    }

    /*
      Prevent the left, right, top, bottom edges from going outside the screen, e.g.
       +--------+          +--------+
       |       xxx    ->   |     xxx|
       +--------+          +--------+
    */
    /**
     * Instantly snap the edges back into the viewing area. Note: the top right snapping takes priority if the widget
     * is larger than the screen, although that is undefined behavior (widgets should fit inside the view area).
     * @param border How close to the edge of the screen can be to this widget. E.g. a value of 0 means the left most
     *               edge of the widget can align exactly with the left most edge of the screen, but it cannot start
     *               rendering further to the left of the screen and get cut off.
     * @return this widget
     */
    public T snapIntoScreen(int border) {
        // TODO this function needs to account for xScale, yScale, as x and y are both in 1080p space, and
        //   Settings.WIDTH etc. may not be in the same coordinate system. (Need to verify / do testing here!)

        // Bottom left
        if (targetX < border)
            this.targetX = border;
        if (targetY < border)
            this.targetY = border;

        // Top right
        if (targetX + getWidth() > (Settings.WIDTH - border))
            this.targetX = Settings.WIDTH - border - getWidth();
        if (targetY + getHeight() > (Settings.HEIGHT - border))
            this.targetY = Settings.HEIGHT - border - getHeight();

        this.x = targetX;
        this.y = targetY;

        return (T)this;
    }

    // --------------------------------------------------------------------------------

    // These can be obtained before (x,y) are set by the anchor
    public float getWidth() { return getPreferredContentWidth() + marginLeft + marginRight; }
    public float getHeight() { return getPreferredContentHeight() + marginBottom + marginTop; }

    // These should only be used after setting the anchor position
    public float getContentLeft() { return x + marginLeft; }
    public float getContentRight() { return x + getPreferredContentWidth() + marginLeft + marginRight; }

    public float getContentBottom() { return y + marginBottom; }
    public float getContentTop() { return y + getPreferredContentHeight() + marginBottom + marginTop; }

    public float getContentCenterX() { return x + marginLeft + 0.5f * getPreferredContentWidth(); }
    public float getContentCenterY() { return y + marginBottom + 0.5f * getPreferredContentHeight(); }

    // --------------------------------------------------------------------------------

    public void moveTowardsTarget() {
        this.x = MathHelper.cardLerpSnap(this.x, this.targetX);
        this.y = MathHelper.cardLerpSnap(this.y, this.targetY);
    }

    public final void render(SpriteBatch sb) {
        moveTowardsTarget();
        renderWidget(sb);
    }

    protected abstract void renderWidget(SpriteBatch sb);
    public void update() {}

    // --------------------------------------------------------------------------------
    // Usually for hitboxes, but can be used to enable/disable computations required each frame - these should recurse
    // down the hierarchy when appropriate

    public void show() {}
    public void hide() {}

    // --------------------------------------------------------------------------------
    // Misc helpers

    public static float getMinimumPreferredWidgetWidth(List<AbstractWidget> widgets) {
        float minWidth = 1000000.0f;

        for (AbstractWidget widget : widgets) {
            float width = widget.getPreferredContentWidth();

            if (width < minWidth)
                minWidth = width;
        }

        return minWidth;
    }

    public static float getMaximumPreferredWidgetWidth(List<AbstractWidget> widgets) {
        float maxWidth = -1.0f;

        for (AbstractWidget widget : widgets) {
            float width = widget.getPreferredContentWidth();

            if (width > maxWidth)
                maxWidth = width;
        }

        return maxWidth;
    }
}
