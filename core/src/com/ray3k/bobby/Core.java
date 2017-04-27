package com.ray3k.bobby;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Core extends ApplicationAdapter {
    Viewport viewport;
    OrthographicCamera camera;

    @Override
    public void create() {
        viewport = new ScreenViewport();
        camera = new OrthographicCamera();
        viewport.setCamera(camera);
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
