package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glRectf;

public class GameObject {
    private ObjectId objectId;
    private BodyDef bodyDef;
    public Body body;
    private FixtureDef bodyFixture;
    private Vec2 positionUpdate;
    protected float width, height;
    public boolean contact;
    public java.awt.Color objectColor;
    protected GameObject colObj;

    public Scene myScene;

 // TODO DO STUFF THAT LETS ME FLAG WETHER OR NOT TO ADD AS BODY, AND ADD FUNCTION THAT ENABLES REINSTATING PHYSICAL PROPERTIES!!!
    public GameObject(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, Scene whichScene) {
        this.objectId = objectId;
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
        float grayscale  = (float)Math.random() * 0.25f + 0.25f;
        objectColor = new java.awt.Color(grayscale, grayscale, grayscale);
        //  objectColor = new java.awt.Color((float) Math.random() * 0.1f, (float) Math.random() * 0.5f + 0.5f, (float) Math.random() * 0.75f + 0.25f);

        myScene = whichScene;

        GameWorld.Instance.addGameObject(this);
    }

    public GameObject(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, float density, float restitution, Scene whichScene) {
        this.objectId = objectId;
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
        float grayscale  = 0.25f;//(float)Math.random() * 0.25f + 0.25f;
        objectColor = new java.awt.Color(grayscale, grayscale, grayscale);
        // objectColor = new java.awt.Color((float) Math.random(), (float) Math.random(), (float) Math.random());

        myScene = whichScene;

        GameWorld.Instance.addGameObject(this);
    }

    public void Init() {
        if (bodyDef.type == BodyType.DYNAMIC) {
            myScene.getSceneObjectsDynamic().add(this);
        }
        else  myScene.getSceneObjectsStatic().add(this);

        if (myScene.equals(GameWorld.Instance.getCurrentScene())) {
            body = GameWorld.Instance.getPhysicsWorld().createBody(bodyDef);
            body.createFixture(bodyFixture);
            if (this instanceof Player) {
                body.setLinearDamping(0.75f);
            }
        }
    }

    // Game Logic goes here
    public void Update() {
        if (positionUpdate != null) {
            body.setTransform(positionUpdate, 0);
            positionUpdate = null;
        }
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
        if (!(this instanceof Player) &&!(this instanceof Bullet)) {
            float[] rgbVal = new float[4];
            objectColor.getRGBColorComponents(rgbVal);
            glColor3f(rgbVal[0], rgbVal[1], rgbVal[3]);
        }
        Vec2 bodyPosition = body.getPosition().mul(30);
        glTranslatef(bodyPosition.x, bodyPosition.y, 0);
        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
        glRectf(-width * 30, -height * 30, width * 30, height * 30);
    }

    // code to destroy Objects
    public void Destroy() {

        GameWorld.Instance.Destroy(this);

    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setPositionUpdate(Vec2 positionUpdate) {
        this.positionUpdate = positionUpdate;
    }

    public void changeScene(Scene newScene) {
        myScene.getSceneObjectsDynamic().remove(this);
        myScene = newScene;
        newScene.getSceneObjectsDynamic().add(this);
    }
}
