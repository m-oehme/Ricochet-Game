package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import static de.htw_berlin.ris.ricochet.Entities.GameWorld.covertedSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glRectf;

public class GameObject {
    protected static Logger log = LogManager.getLogger();

    private ObjectId objectId;
    private BodyDef bodyDef;
    public Body body;
    private FixtureDef bodyFixture;
    protected Vec2 position;
    protected Vec2 positionUpdate;
    protected float width, height;
    public boolean contact;
    public java.awt.Color objectColor;
    protected GameObject colObj;
    protected String name;
    protected BodyType type;

    public Scene myScene;

 // TODO DO STUFF THAT LETS ME FLAG WETHER OR NOT TO ADD AS BODY, AND ADD FUNCTION THAT ENABLES REINSTATING PHYSICAL PROPERTIES!!!
    public GameObject(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, Scene whichScene) {
        this.position = pos;
        this.objectId = objectId;
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.userData = this;
        PolygonShape boxShape = new PolygonShape();
        this.width = width;
        this.height = height;
        boxShape.setAsBox(width, height);
        bodyFixture = new FixtureDef();
        bodyFixture.density = 0.1f;
        bodyFixture.shape = boxShape;
        type = bodyType;
        contact = false;
        float grayscale  = (float)Math.random() * 0.25f + 0.25f;
        objectColor = new java.awt.Color(grayscale, grayscale, grayscale);
        //  objectColor = new java.awt.Color((float) Math.random() * 0.1f, (float) Math.random() * 0.5f + 0.5f, (float) Math.random() * 0.75f + 0.25f);

        myScene = whichScene;

        GameWorld.Instance.addGameObject(this);
    }

    public GameObject(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, float density, float restitution, Scene whichScene) {
        this.objectId = objectId;
        this.position = pos;
        bodyDef = new BodyDef();
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

        type = bodyType;

        myScene = whichScene;

        GameWorld.Instance.addGameObject(this);
    }

    public void Init() {
        if (type == BodyType.DYNAMIC) myScene.getSceneObjectsDynamic().add(this);
        else  myScene.getSceneObjectsStatic().add(this);

        bodyDef.position.set(this.position);
        body = GameWorld.Instance.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(bodyFixture);
        if (this instanceof Player) {
            body.setLinearDamping(0.75f);
        }
    }

    // Game Logic goes here
    public void Update() {
        if (positionUpdate != null) {
            body.setTransform(positionUpdate, 0);
            positionUpdate = null;
        }
        this.position = new Vec2(body.getPosition().x, body.getPosition().y);

        switchScene(this.position);
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
        Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);
        Vec2 bodyPosition = body.getPosition().sub(sceneOffset).mul(30);
        glTranslatef(bodyPosition.x, bodyPosition.y, 0);
        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
        glRectf(-width * 30, -height * 30, width * 30, height * 30);
    }

    // code to destroy Objects
    public void Destroy() {

        GameWorld.Instance.Destroy(this);
    }

    public void switchScene(Vec2 position) {
        Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);

        if (position.sub(sceneOffset).x > covertedSize.x) {
            log.debug("Scene switch to RIGHT");
            Vec2 newScenePosition = myScene.getLocation().add(new Vec2(1, 0));
            Vec2 entrancePos = new Vec2(0 + 0.5f, position.y);
            finalizeSceneSwitch(newScenePosition, entrancePos);
        }
        if (position.sub(sceneOffset).x < 0) {
            log.debug("Scene switch to LEFT");
            Vec2 newScenePosition = myScene.getLocation().add(new Vec2(-1, 0));
            Vec2 entrancePos = new Vec2(covertedSize.x - 0.5f, position.y);
            finalizeSceneSwitch(newScenePosition, entrancePos);
        }
        if (position.sub(sceneOffset).y > covertedSize.y) {
            log.debug("Scene switch to UP");
            Vec2 newScenePosition = myScene.getLocation().add(new Vec2(0, 1));
            Vec2 entrancePos = new Vec2(position.x, 0 + 0.5f);
            finalizeSceneSwitch(newScenePosition, entrancePos);
        }
        if (position.sub(sceneOffset).y < 0) {
            log.debug("Scene switch to DOWN");
            Vec2 newScenePosition = myScene.getLocation().add(new Vec2(0, -1));
            Vec2 entrancePos = new Vec2(position.x, covertedSize.y - 0.5f);
            finalizeSceneSwitch(newScenePosition, entrancePos);
        }
    }

    private void finalizeSceneSwitch(Vec2 newScenePosition, Vec2 entrancePos) {
        if (GameWorld.Instance.getWorldScenes().containsKey(newScenePosition)) {
            myScene.getSceneObjectsDynamic().remove(this);

            myScene = GameWorld.Instance.getWorldScenes().get(newScenePosition);
            myScene.getSceneObjectsDynamic().add(this);
//            this.setPositionUpdate(entrancePos);
        }
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setPositionUpdate(Vec2 positionUpdate) {
        this.positionUpdate = positionUpdate;
    }
}
