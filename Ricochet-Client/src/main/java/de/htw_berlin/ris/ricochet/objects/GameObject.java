package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.Door;
import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glRectf;

public class GameObject {
    private BodyDef bodyDef;
    public Body body;
    private FixtureDef bodyFixture;
    public Vec2 position;
    private float width, height;
    public boolean contact;
    public java.awt.Color objectColor;
    protected GameObject colObj;
    //TODO make unitConversion a cont in gameworld
    protected static Vec2 unitConversion;

    public GameObject(Vec2 pos, float width, float height, BodyType bodyType) {
        unitConversion = new Vec2 (   GameWorld.Instance.WINDOW_DIMENSIONS[0]/30,   GameWorld.Instance.WINDOW_DIMENSIONS[1]/30);
        bodyDef = new BodyDef();
        bodyDef.position.set(pos.x *unitConversion.x, pos.y*unitConversion.y);
        bodyDef.type = bodyType;
        bodyDef.userData = this;
        PolygonShape boxShape = new PolygonShape();
        this.width = width;
        this.height = height;
        boxShape.setAsBox(width, height);
        body = GameWorld.Instance.getPhysicsWorld().createBody(bodyDef);
        bodyFixture = new FixtureDef();
        bodyFixture.density = 0.1f;
        bodyFixture.shape = boxShape;
        body.createFixture(bodyFixture);
        if (this instanceof Player){
            body.setLinearDamping(0.75f);
        }
        contact = false;
        objectColor = new java.awt.Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        Init();
    }

    public GameObject(Vec2 pos, float width, float height, BodyType bodyType, float density, float restitution) {
        unitConversion = new Vec2 (   GameWorld.Instance.WINDOW_DIMENSIONS[0]/30,   GameWorld.Instance.WINDOW_DIMENSIONS[1]/30);
        bodyDef = new BodyDef();
        bodyDef.position.set(pos.x*unitConversion.x, pos.y*unitConversion.y);
        bodyDef.type = bodyType;
        bodyDef.userData = this;
        PolygonShape boxShape = new PolygonShape();
        this.width = width;
        this.height = height;
        boxShape.setAsBox(width, height);
        body = GameWorld.Instance.getPhysicsWorld().createBody(bodyDef);
        bodyFixture = new FixtureDef();
        bodyFixture.density = density;
        bodyFixture.shape = boxShape;
        bodyFixture.restitution = restitution;
        body.createFixture(bodyFixture);
        contact = false;
        objectColor = new java.awt.Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        Init();
    }

    public void Init() {

        GameWorld.Instance.getCurrentScene().getSceneObjects().add(this);
    }

    // Game Logic goes here
    public void Update() {

    }

    public void StartContact(GameObject gameObject) {

        contact = true;
        this.colObj = gameObject;
    }

    public void EndContact(GameObject gameObject) {

        contact = false;
        this.colObj = null;

    }

    // Code for drawing the objects
    public void Render() {

        float[] rgbVal = new float[4];
        if (contact) {

            Color.red.getRGBColorComponents(rgbVal);
        } else if (this instanceof Door) {

        } else {
            objectColor.getRGBColorComponents(rgbVal);
        }

        glColor3f(rgbVal[0], rgbVal[1], rgbVal[3]);
        Vec2 bodyPosition = body.getPosition().mul(30);
        glTranslatef(bodyPosition.x, bodyPosition.y, 0);
        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
        glRectf(-width * 30, -height * 30, width * 30, height * 30);
    }

    // code to destroy Objects
    public void Destroy() {
        // RicochetClient.world.getPhysicsWorld().destroyBody(this.body);
        //  RicochetClient.world.getCurrentScene().getSceneObjects().remove(this);
        GameWorld.Instance.Destroy(this);

    }


}
