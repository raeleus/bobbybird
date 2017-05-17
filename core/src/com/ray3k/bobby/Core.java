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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.ray3k.bobby.states.CreditsState;
import com.ray3k.bobby.states.GameState;
import com.ray3k.bobby.states.LoadingState;
import com.ray3k.bobby.states.MenuState;

public class Core extends ApplicationAdapter {
    public final static String VERSION = "1";
    public final static String DATA_PATH = "bobby_data";
    private final static long MS_PER_UPDATE = 10;
    private AssetManager assetManager;
    private StateManager stateManager;
    private SpriteBatch spriteBatch;
    private PixmapPacker pixmapPacker;
    private ObjectMap<String, Array<String>> imagePacks;
    private long previous;
    private long lag;
    private TextureAtlas atlas;
    
    @Override
    public void create() {
        initManagers();
        
        createLocalFiles();
        
        loadAssets();
        
        previous = TimeUtils.millis();
        lag = 0;
        
        stateManager.loadState("loading");
    }
    
    public void initManagers() {
        assetManager = new AssetManager(new LocalFileHandleResolver(), true);
        
        imagePacks = new ObjectMap<String, Array<String>>();
        for (String name : new String[] {"characters", "obstacles", "grounds", "clouds", "bushes", "buildings"}) {
            imagePacks.put(DATA_PATH + "/" + name, new Array<String>());
        }
        
        stateManager = new StateManager(this);
        stateManager.addState("loading", new LoadingState("menu", this));
        stateManager.addState("menu", new MenuState(this));
        stateManager.addState("game", new GameState(this));
        stateManager.addState("credits", new CreditsState(this));
        
        spriteBatch = new SpriteBatch();
        
        pixmapPacker = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 5, true, new PixmapPacker.GuillotineStrategy());
    }
    
    @Override
    public void render() {
        long current = TimeUtils.millis();
        long elapsed = current - previous;
        previous = current;
        lag += elapsed;
        
        while (lag >= MS_PER_UPDATE) {
            stateManager.act(MS_PER_UPDATE / 1000.0f);
            lag -= MS_PER_UPDATE;
        }
        
        stateManager.draw(spriteBatch, lag / MS_PER_UPDATE);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        stateManager.dispose();
        pixmapPacker.dispose();
        if (atlas != null) {
            atlas.dispose();
        }
    }
    
    public void createLocalFiles() {
        for (String directory : imagePacks.keys()) {
            Gdx.files.local(directory).mkdirs();
        }
        
        if (!Gdx.files.local(DATA_PATH + "/skin/skin.json").exists()) {
            Gdx.files.internal("skin/font-export.fnt").copyTo(Gdx.files.local(DATA_PATH + "/skin/font-export.fnt"));
            Gdx.files.internal("skin/font-game-over-export.fnt").copyTo(Gdx.files.local(DATA_PATH + "/skin/font-game-over-export.fnt"));
            Gdx.files.internal("skin/font-get-ready-export.fnt").copyTo(Gdx.files.local(DATA_PATH + "/skin/font-get-ready-export.fnt"));
            Gdx.files.internal("skin/font-score-export.fnt").copyTo(Gdx.files.local(DATA_PATH + "/skin/font-score-export.fnt"));
            Gdx.files.internal("skin/font-summary-export.fnt").copyTo(Gdx.files.local(DATA_PATH + "/skin/font-summary-export.fnt"));
            Gdx.files.internal("skin/font-title-export.fnt").copyTo(Gdx.files.local(DATA_PATH + "/skin/font-title-export.fnt"));
            Gdx.files.internal("skin/skin.atlas").copyTo(Gdx.files.local(DATA_PATH + "/skin/skin.atlas"));
            Gdx.files.internal("skin/skin.json").copyTo(Gdx.files.local(DATA_PATH + "/skin/skin.json"));
            Gdx.files.internal("skin/skin.png").copyTo(Gdx.files.local(DATA_PATH + "/skin/skin.png"));
        }
        
        if (!Gdx.files.local(DATA_PATH + "/data.json").exists()) {
            Gdx.files.internal("data.json").copyTo(Gdx.files.local(DATA_PATH));
        }
        
        Gdx.files.local(DATA_PATH + "/sfx/").mkdirs();
        
        if (!Gdx.files.local(DATA_PATH + "/sfx/coin.wav").exists()) {
            Gdx.files.internal("sfx/coin.wav").copyTo(Gdx.files.local(DATA_PATH + "/sfx/"));
        }
        
        if (!Gdx.files.local(DATA_PATH + "/sfx/hit.wav").exists()) {
            Gdx.files.internal("sfx/hit.wav").copyTo(Gdx.files.local(DATA_PATH + "/sfx/"));
        }
        
        if (!Gdx.files.local(DATA_PATH + "/sfx/jump.wav").exists()) {
            Gdx.files.internal("sfx/jump.wav").copyTo(Gdx.files.local(DATA_PATH + "/sfx/"));
        }
    }
    
    public void loadAssets() {
        assetManager.clear();
        
        assetManager.load(DATA_PATH + "/skin/skin.json", Skin.class);
        
        for (String directory : imagePacks.keys()) {
            FileHandle folder = Gdx.files.local(directory);
            for (FileHandle file : folder.list()) {
                assetManager.load(file.path(), Pixmap.class);
                imagePacks.get(directory).add(file.nameWithoutExtension());
            }
        }
        
        assetManager.load(DATA_PATH + "/sfx/coin.wav", Sound.class);
        assetManager.load(DATA_PATH + "/sfx/hit.wav", Sound.class);
        assetManager.load(DATA_PATH + "/sfx/jump.wav", Sound.class);
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resize(int width, int height) {
        stateManager.resize(width, height);
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public PixmapPacker getPixmapPacker() {
        return pixmapPacker;
    }

    public ObjectMap<String, Array<String>> getImagePacks() {
        return imagePacks;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void setAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }
}