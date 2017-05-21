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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.bobby.Core;
import com.ray3k.bobby.Entity;
import com.ray3k.bobby.InputManager;
import com.ray3k.bobby.states.GameState;

public class BirdEntity extends Entity implements InputManager.FlapListener {
    private Sound jump;
    private Sound hit;
    private Sound coin;
    private boolean flying;
    private GameState gameState;
    
    public BirdEntity(GameState gameState) {
        super(gameState.getManager(), gameState.getCore());
        this.gameState = gameState;
        gameState.getInputManager().addFlapListener(this);
        flying = false;
        setCheckingCollisions(true);
    }

    @Override
    public void create() {
        setTextureRegion(getCore().getAtlas().findRegion(((GameState) getCore().getStateManager().getState("game")).getSelectedCharacter()));
        setX(50);
        setY(Gdx.graphics.getHeight() / 2.0f - getTextureRegion().getRegionHeight() / 2.0f);
        setOffsetX(getTextureRegion().getRegionWidth() / 2.0f);
        setOffsetY(getTextureRegion().getRegionHeight() / 2.0f);
        getCollisionBox().setSize(getTextureRegion().getRegionWidth() / 2.0f, getTextureRegion().getRegionHeight() / 2.0f);
        
        jump = getCore().getAssetManager().get(Core.DATA_PATH + "/sfx/jump.wav", Sound.class);
        hit = getCore().getAssetManager().get(Core.DATA_PATH + "/sfx/hit.wav", Sound.class);
        coin = getCore().getAssetManager().get(Core.DATA_PATH + "/sfx/coin.wav", Sound.class);
    }

    @Override
    public void act(float delta) {
        if (flying) {
            float percent = (getYspeed() + 1000) / 800;
            percent = MathUtils.clamp(percent, 0.0f, 1.0f);
            setRotation(120.0f * percent - 90.0f);
        } else {
            setRotation(0.0f);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        
    }

    @Override
    public void destroy() {
        hit.play();
        gameState.getInputManager().removeFlapListener(this);
    }

    @Override
    public void collision(Entity other) {
        if (other instanceof GroundEntity || other instanceof ObstacleEntity) {
            dispose();
        }
    }

    @Override
    public void flapPressed() {
        setMotion(525.0f, 90.0f);
        setGravity(1500.0f, 270.0f);
        jump.play();
        flying = true;
    }
}
