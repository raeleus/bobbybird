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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    private final Vector2 position;
    private final Vector2 speed;
    private final Vector2 offset;
    private final Vector2 scale;
    private float rotation;
    private TextureRegion textureRegion;
    private boolean destroyed;
    private final EntityManager manager;
    private final Core core;

    public Entity(EntityManager manager, Core core) {
        position = new Vector2();
        speed = new Vector2();
        offset = new Vector2();
        scale = new Vector2();
        scale.x = 1.0f;
        scale.y = 1.0f;
        rotation = 0.0f;
        destroyed = false;
        this.manager = manager;
        manager.addEntity(this);
        this.core = core;
        
        create();
    }
    
    public abstract void create();
    
    public abstract void act(float delta);
    
    public abstract void draw(SpriteBatch spriteBatch, float delta);
    
    public abstract void destroy();
    
    public void dispose() {
        destroyed = true;
    }

    public Vector2 getPosition() {
        return position.cpy();
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public void setPosition(Vector2 position) {
        this.position.set(position);
    }
    
    public void setX(float x) {
        this.position.x = x;
    }
    
    public void setY(float y) {
        this.position.y = y;
    }
    
    public void addX(float x) {
        this.position.x += x;
    }
    
    public void addY(float y) {
        this.position.y += y;
    }

    public Vector2 getSpeed() {
        return speed.cpy();
    }
    
    public float getXspeed() {
        return speed.x;
    }
    
    public float getYspeed() {
        return speed.y;
    }
    
    public void setSpeed(Vector2 speed) {
        this.speed.set(speed);
    }
    
    public void setXspeed(float x) {
        this.speed.x = x;
    }
    
    public void setYspeed(float y) {
        this.speed.y = y;
    }
    
    public void addXspeed(float x) {
        this.speed.x += x;
    }
    
    public void addYspeed(float y) {
        this.speed.y += y;
    }

    public float getRotation() {
        return rotation;
    }

    public Vector2 getOffset() {
        return offset.cpy();
    }
    
    public float getOffsetX() {
        return offset.x;
    }
    
    public void setOffsetX(float x) {
        offset.x = x;
    }
    
    public float getOffsetY() {
        return offset.y;
    }
    
    public void setOffsetY(float y) {
        offset.y = y;
    }

    public Vector2 getScale() {
        return scale.cpy();
    }
    
    public float getScaleX() {
        return scale.x;
    }
    
    public void setScaleX(float scaleX) {
        scale.x = scaleX;
    }
    
    public float getScaleY() {
        return scale.y;
    }
    
    public void setScaleY(float scaleY) {
        scale.y = scaleY;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
    
    public void addRotation(float rotation) {
        this.rotation += rotation;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public EntityManager getManager() {
        return manager;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public Core getCore() {
        return core;
    }
}
