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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;

public class EntityManager {
    private final Array<Entity> entities;
    
    public EntityManager() {
        entities = new Array<Entity>();
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public Array<Entity> getEntities() {
        return new Array<Entity>(entities);
    }
    
    public void act(float delta) {
        Iterator<Entity> iter = entities.iterator();
        while (iter.hasNext()) {
            Entity entity = iter.next();
            if (!entity.isDestroyed()) {
                entity.addXspeed(entity.getGravityX() * delta);
                entity.addYspeed(entity.getGravityY() * delta);
                
                entity.addX(entity.getXspeed() * delta);
                entity.addY(entity.getYspeed() * delta);
                
                entity.act(delta);
            } else {
                iter.remove();
            }
        }
    }
    
    public void draw(SpriteBatch spriteBatch, float delta) {
        for (Entity entity : entities) {
            if (!entity.isDestroyed()) {
                if (entity.getTextureRegion() != null) {
                    spriteBatch.draw(entity.getTextureRegion(), entity.getX() + entity.getXspeed() * delta, entity.getY() + entity.getYspeed() * delta, entity.getOffsetX(), entity.getOffsetY(), entity.getTextureRegion().getRegionWidth(), entity.getTextureRegion().getRegionHeight(), entity.getScaleX(), entity.getScaleY(), entity.getRotation());
                }
                
                entity.draw(spriteBatch, delta);
            }
        }
    }
}
