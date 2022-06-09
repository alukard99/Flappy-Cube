package com.nopalsoft.flappy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.flappy.objetos.Counter;
import com.nopalsoft.flappy.objetos.Cube;
import com.nopalsoft.flappy.objetos.Drop;
import com.nopalsoft.flappy.screens.Screens;

public class WorldGame {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    static final int STATE_RUNNING = 0;
    static final int STATE_GAME_OVER = 1;
    public int state;
    private Sound dropsound;

    /**
     * Tiempo que pasa entre cada spawn de gota
     */
    final float TIME_TO_SPAWN_DROP = 1.5f;
    float timeToSpawnDrop;

    public World oWorldBox;
    public int score;

    /**
     * Objeto cubo
     */
    Cube oCube;

    /**
     * Array de gotas
     */
    Array<Drop> arrDrops;

    /**
     * Array con los bodys del juego
     */
    Array<Body> arrBodies;

    /**
     * Suelo
     */
    Counter oGround;

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -13.0f), true);
        oWorldBox.setContactListener(new Collisions());

        arrDrops = new Array<>();
        arrBodies = new Array<>();

        timeToSpawnDrop = 1.5f;
        this.dropsound = Gdx.audio.newSound(Gdx.files.internal("data/dropsound.mp3"));
        createCube();
        createLeftLimit();
        createRightLimit();
        createGround();
        state = STATE_RUNNING;
    }

    private void createCube() {
        oCube = new Cube(2.5f, 2.2f);

        BodyDef bd = new BodyDef();
        bd.position.x = oCube.position.x;
        bd.position.y = oCube.position.y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(.25f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 8;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(oCube);
        oBody.setBullet(true);

        shape.dispose();
    }

    private void createLeftLimit() {
        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 0;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0.10f, 5f, 0.10f, 1f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);
        shape.dispose();
    }

    private void createRightLimit() {

        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 0;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(4.75f, 5f, 4.75f, 1f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);
        shape.dispose();
    }

    private void createGround(){
        oGround = new Counter(0, 0);

        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 1f;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, .89f, WIDTH, .89f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);
        fixture.shape = shape;
        oBody.setFixedRotation(true);
        oBody.setUserData(oGround);
        oBody.setBullet(true);
        shape.dispose();
    }

    public void addDrop() {
        float x = MathUtils.random() * (2f) + 1.5f;
        float y = 10f;

        // Add the bottom drop
        addDrop(x, y);

    }

    private void addDrop(float x, float y) {

        Drop obj;
        obj = new Drop(x, y);


        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.DynamicBody;
        Body oBody = oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(.36f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 0;

        oBody.setGravityScale(0.3f);
        oBody.createFixture(fixture);
        oBody.setFixedRotation(true);
        oBody.setUserData(obj);
        arrDrops.add(obj);
        shape.dispose();
    }

    public void update(float delta, Boolean move ,int dir) {
        oWorldBox.step(delta, 8, 4);

        deleteObjects();

        timeToSpawnDrop += delta;

        if (timeToSpawnDrop >= TIME_TO_SPAWN_DROP) {
            timeToSpawnDrop -= TIME_TO_SPAWN_DROP;
            addDrop();
        }

        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Cube) {
                updateCube(body, delta, dir);
            } else if (body.getUserData() instanceof Drop) {
                updateDrops(body);
            } else if (body.getUserData() instanceof Counter) {
                updateCounter(body);
            }
        }

        if (oCube.state == Cube.STATE_DEAD)
            state = STATE_GAME_OVER;
    }

    private void updateCube(Body body, float delta, int dir) {

        oCube.update(delta, body);

        if (oCube.state == Cube.STATE_NORMAL) {
            if(dir==1) {
                body.setLinearVelocity(Cube.JUMP_SPEED*-1, 0); //Se multiplica por -1 para que sea negativo (Se mueve a la izq)
            }
            else if(dir==2){
                body.setLinearVelocity(Cube.JUMP_SPEED, 0);
            }
        }
    }

    private void updateDrops(Body body) {
        if (oCube.state == Cube.STATE_NORMAL) {
            Drop obj = (Drop) body.getUserData();

            obj.update(body);
            if (obj.position.y <= -5)
                obj.state = Drop.STATE_COLLECTED;

        } else
            body.setLinearVelocity(0, 0);

    }

    private void updateCounter(Body body) {
        if (oCube.state == Cube.STATE_NORMAL) {
            Counter obj = (Counter) body.getUserData();

            obj.update(body);
        }
    }

    private void deleteObjects() {
        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {
                if (body.getUserData() instanceof Drop) {
                    Drop obj = (Drop) body.getUserData();
                    if (obj.state == Drop.STATE_COLLECTED) {
                        arrDrops.removeValue(obj, false);
                        oWorldBox.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Counter) {
                    Counter obj = (Counter) body.getUserData();
                    if (obj.state == Counter.STATE_REMOVE) {
                        oWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    class Collisions implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if(a.getBody().getUserData() instanceof Cube){
                if (b.getBody().getUserData() instanceof Drop)
                    beginContactCube(a, b);
            }
            if(a.getBody().getUserData() instanceof Counter){
                if (b.getBody().getUserData() instanceof Drop){
                    beginContactDead(b);
                }
            }
            else if(b.getBody().getUserData() instanceof Counter){
                if(a.getBody().getUserData() instanceof Drop){
                    beginContactDead(a);
                }
            }
        }

        private void beginContactCube(Fixture cube, Fixture otraCosa) {
            Object somethingElse = otraCosa.getBody().getUserData();

            if (somethingElse instanceof Drop) {
                Drop obj = (Drop) somethingElse;
                if (obj.state == Drop.STATE_NORMAL) {
                    obj.state = Drop.STATE_COLLECTED;
                    score++;
                    dropsound.play();
                }
            } else {
                if (oCube.state == Cube.STATE_NORMAL) {
                    ;

                }
            }
        }

        private void beginContactDead(Fixture drop) {
            Object gota = drop.getBody().getUserData();

            if(gota instanceof Drop){
                Drop obj = (Drop) gota;
                if(obj.state == Drop.STATE_NORMAL){
                    obj.state = Drop.STATE_COLLECTED;
                }
            }

            state = STATE_GAME_OVER;
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }

    }

}
