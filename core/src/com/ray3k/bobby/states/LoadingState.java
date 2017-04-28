package com.ray3k.bobby.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.bobby.State;

public class LoadingState implements State {
    private Stage stage;
    private Skin skin;
    private PixmapPacker pixmapPacker;
    
    
    @Override
    public void create() {
        pixmapPacker = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 5, true, new PixmapPacker.GuillotineStrategy());
        
        Pixmap image = new Pixmap(file);
        image.d
        pixmapPacker.pack(image);
        stage = new Stage(new ScreenViewport());
        
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        
    }

    @Override
    public void act(float delta) {
        
    }

    @Override
    public void dispose() {
        
    }
}
