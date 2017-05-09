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

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class ScrollerPane extends Table {
    private TiledDrawable tiledDrawable;
    private float xPosition;
    private float yPosition;
    private float xSpeed;
    private float ySpeed;
    private Table innerTable;
    private ScrollPane scrollPane;

    public ScrollerPane(TiledDrawable tiledDrawable, float xSpeed, float ySpeed) {
        this.tiledDrawable = tiledDrawable;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        xPosition = 0.0f;
        yPosition = 0.0f;
        
        Image image = new Image(tiledDrawable);
        
        innerTable = new Table();
        innerTable.add(image).width(5000);
        scrollPane = new ScrollPane(innerTable);
        scrollPane.setTouchable(Touchable.disabled);
        scrollPane.setSmoothScrolling(false);
        this.add(scrollPane).grow();
    }

    @Override
    public void layout() {
        super.layout();
//        innerTable.getCells().first().width(getWidth() + tiledDrawable.getMinWidth());
//        innerTable.getCells().first().height(getHeight() + tiledDrawable.getMinHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        
        xPosition += xSpeed * delta;
        xPosition %= tiledDrawable.getMinWidth();
        scrollPane.setScrollX(-xPosition);
        yPosition += ySpeed * delta;
        yPosition %= tiledDrawable.getMinHeight();
        scrollPane.setScrollY(-yPosition);
        System.out.println(scrollPane.getScrollX());
    }
}
