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

package com.ray3k.bobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import static com.ray3k.bobby.Core.DATA_PATH;
import com.ray3k.bobby.entities.CoinEntity;
import com.ray3k.bobby.entities.ObstacleEntity;
import com.ray3k.bobby.states.GameState;

public class ObstacleManager extends Entity {
    private float timer;
    private TextureRegion obstacleTexture;
    private TextureRegion obstacleFlipped;
    private GameState gameState;
    
    public ObstacleManager(GameState gameState) {
        super(gameState.getManager(), gameState.getCore());
        this.gameState = gameState;
    }
    
    public void spawnObstacle() {
        Array<ObstacleEntity> obstacles = new Array<ObstacleEntity>();
        ObstacleEntity obstacle = new ObstacleEntity(gameState);
        float yPos = gameState.getGroundTexture().getRegionHeight() - obstacleTexture.getRegionHeight() + MathUtils.random(GameState.OBSTACLE_MIN_HEIGHT, GameState.OBSTACLE_MAX_HEIGHT);
        obstacle.setPosition(Gdx.graphics.getWidth(), yPos);
        obstacle.setTextureRegion(obstacleTexture);
        obstacle.getCollisionBox().setSize(obstacle.getTextureRegion().getRegionWidth(), obstacle.getTextureRegion().getRegionHeight());
        obstacles.add(obstacle);
        
        CoinEntity coin = new CoinEntity(gameState);
        coin.getCollisionBox().setSize(obstacleTexture.getRegionWidth(), GameState.GAP_HEIGHT);
        coin.setPosition(Gdx.graphics.getWidth(), yPos + obstacleTexture.getRegionHeight());
        
        obstacle = new ObstacleEntity(gameState);
        obstacle.setPosition(Gdx.graphics.getWidth(), yPos + GameState.GAP_HEIGHT + obstacleTexture.getRegionHeight());
        obstacle.setTextureRegion(obstacleFlipped);
        obstacle.getCollisionBox().setSize(obstacle.getTextureRegion().getRegionWidth(), obstacle.getTextureRegion().getRegionHeight());
        obstacles.add(obstacle);
    }

    @Override
    public void create() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/obstacles");
        obstacleTexture = getCore().getAtlas().findRegion(names.random());
        obstacleFlipped = new TextureRegion(obstacleTexture);
        obstacleFlipped.flip(false, true);
        timer = (obstacleTexture.getRegionWidth() + GameState.OBSTACLE_GAP) / GameState.SCROLL_SPEED;
    }

    @Override
    public void act(float delta) {
        timer -= delta;
        if (timer < 0) {
            spawnObstacle();
            timer = (obstacleTexture.getRegionWidth() + GameState.OBSTACLE_GAP) / GameState.SCROLL_SPEED;
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
}
