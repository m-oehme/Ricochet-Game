package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
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
    public String name;
    private BodyDef bodyDef;
    public Body body;
    private FixtureDef bodyFixture;
    public Vec2 position;
    protected float width, height;
    public boolean contact;
    public java.awt.Color objectColor;
    protected GameObject colObj;



    public static GameObject FullWall(Vec2 position, boolean horizontal, Scene whichScene){
       GameObject Wall =  new GameObject(position, (horizontal) ? GameWorld.covertedSize.x+1 :GameWorld.covertedSize.x/30,  (horizontal) ? GameWorld.covertedSize.y/30 : GameWorld.covertedSize.y,BodyType.STATIC, 1f, 0.5f,whichScene );
       return Wall;

    }

    public GameObject(Vec2 pos, float width, float height, BodyType bodyType, Scene whichScene) {

        bodyDef = new BodyDef();
        bodyDef.position.set(pos.x, pos.y);
        bodyDef.type = bodyType;
        bodyDef.userData = this;
        PolygonShape boxShape = new PolygonShape();
        this.width = width;
        this.height = height;
        boxShape.setAsBox(width, height);
        bodyFixture = new FixtureDef();
        bodyFixture.density = 0.1f;
        bodyFixture.shape = boxShape;

        contact = false;
        objectColor = new java.awt.Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        whichScene.getSceneObjects().add(this);
        if (whichScene.equals(GameWorld.Instance.getCurrentScene())){
            Init();
        }

    }

    public GameObject(Vec2 pos, float width, float height, BodyType bodyType, float density, float restitution, Scene whichScene) {
        bodyDef = new BodyDef();
        bodyDef.position.set(pos.x, pos.y);
        bodyDef.type = bodyType;
        bodyDef.userData = this;
        PolygonShape boxShape = new PolygonShape();
        this.width = width;
        this.height = height;
        boxShape.setAsBox(width, height);
        bodyFixture = new FixtureDef();
        bodyFixture.density = density;
        bodyFixture.shape = boxShape;
        bodyFixture.restitution = restitution;
        contact = false;
        objectColor = new java.awt.Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        whichScene.getSceneObjects().add(this);
        if (whichScene.equals(GameWorld.Instance.getCurrentScene())){
            Init();
        }
    }

    public void Init() {
        body = GameWorld.Instance.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(bodyFixture);
        if (this instanceof Player) {
            body.setLinearDamping(0.75f);
        }
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
// TODO :: rethink rendering now its always 2x width & height
        float[] rgbVal = new float[4];
        if (contact) {
            Color.red.getRGBColorComponents(rgbVal);
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
