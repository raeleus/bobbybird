package com.ray3k.bobby.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.bobby.Core;
import com.ray3k.bobby.State;

public class LoadingState implements State {
    private Stage stage;
    private Skin skin;
    private ProgressBar progressBar;
    private String nextState;
    private Table root;
    private boolean finishedLoading;
    
    public LoadingState(String nextState) {
        this.nextState = nextState;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }
    
    @Override
    public void start() {
        finishedLoading = false;
        
        stage = new Stage(new ScreenViewport());
        
        skin = createSkin();
        
        Image image= new Image(skin, "bg");
        image.setScaling(Scaling.stretch);
        image.setFillParent(true);
        stage.addActor(image);
        
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        progressBar = new ProgressBar(0, 1, .01f, false, skin);
        progressBar.setAnimateDuration(.25f);
        root.add(progressBar).growX().expandY().pad(20.0f);
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        stage.draw();
    }

    @Override
    public void act(float delta) {
        AssetManager assetManager = Core.core.getAssetManager();
        progressBar.setValue(assetManager.getProgress());
        stage.act(delta);
        if (!finishedLoading && assetManager.update()) {
            Action changeStateAction = new Action() {
                @Override
                public boolean act(float delta) {
                    finishedLoading = true;
                    packPixmaps();
                    Core.core.getStateManager().loadState(nextState);
                    return true;
                }
            };
            root.addAction(new SequenceAction(new DelayAction(1.0f), changeStateAction));
        }
    }

    @Override
    public void dispose() {
        
    }
    
    private Drawable createDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
    
    private Skin createSkin() {
        Skin returnValue = new Skin();
        
        returnValue.add("bg", createDrawable(20, 20, Color.DARK_GRAY), Drawable.class);
        returnValue.add("progress-bar-back", createDrawable(20, 20, Color.BLACK), Drawable.class);
        returnValue.add("progress-bar", createDrawable(1, 20, Color.BLUE), Drawable.class);
        
        ProgressBarStyle progressBarStyle = new ProgressBarStyle();
        progressBarStyle.background = returnValue.getDrawable("progress-bar-back");
        progressBarStyle.knobBefore = returnValue.getDrawable("progress-bar");
        
        returnValue.add("default-horizontal", progressBarStyle);
        
        return returnValue;
    }
    
    private void packPixmaps() {
        for (String name : Core.core.getAssetManager().getAssetNames()) {
            System.out.println(name);
        }
        
        for (String directory : Core.core.getImagePacks().keys()) {
            for (String name : Core.core.getImagePacks().get(directory)) {
                Core.core.getPixmapPacker().pack(Core.core.getAssetManager().get(directory + "/" + name, Pixmap.class));
            }
        }
    }

    @Override
    public void stop() {
        stage.dispose();
        skin.dispose();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
