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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ScrollingTiledDrawable extends TextureRegionDrawable {
    private final Color color = new Color(1, 1, 1, 1);
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;

    public ScrollingTiledDrawable() {
        super();
    }

    public ScrollingTiledDrawable(TextureRegion region) {
        super(region);
    }

    public ScrollingTiledDrawable(TextureRegionDrawable drawable) {
        super(drawable);
    }

    public void draw(Batch batch, float x, float y, float width, float height) {
        float batchColor = batch.getPackedColor();
        batch.setColor(batch.getColor().mul(color));
        
        TextureRegion region = getRegion();
        Texture texture = region.getTexture();
        
        float regionWidth = region.getRegionWidth();
        float regionHeight = region.getRegionHeight();
        float partialWidth = regionWidth - offsetX % regionWidth;
        float u = region.getU() - (offsetX % regionWidth) / texture.getWidth();
        float u2 = region.getU2();
        float v = region.getV();
        float v2 = region.getV2();
        
        batch.draw(texture, x, y, partialWidth, regionHeight, u, v2, u2, v);
        
        
        
        
//        int fullX = (int) (width / regionWidth);
//        float fullY = (int) (height / regionHeight);
//        float remainingX = width - regionWidth * fullX;
//        float remainingY = height - regionHeight * fullY;
//        float startX = x, startY = y;
//        float endX = x + width - remainingX;
//        float endY = y + height - remainingY;
//
//        
//        
//        
//        float u = region.getU();
//        float v2 = region.getV2();
//        if (remainingX > 0) {
//            // left edge.
//            float u2 = u + remainingX / texture.getWidth();
//            float v = region.getV();
//            y = startY;
//            for (int ii = 0; ii < fullY; ii++) {
//                batch.draw(texture, x, y, remainingX, regionHeight, u, v2, u2, v);
//                y += regionHeight;
//            }
//            // Upper right corner.
//            if (remainingY > 0) {
//                v = v2 - remainingY / texture.getHeight();
//                batch.draw(texture, x, y, remainingX, remainingY, u, v2, u2, v);
//            }
//        }
//        if (remainingY > 0) {
//            // Top edge.
//            float u2 = region.getU2();
//            float v = v2 - remainingY / texture.getHeight();
//            x = startX;
//            for (int i = 0; i < fullX; i++) {
//                batch.draw(texture, x, y, regionWidth, remainingY, u, v2, u2, v);
//                x += regionWidth;
//            }
//        }
//        
//        //body
//        for (int i = 0; i < fullX; i++) {
//            y = startY;
//            for (int ii = 0; ii < fullY; ii++) {
//                batch.draw(region, x, y, regionWidth, regionHeight);
//                y += regionHeight;
//            }
//            x += regionWidth;
//        }
//        
//        if (remainingX > 0) {
//            // Right edge.
//            float u2 = u + remainingX / texture.getWidth();
//            float v = region.getV();
//            y = startY;
//            for (int ii = 0; ii < fullY; ii++) {
//                batch.draw(texture, x, y, remainingX, regionHeight, u, v2, u2, v);
//                y += regionHeight;
//            }
//            // Upper right corner.
//            if (remainingY > 0) {
//                v = v2 - remainingY / texture.getHeight();
//                batch.draw(texture, x, y, remainingX, remainingY, u, v2, u2, v);
//            }
//        }
//        if (remainingY > 0) {
//            // Top edge.
//            float u2 = region.getU2();
//            float v = v2 - remainingY / texture.getHeight();
//            x = startX;
//            for (int i = 0; i < fullX; i++) {
//                batch.draw(texture, x, y, regionWidth, remainingY, u, v2, u2, v);
//                x += regionWidth;
//            }
//        }

        batch.setColor(batchColor);
    }

    public void draw(Batch batch, float x, float y, float originX, float originY,
            float width, float height, float scaleX,
            float scaleY, float rotation) {
        throw new UnsupportedOperationException();
    }

    public Color getColor() {
        return color;
    }

    public ScrollingTiledDrawable tint(Color tint) {
        ScrollingTiledDrawable drawable = new ScrollingTiledDrawable(this);
        drawable.color.set(tint);
        drawable.setLeftWidth(getLeftWidth());
        drawable.setRightWidth(getRightWidth());
        drawable.setTopHeight(getTopHeight());
        drawable.setBottomHeight(getBottomHeight());
        return drawable;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
