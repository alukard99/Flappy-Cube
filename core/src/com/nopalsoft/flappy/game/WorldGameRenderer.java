package com.nopalsoft.flappy.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.flappy.Assets;
import com.nopalsoft.flappy.objetos.Cube;
import com.nopalsoft.flappy.objetos.Drop;
import com.nopalsoft.flappy.screens.Screens;

public class WorldGameRenderer {

    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch spriteBatch;
    WorldGame oWorld;
    OrthographicCamera oCam;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batcher, WorldGame oWorld) {

        this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.spriteBatch = batcher;
        this.oWorld = oWorld;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render(float delta) {

        oCam.update();
        spriteBatch.setProjectionMatrix(oCam.combined);

        spriteBatch.begin();
        spriteBatch.disableBlending();
        drawBackground(delta);
        spriteBatch.enableBlending();
        drawDrop(delta);
        drawCube(delta);

        spriteBatch.end();

        //Descomentar para activar modo debug
        renderBox.render(oWorld.oWorldBox, oCam.combined);
    }

    private void drawBackground(float delta) {
        spriteBatch.draw(Assets.background, 0, 0, WIDTH, HEIGHT);
    }

    private void drawDrop(float delta) {
        for (Drop obj : oWorld.arrDrops) {
            spriteBatch.draw(Assets.drop, obj.position.x - .5f,
                    obj.position.y-.5f, 0.95f, 1.5f);
        }
    }

    private void drawCube(float delta) {
        Cube obj = oWorld.oCube;
        TextureRegion keyFrame;

        if (obj.state == Cube.STATE_NORMAL) {
            keyFrame = Assets.cube.getKeyFrame(obj.stateTime, true);
        } else {
            keyFrame = Assets.cube.getKeyFrame(obj.stateTime, false);
        }
        spriteBatch.draw(keyFrame, obj.position.x - .3f, obj.position.y - .25f, .6f, .5f);
    }

}
