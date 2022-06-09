package com.nopalsoft.flappy.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Drop {

    public static float WIDTH = 1f;
    public static float HEIGHT = 1f;
    public static float SPEED_Y = -1f;

    public static int STATE_NORMAL = 0;
    public static int STATE_COLLECTED = 1;
    public int state;

    public Vector2 position;
    public float stateTime;

    public Drop(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_NORMAL;
    }

    // Update para sincronizar con el body

    public void update(Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
    }
}
