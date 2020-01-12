package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.MyRayCastCallback;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.RicochetGameGUI;
import de.htw_berlin.ris.ricochet.math.Vector2;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.apache.logging.log4j.util.PropertySource;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.RayCastInput;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.lwjgl.Sys;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class CompanionAI extends EnemyCompanionAI implements Runnable {
    protected Player guardianPlayer;
    private boolean isAlive = true;
    protected ArrayList<MyRayCastCallback> rayCasts = new ArrayList<>();
    protected ArrayList<Vec2> rayDirs = new ArrayList<>();
    Fixture closestFixture;
    boolean hitShit;


    public CompanionAI(ObjectId objectId, Vec2 pos, float width, float height, Scene whichScene, Player guardianPlayer) {
        super(objectId, pos, width, height, whichScene);
        this.guardianPlayer = guardianPlayer;
    }

    @Override
    public void Init() {
        super.Init();

        RicochetGameGUI.get().getMainGUIThreadPool().execute(this);
    }

    @Override
    public void Update() {
        super.Update();
    }

    private float ConvertToBoxCoordinate(float x) {
        return x * 0.01f;
    }

/*    private void CalculateRaycast(Vec2 Origin) {
        if (GameWorld.Instance.getPhysicsWorld() == null) return;
        raycasts.clear();
        raycastBodies.clear();
        //START WITH 0 Degree
        direction.set(1, 0);
        float rotateAngle = 360 / RAY_COUNT;

        for (int i = 0; i < RAY_COUNT; i++) {
            float x1 = ConvertToBoxCoordinate(Origin.x);
            float y1 = ConvertToBoxCoordinate(Origin.y);
            float x2 = x1 + ConvertToBoxCoordinate(direction.x);
            float y2 = y1 + ConvertToBoxCoordinate(direction.y);
            Vec2 endPoint = new Vec2(x2, y2).sub(new Vec2(x1, y1));
            endPoint = endPoint.mul(endPoint.normalize());
            endPoint = endPoint.add(new Vec2(x1, y1));
            MyRayCastCallback ray = new MyRayCastCallback(raycastBodies, endPoint);
            GameWorld.Instance.getPhysicsWorld().raycast(ray, new Vec2(x1, y1), new Vec2(endPoint));
            raycasts.add(ray);
            Vector2 dir = new Vector2(direction.x, direction.y);
            dir.rotate(rotateAngle);
            direction.x = dir.x;
            direction.y = dir.y;
            //world.rayCast(callback,new Vector2(x,y),new Vector2(x2,y2));
        }
        if (raycastBodies.size() > 0) {
            System.out.println("bleh");
        }

    }*/

    protected void calculateAndCorrectCollision() {

        // Distance from Guardian Player
        Vec2 distance = guardianPlayer.position.sub(position);
        Vec2 weightGuardian = body.getLinearVelocity();

        if (distance.lengthSquared() > 27) {
            weightGuardian = distance;
        } else if (distance.lengthSquared() < 9.0) {
            weightGuardian = distance.negate().mul(0.5f);
        } else if (distance.lengthSquared() < 12.0 || distance.lengthSquared() > 25) {
            weightGuardian = new Vec2(0, 0);
        }

        // collisionPoint  = distance.mul(30);

        // Distance from other Objects
        Vec2 weightCollision = new Vec2(0, 0);



        Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);
        Vec2 myPosition = body.getPosition().sub(sceneOffset).mul(30);
        Vec2 PlayerPos =guardianPlayer.body.getPosition().sub(sceneOffset).mul(30);

        float radius = 5;
        float numRays = 12;
        rayDirs.clear();
        rayCasts.clear();

        ArrayList<Vec2> tempDir = new ArrayList<>();
        ArrayList<MyRayCastCallback> tempRays = new ArrayList<>();

        double angle = 0;
        double angleIncrement = 2 * Math.PI / numRays;
        for (int i = 0; i < numRays; i++) {
            angle = i * angleIncrement;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            Vec2 castDir = new Vec2((float)(x + position.x), (float)(y + position.y));
            hitShit =  CastRay(position, castDir);
            tempDir.add(castDir);
        }
     /*   Vec2 castDir = PlayerPos.sub(myPosition);
        castDir = castDir.mul(castDir.normalize());*/


       /* Vec2 castDir = PlayerPos.sub(myPosition);
        castDir = castDir.mul(castDir.normalize());




        //  System.out.println(closestFixture.getBody().getUserData().getClass().toString());
       /* if (hitStuff){
            Vec2 distanceCollision = body.getPosition().sub(collisionPoint);

            if (distanceCollision.lengthSquared() < 2) {
                weightCollision = distanceCollision.negate();
                float distScale = 10/distanceCollision.lengthSquared();
                weightCollision.mul(distScale);

            } else {
                weightCollision = new Vec2(1/distanceCollision.x, 1/distanceCollision.y);
            }
        }*/


        body.setLinearVelocity(weightGuardian);

        ClientNetManager.get().sentMessage(new ObjectMoveMessage(ClientNetManager.get().getClientId(), this.getObjectId(), myScene.getLocation(), this.position));
        rayDirs = tempDir;
    }

    public void debugRay (){
       /*for (Vec2 collisionPoint:collisionPoints ) {
            Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);
            Vec2 myPosition = body.getPosition().sub(sceneOffset).mul(30);
            Vec2 PlayerPos = guardianPlayer.body.getPosition().sub(sceneOffset).mul(30);
         //   Vec2 castDir = PlayerPos.sub(myPosition);
         //   castDir = castDir.mul(castDir.normalize()).add(myPosition);

            Vec2 colPoint = collisionPoint.sub(sceneOffset).mul(30);

            glBegin(GL_LINES);
            glVertex2f( myPosition.x ,myPosition.y );

            glVertex2f(colPoint.x ,colPoint.y  );


            glEnd();
        }*/

       for (MyRayCastCallback ray:rayCasts ) {
            Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);
            Vec2 myPosition = body.getPosition().sub(sceneOffset).mul(30);

            Vec2 colPoint = ray.collisionPoint.sub(sceneOffset).mul(30);
            Vec2 finalDir = ray.direction.sub(sceneOffset).mul(30);
            glBegin(GL_LINES);
            glVertex2f( myPosition.x ,myPosition.y );
            if (ray.didHit){
                glVertex2f(colPoint.x ,colPoint.y  );
            }else{

                glVertex2f(finalDir.x ,finalDir.y  );
            }

            glEnd();
        }



    }

    boolean CastRay(Vec2 origin, Vec2 direction) {

        World b2dWorld = body.getWorld();

        MyRayCastCallback rayCastCallback =  new MyRayCastCallback();

      /*  RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
                //return -1 - to ignore the current fixture
                //return 0 - to terminate the raycast
                //return fraction - to clip the raycast at current point
                //return 1 - don't clip the ray and continue
                if (fraction < 1.0f) {
                    collisionPoints.add(point) ;
                    closestFixture = fixture;
                    hit[0] = true;
                    return fraction;
                }

                return 1;
            }
        };*/


        b2dWorld.raycast(rayCastCallback, origin, direction);

        rayCastCallback.direction = direction;
        rayCasts.add(rayCastCallback);
        return rayCastCallback.didHit;
    }

    @Override
    public void run() {
        while (isAlive) {
            calculateAndCorrectCollision();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void Destroy() {
        isAlive = false;
        super.Destroy();
    }
}
