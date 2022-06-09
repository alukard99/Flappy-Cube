package com.nopalsoft.flappy.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.flappy.MainFlappyBird;

public abstract class Screens extends InputAdapter implements Screen {
    public static final float SCREEN_WIDTH = 480;
    public static final float SCREEN_HEIGHT = 800;

    public static final float WORLD_WIDTH = 4.8f;
    public static final float WORLD_HEIGHT = 8;

    public MainFlappyBird game;

    public OrthographicCamera oCam;
    public SpriteBatch spriteBatch;
    public Stage stage;

    public Screens(MainFlappyBird game) {
        this.game = game;

        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);

        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

        update(delta);

        stage.act(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);

        stage.draw();
    }

    public abstract void draw(float delta);

    public abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }

}
