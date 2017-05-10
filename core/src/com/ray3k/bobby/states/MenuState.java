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
package com.ray3k.bobby.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.bobby.Core;
import static com.ray3k.bobby.Core.DATA_PATH;
import com.ray3k.bobby.State;
import com.ray3k.bobby.actors.BouncingImage;
import com.ray3k.bobby.actors.ScrollerPane;

public class MenuState extends State {
    private Stage stage;
    private Skin skin;
    private Table root;

    public MenuState(Core core) {
        super(core);
    }
    
    @Override
    public void start() {
        skin = getCore().getAssetManager().get(Core.DATA_PATH + "/skin/skin.json", Skin.class);
        stage = new Stage(new ScreenViewport());
        
        Gdx.input.setInputProcessor(stage);
        
        Image bg = new Image(skin, "sky");
        bg.setFillParent(true);
        stage.addActor(bg);
        
        createBG();
        
        createMenu();
    }
    
    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        stage.draw();
    }

    @Override
    public void act(float delta) {
        stage.act(delta);
    }

    @Override
    public void dispose() {
        
    }

    @Override
    public void stop() {
        stage.dispose();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    private void createBG() {
        Table tableBG = new Table();
        tableBG.setFillParent(true);
        stage.addActor(tableBG);
        
        Stack stack = new Stack();
        float height = 0;
        
        Table table = new Table();
        stack.add(table);
        
        Image image = new Image(getCloud());
        table.add(image).growX().expandY().top();
        height += image.getHeight() / 2.0f;

        table = new Table();
        stack.add(table);
        
        image = new Image(getBuilding());
        table.add(image).growX().expandY().bottom();
        height += image.getHeight();

        table = new Table();
        stack.add(table);
        
        image = new Image(getBush());
        table.add(image).growX().expandY().bottom();
        
        tableBG.add(stack).growX().expandY().bottom().height(height);
        
        tableBG.row();
        ScrollerPane scrollerPane = new ScrollerPane(getGround(), -300.0f, 0.0f);
        tableBG.add(scrollerPane);
    }
    
    private TiledDrawable getCloud() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/clouds");
        
        TiledDrawable tiledDrawable = new TiledDrawable(getCore().getAtlas().findRegion(names.random()));
        return tiledDrawable;
    }
    
    private TiledDrawable getBuilding() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/buildings");
        
        TiledDrawable tiledDrawable = new TiledDrawable(getCore().getAtlas().findRegion(names.random()));
        return tiledDrawable;
    }
    
    private TiledDrawable getGround() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/grounds");
        
        TiledDrawable tiledDrawable = new TiledDrawable(getCore().getAtlas().findRegion(names.random()));
        return tiledDrawable;
    }
    
    private TiledDrawable getBush() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/bushes");
        
        TiledDrawable tiledDrawable = new TiledDrawable(getCore().getAtlas().findRegion(names.random()));
        return tiledDrawable;
    }
    
    private Drawable getCharacter() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/characters");
        
        Drawable drawable = new TextureRegionDrawable(getCore().getAtlas().findRegion(names.random()));
        return drawable;
    }
    
    private void createMenu() {
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        FileHandle fileHandle = Gdx.files.local(Core.DATA_PATH + "/data.json");
        JsonReader reader = new JsonReader();
        JsonValue val = reader.parse(fileHandle);
        
        Label title = new Label(val.getString("title"), skin, "title");
        root.add(title).padTop(50.0f).padBottom(75.0f);
        
        root.row();
        BouncingImage image = new BouncingImage(getCharacter());
        root.add(image);
        
        root.row();
        ImageButton imageButton = new  ImageButton(skin, "play");
        root.add(imageButton).padTop(75.0f).expandY().top();
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                showCharacterDialog();
            }
        });
    }
    
    private void showCharacterDialog() {
        Dialog dialog = new Dialog("", skin);
        
        Label label = new Label("Choose a character...", skin);
        dialog.getContentTable().add(label);
        
        dialog.getContentTable().row();
        Table table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        dialog.getContentTable().add(scrollPane).grow();
        
        ButtonGroup buttons = new ButtonGroup();
        for (String name : getCore().getImagePacks().get(DATA_PATH + "/characters")) {
            Drawable drawable = new TextureRegionDrawable(getCore().getAtlas().findRegion(name));
            Image image = new Image(drawable);
            ImageTextButton imageTextButton = new ImageTextButton(name, skin, "list");
            imageTextButton.getImageCell().setActor(image);
            imageTextButton.getLabelCell().left().expandX();
            table.add(imageTextButton).growX();
            buttons.add(imageTextButton);
            
            table.row();
        }
        
        dialog.getContentTable().row();
        TextButton textButton = new TextButton("OK", skin);
        dialog.getContentTable().add(textButton);
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                Action changeStateAction = new Action() {
                    @Override
                    public boolean act(float delta) {
                        getCore().getStateManager().loadState("game");
                        return true;
                    }
                };
                root.addAction(new SequenceAction(new DelayAction(.5f), changeStateAction));
            }
        });
        
        dialog.show(stage);
        dialog.setSize(400.0f, 400.0f);
        dialog.setPosition(stage.getWidth() / 2.0f, stage.getHeight() / 2.0f, Align.center);
        stage.setScrollFocus(scrollPane);
    }
}
