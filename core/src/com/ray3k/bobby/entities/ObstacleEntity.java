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

package com.ray3k.bobby.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ray3k.bobby.Entity;
import com.ray3k.bobby.states.GameState;

public class ObstacleEntity extends Entity {
    private final GameState gameState;
    private boolean createOnDeath;
    
    public ObstacleEntity (GameState gameState) {
        super(gameState.getManager(), gameState.getCore());
        this.gameState = gameState;
    }
    
    @Override
    public void create() {
        createOnDeath = true;
        setDepth(100);
        setMotion(500.0f, 180.0f);
        setCheckingCollisions(true);
    }

    @Override
    public void act(float delta) {
        if (createOnDeath && getX() < -getTextureRegion().getRegionWidth() - GameState.OBSTACLE_GAP) {
            dispose();
            gameState.createObstaclePair();
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void collision(Entity other) {
    }

    public boolean isCreateOnDeath() {
        return createOnDeath;
    }

    public void setCreateOnDeath(boolean createOnDeath) {
        this.createOnDeath = createOnDeath;
    }

}
