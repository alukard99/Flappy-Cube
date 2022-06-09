package com.nopalsoft.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Assets {

    public static BitmapFont font;
    private static final GlyphLayout glyphLayout = new GlyphLayout();

    public static Animation<AtlasRegion> cube;

    public static TextureRegion background;
    public static TextureRegion gameOver;
    public static TextureRegion getReady;
    public static TextureRegion drop;

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlas"));

        background = atlas.findRegion("background");
        gameOver = atlas.findRegion("over");
        getReady = atlas.findRegion("ready");
        drop = atlas.findRegion("drop");
        cube = new Animation<>(.3f,
                atlas.findRegion("cube"),
                atlas.findRegion("cube1"),
                atlas.findRegion("cube2"));

        // Use default libGDX font
        font = new BitmapFont();
        font.getData().scale(5f);
    }

    public static float getTextWidth(String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }
}
