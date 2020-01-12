package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import static de.htw_berlin.ris.ricochet.Entities.GameWorld.covertedSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glRectf;

public class GameObject {
    protected static Logger log = LogManager.getLogger();

    private ObjectId objectId;
    private BodyDef bodyDef;
    public Body body;
    private FixtureDef bodyFixture, sensorFixture;
    protected Vec2 position;
    private Vec2 positionUpdate;
    protected float width, height;
    public boolean contact;
    public java.awt.Color objectColor;
    protected GameObject colObj;
    protected String name;
    protected BodyType type;

    final short GROUP_PLAYER = -1;
    final short GROUP_OTHERPLAYER = -1;
    final short GROUP_BULLETS = -1;
    final short GROUP_SENSOR = 1;
    final short GROUP_SCENE = 1;

    public Scene myScene;

    public GameObject(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, Scene whichScene) {
        this.position = pos;
        this.objectId = objectId;
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.userData = this;

        this.width = width;
        this.height = height;
        if (this instanceof Bullet) {
            CircleShape circleShape = new CircleShape();
            CircleShape circleShapeSesnsor = new CircleShape();
            circleShape.m_radius = width;
            circleShape.m_radius = width + (width * 0.01f);

            bodyFixture = new FixtureDef();
            bodyFixture.density = 0.1f;
            bodyFixture.shape = circleShape;

            sensorFixture = new FixtureDef();
            sensorFixture.density = 0.1f;
            sensorFixture.shape = circleShapeSesnsor;
            sensorFixture.isSensor = true;

        }else{
            PolygonShape boxShape = new PolygonShape();
            PolygonShape boxShapeSensor = new PolygonShape();
            boxShape.setAsBox(width, height);
            boxShapeSensor.setAsBox(width + (width * 0.01f), height + (height * 0.01f));

            bodyFixture = new FixtureDef();
            bodyFixture.density = 0.1f;
            bodyFixture.shape = boxShape;

            boxShapeSensor.setAsBox(width + (width * 0.01f), height + (height * 0.01f));
            sensorFixture = new FixtureDef();
            sensorFixture.density = 0.1f;
            sensorFixture.shape = boxShapeSensor;
            sensorFixture.isSensor = true;
        }




        if (this instanceof Player) {
            bodyFixture.filter.groupIndex = GROUP_PLAYER;
            sensorFixture.filter.groupIndex = GROUP_SENSOR;
        } else if (this instanceof EnemyPlayer) {
            bodyFixture.filter.groupIndex = GROUP_OTHERPLAYER;
            sensorFixture.filter.groupIndex = GROUP_SENSOR;
        } else if (this instanceof Bullet) {
            bodyFixture.filter.groupIndex = GROUP_BULLETS;
        } else {
            bodyFixture.filter.groupIndex = GROUP_SCENE;
        }

        type = bodyType;
        contact = false;
        float grayscale = (float) Math.random() * 0.25f + 0.25f;
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
        PolygonShape boxShapeSensor = new PolygonShape();
        boxShapeSensor.setAsBox(width + (width * 0.1f), height + (height * 0.1f));
        sensorFixture = new FixtureDef();
        sensorFixture.density = 0.1f;
        sensorFixture.shape = boxShapeSensor;
        sensorFixture.isSensor = true;

        if (this instanceof Player) {
            bodyFixture.filter.groupIndex = GROUP_PLAYER;
            sensorFixture.filter.groupIndex = GROUP_SENSOR;
        } else if (this instanceof EnemyPlayer) {

            bodyFixture.filter.groupIndex = GROUP_OTHERPLAYER;
            sensorFixture.filter.groupIndex = GROUP_SENSOR;
        } else if (this instanceof Bullet) {
            bodyFixture.filter.groupIndex = GROUP_BULLETS;
        } else {
            bodyFixture.filter.groupIndex = GROUP_SCENE;
        }

        contact = false;
        type = bodyType;
        float grayscale = 0.25f;//(float)Math.random() * 0.25f + 0.25f;
        objectColor = new java.awt.Color(grayscale, grayscale, grayscale);


        myScene = whichScene;

        GameWorld.Instance.addGameObject(this);
    }

    public void Init() {
        if (type == BodyType.DYNAMIC) myScene.getSceneObjectsDynamic().add(this);
        else myScene.getSceneObjectsStatic().add(this);

        bodyDef.position.set(this.position);
        body = GameWorld.Instance.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(bodyFixture);

        if (this instanceof Player || this instanceof EnemyPlayer) {
            body.setLinearDamping(0.75f);
            body.createFixture(sensorFixture);
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
        if (!(this instanceof Player) && !(this instanceof Bullet)) {
            float[] rgbVal = new float[4];
            objectColor.getRGBColorComponents(rgbVal);
            glColor3f(rgbVal[0], rgbVal[1], rgbVal[3]);
        }
        Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);
        Vec2 bodyPosition = body.getPosition().sub(sceneOffset).mul(30);

        if (this instanceof Bullet) {
           // DrawCircle(bodyPosition, 5f, 10);
            renderSphere(bodyPosition, 10f);
        } else if (this instanceof CompanionAI || this instanceof EnemyCompanionAI){
            glTranslatef(bodyPosition.x, bodyPosition.y, 0);
            glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);

            renderTriangle(-width/2 * 30,   width/2 *30,  height * 30);
        }
        else {
            glTranslatef(bodyPosition.x, bodyPosition.y, 0);
            glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
            glRectf(-width * 30, -height * 30, width * 30, height * 30);

        }

    }
    private void renderTriangle(float x1, float x2, float y1){

        glBegin(GL_TRIANGLES);

       /*glVertex2f(x1+(position.x*30),  y1+(position.y*30));
        glVertex2f(x2+(position.x*30), y1+(position.y*30));
        glVertex2f((position.x*30), y2+(position.y*30));*/
        glVertex2f(x1,  0);
        glVertex2f(x2, 0);
        glVertex2f(0, y1);

        glEnd();

    }


    private void renderSphere(Vec2 position, float radius) {
        int numVertices = 20;
          glBegin(GL_POLYGON);
        {
            double angle = 0;
            double angleIncrement = 2 * Math.PI / numVertices;
            for (int i = 0; i < numVertices; i++) {
                angle = i * angleIncrement;
                double x = radius * Math.cos(angle);
                double y = radius * Math.sin(angle);
                glVertex2d(x+position.x, y+position.y);
            }
        }
        glEnd();
    }


    private void DrawCircle(Vec2 center, float r, int num_segments) {
        float theta = 2 * 3.1415926f / (float) (num_segments);
        float c = (float) Math.cos(theta);//precalculate the sine and cosine
        float s = (float) Math.sin(theta);
        float t;

        float x = r;//we start at angle = 0
        float y = 0;

        glBegin(GL_LINE_LOOP);
        for (int ii = 0; ii < num_segments; ii++) {
            glVertex2f(x + center.x, y + center.y);//output vertex

            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        glEnd();
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
