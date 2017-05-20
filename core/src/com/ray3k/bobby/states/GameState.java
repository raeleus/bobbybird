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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.bobby.Core;
import static com.ray3k.bobby.Core.DATA_PATH;
import com.ray3k.bobby.EntityManager;
import com.ray3k.bobby.InputManager;
import com.ray3k.bobby.State;
import com.ray3k.bobby.entities.BirdEntity;
import com.ray3k.bobby.entities.GroundEntity;
import com.ray3k.bobby.entities.ObstacleEntity;

public class GameState extends State {
    public static final float OBSTACLE_GAP = 400.0f;
    private String selectedCharacter;
    private TextureRegion groundTexture;
    private TextureRegion obstacleTexture;
    private TextureRegion bushTexture;
    private TextureRegion buildingTexture;
    private TextureRegion cloudTexture;
    
    private Stage stage;
    private Skin skin;
    private Table table;
    private int score;
    private EntityManager manager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private InputManager inputManager;
    
    public GameState(Core core) {
        super(core);
    }
    
    @Override
    public void start() {
        score = 0;
        
        manager = new EntityManager();
        
        inputManager = new InputManager();
        
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        
        skin = getCore().getAssetManager().get(Core.DATA_PATH + "/skin/skin.json", Skin.class);
        stage = new Stage(new ScreenViewport());
        
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputManager);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        showTutorial();
        
        BirdEntity bird = new BirdEntity(this);
        
        groundTexture = getGround();
        cloudTexture = getCloud();
        bushTexture = getBush();
        buildingTexture = getBuilding();
        obstacleTexture = getObstacle();
        
        for (int x = 0; x < Gdx.graphics.getWidth() + groundTexture.getRegionWidth(); x += groundTexture.getRegionWidth()) {
            GroundEntity ground = new GroundEntity(this);
            ground.setTextureRegion(groundTexture);
            ground.setPosition(x, 0.0f);
        }
        
        for (int i = 0; i < Gdx.graphics.getWidth(); i += obstacleTexture.getRegionWidth() + OBSTACLE_GAP) {
            Array<ObstacleEntity> obstacles = createObstaclePair();
            obstacles.get(0).setX(Gdx.graphics.getWidth() + i);
            obstacles.get(1).setX(Gdx.graphics.getWidth() + i);
        }
    }
    
    public void createGroundEntity() {
        GroundEntity ground = new GroundEntity(this);
        ground.setTextureRegion(groundTexture);
        ground.getCollisionBox().setSize(ground.getTextureRegion().getRegionWidth(), ground.getTextureRegion().getRegionHeight());
        ground.setCheckingCollisions(true); 
        ground.setPosition(Gdx.graphics.getWidth() + groundTexture.getRegionWidth() - Gdx.graphics.getWidth() % groundTexture.getRegionWidth(), 0.0f);
    }
    
    public Array<ObstacleEntity> createObstaclePair() {
        Array<ObstacleEntity> obstacles = new Array<ObstacleEntity>();
        ObstacleEntity obstacle = new ObstacleEntity(this);
        obstacle.setPosition(Gdx.graphics.getWidth(), 0.0f);
        obstacle.setTextureRegion(obstacleTexture);
        obstacle.getCollisionBox().setSize(obstacle.getTextureRegion().getRegionWidth(), obstacle.getTextureRegion().getRegionHeight());
        obstacles.add(obstacle);
        
        obstacle = new ObstacleEntity(this);
        obstacle.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - obstacleTexture.getRegionHeight());
        obstacle.setTextureRegion(obstacleTexture);
        obstacle.getCollisionBox().setSize(obstacle.getTextureRegion().getRegionWidth(), obstacle.getTextureRegion().getRegionHeight());
        obstacle.setCreateOnDeath(false);
        obstacles.add(obstacle);
        return obstacles;
    }
    
    private TextureRegion getCloud() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/clouds");
        return getCore().getAtlas().findRegion(names.random());
    }
    
    private TextureRegion getBuilding() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/buildings");
        return getCore().getAtlas().findRegion(names.random());
    }
    
    private TextureRegion getGround() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/grounds");
        return getCore().getAtlas().findRegion(names.random());
    }
    
    private TextureRegion getObstacle() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/obstacles");
        return getCore().getAtlas().findRegion(names.random());
    }
    
    private TextureRegion getBush() {
        Array<String> names = getCore().getImagePacks().get(DATA_PATH + "/bushes");
        return getCore().getAtlas().findRegion(names.random());
    }
    
    @Override
    public void draw(SpriteBatch spriteBatch, float delta) {
        Gdx.gl.glClearColor(0, 113/255.0f, 188/255.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        manager.draw(spriteBatch, delta);
        spriteBatch.end();
        
        stage.draw();
    }

    @Override
    public void act(float delta) {
        manager.act(delta);
        
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
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        stage.getViewport().update(width, height, true);
    }

    public String getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(String selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }
    
    private void showTutorial() {
        table.clear();
        
        final Table subTable = new Table();
        table.add(subTable);
        
        Label label = new Label("Get Ready!", skin, "get-ready");
        subTable.add(label).colspan(3).padBottom(50.0f);
        
        subTable.row();
        subTable.defaults().padTop(50.0f);
        label = new Label(" PRESS ", skin, "flag");
        subTable.add(label);
        
        Image image = new Image(skin, "spacebar");
        image.setScaling(Scaling.none);
        subTable.add(image);
        
        label = new Label(" PRESS ", skin, "flag-reversed");
        subTable.add(label);
        
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.SPACE) {
                    stage.removeListener(this);
                    
                    SequenceAction seq = new SequenceAction();
                    AlphaAction alphaAction = new AlphaAction();
                    alphaAction.setAlpha(0.0f);
                    alphaAction.setDuration(.5f);
                    seq.addAction(alphaAction);
                    
                    Action action = new Action() {
                        @Override
                        public boolean act(float delta) {
                            showScore();
                            return true;
                        }
                    };
                    seq.addAction(action);
                    subTable.addAction(seq);
                }
                return true;
            }
            
        });
    }
    
    private void showScore() {
        table.clear();
        
        Label label = new Label(Integer.toString(score), skin, "score");
        label.setColor(1.0f, 1.0f, 1.0f, 0.0f);
        table.add(label).padTop(30.0f).expandY().top();
        
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1.0f);
        alphaAction.setDuration(.5f);
        label.addAction(alphaAction);
    }

    public EntityManager getManager() {
        return manager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
