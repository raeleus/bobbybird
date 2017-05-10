/*
 * The MIT License
 *
 * Copyright 2017 Raymond Buckley.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ray3k.bobby.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

/** Displays a {@link Drawable}, scaled various way within the widgets bounds. The preferred size is the min size of the drawable.
 * Only when using a {@link TextureRegionDrawable} will the actor's scale, rotation, and origin be used when drawing.
 * @author Nathan Sweet */
public class BouncingImage extends Widget {
	private Scaling scaling;
	private int align = Align.center;
	private float imageX, imageY, imageWidth, imageHeight;
	private Drawable drawable;
        private FloatAction floatAction;

	/** Creates an image with no region or patch, stretched, and aligned center. */
	public BouncingImage () {
		this((Drawable)null);
	}

	/** Creates an image stretched, and aligned center.
	 * @param patch May be null. */
	public BouncingImage (NinePatch patch) {
		this(new NinePatchDrawable(patch), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center.
	 * @param region May be null. */
	public BouncingImage (TextureRegion region) {
		this(new TextureRegionDrawable(region), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center. */
	public BouncingImage (Texture texture) {
		this(new TextureRegionDrawable(new TextureRegion(texture)));
	}

	/** Creates an image stretched, and aligned center. */
	public BouncingImage (Skin skin, String drawableName) {
		this(skin.getDrawable(drawableName), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center.
	 * @param drawable May be null. */
	public BouncingImage (Drawable drawable) {
		this(drawable, Scaling.stretch, Align.center);
	}

	/** Creates an image aligned center.
	 * @param drawable May be null. */
	public BouncingImage (Drawable drawable, Scaling scaling) {
		this(drawable, scaling, Align.center);
	}

	/** @param drawable May be null. */
	public BouncingImage (Drawable drawable, Scaling scaling, int align) {
		setDrawable(drawable);
		this.scaling = scaling;
		this.align = align;
		setSize(getPrefWidth(), getPrefHeight());
                addBounce();
	}
        
        public void addBounce() {
            SequenceAction sequence = new SequenceAction();
            
            floatAction = new FloatAction(-20, 20);
            floatAction.setInterpolation(Interpolation.sine);
            floatAction.setDuration(.5f);
            sequence.addAction(floatAction);

            final FloatAction newFloatAction = new FloatAction(20, -20);
            newFloatAction.setValue(20);
            newFloatAction.setInterpolation(Interpolation.sine);
            newFloatAction.setDuration(.5f);
            Action action = new Action() {
                @Override
                public boolean act(float delta) {
                    floatAction = newFloatAction;
                    return true;
                }
            };
            sequence.addAction(action);
            sequence.addAction(newFloatAction);
            
            action = new Action() {
                @Override
                public boolean act(float delta) {
                    addBounce();
                    return true;
                }
            };
            sequence.addAction(action);
            
            addAction(sequence);
        }

	public void layout () {
		if (drawable == null) return;

		float regionWidth = drawable.getMinWidth();
		float regionHeight = drawable.getMinHeight();
		float width = getWidth();
		float height = getHeight();

		Vector2 size = scaling.apply(regionWidth, regionHeight, width, height);
		imageWidth = size.x;
		imageHeight = size.y;

		if ((align & Align.left) != 0)
			imageX = 0;
		else if ((align & Align.right) != 0)
			imageX = (int)(width - imageWidth);
		else
			imageX = (int)(width / 2 - imageWidth / 2);

		if ((align & Align.top) != 0)
			imageY = (int)(height - imageHeight);
		else if ((align & Align.bottom) != 0)
			imageY = 0;
		else
			imageY = (int)(height / 2 - imageHeight / 2);
	}

	public void draw (Batch batch, float parentAlpha) {
		validate();

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX();
		float y = getY() + floatAction.getValue();
		float scaleX = getScaleX();
		float scaleY = getScaleY();

		if (drawable instanceof TransformDrawable) {
			float rotation = getRotation();
			if (scaleX != 1 || scaleY != 1 || rotation != 0) {
				((TransformDrawable)drawable).draw(batch, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY,
					imageWidth, imageHeight, scaleX, scaleY, rotation);
				return;
			}
		}
		if (drawable != null) drawable.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
	}

	public void setDrawable (Skin skin, String drawableName) {
		setDrawable(skin.getDrawable(drawableName));
	}

	/** @param drawable May be null. */
	public void setDrawable (Drawable drawable) {
		if (this.drawable == drawable) return;
		if (drawable != null) {
			if (getPrefWidth() != drawable.getMinWidth() || getPrefHeight() != drawable.getMinHeight()) invalidateHierarchy();
		} else
			invalidateHierarchy();
		this.drawable = drawable;
	}

	/** @return May be null. */
	public Drawable getDrawable () {
		return drawable;
	}

	public void setScaling (Scaling scaling) {
		if (scaling == null) throw new IllegalArgumentException("scaling cannot be null.");
		this.scaling = scaling;
		invalidate();
	}

	public void setAlign (int align) {
		this.align = align;
		invalidate();
	}

	public float getMinWidth () {
		return 0;
	}

	public float getMinHeight () {
		return 0;
	}

	public float getPrefWidth () {
		if (drawable != null) return drawable.getMinWidth();
		return 0;
	}

	public float getPrefHeight () {
		if (drawable != null) return drawable.getMinHeight();
		return 0;
	}

	public float getImageX () {
		return imageX;
	}

	public float getImageY () {
		return imageY;
	}

	public float getImageWidth () {
		return imageWidth;
	}

	public float getImageHeight () {
		return imageHeight;
	}
}

