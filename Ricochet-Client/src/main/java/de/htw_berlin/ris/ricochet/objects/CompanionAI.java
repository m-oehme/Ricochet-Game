package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.MyRayCastCallback;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.RicochetGameGUI;
import de.htw_berlin.ris.ricochet.items.Pistol;
import de.htw_berlin.ris.ricochet.items.Weapon;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.opengl.GL11.*;

public class CompanionAI extends EnemyCompanionAI implements Runnable {
    protected Player guardianPlayer;
    private boolean isAlive = true;
    protected CopyOnWriteArrayList<MyRayCastCallback> rayCasts = new CopyOnWriteArrayList<>();
    boolean hitShit;
    private Weapon currentWeap  = new Pistol(this,5000);
    float speed = 15.0f;
    float range = 10;
    private boolean traveling, waiting;
    Vec2 originalPos;
    float totalDist;
    int lonelyCounter = 1 ;

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
        calculateAndCorrectCollision();
        handleAttack();

    }

    private float ConvertToBoxCoordinate(float x) {
        return x * 0.01f;
    }


    protected void handleAttack(){

        if (guardianPlayer.nearestEnemy == null){
            return;
        }
        Vec2 enemyDir = guardianPlayer.nearestEnemy.position.sub(position);
        float dist = enemyDir.length();
        if(dist < range){
            currentWeap.shoot(enemyDir);
        }
    }



    protected void calculateAndCorrectCollision() {

        // Distance from Guardian Player
        Vec2 weightGuardian = new Vec2(0, 0);
        if (!traveling && !waiting) {
            originalPos = position;
            traveling = true;
        }
        totalDist = guardianPlayer.position.sub(originalPos).length();
        Vec2 distance = guardianPlayer.position.sub(position);
        float playerDist = distance.length();
        distance.normalize();
        if (traveling && !waiting) {

            float speedFac = playerDist / totalDist;

            if (playerDist <= 3) {
                speedFac = 0.01f;
                traveling = false;
                waiting = true;
            }
            weightGuardian = distance.mul(speed * speedFac);
        }

        if (waiting) {
            if (playerDist >= 5) {
                waiting = false;
            }
        }

        // Distance from other Objects
        Vec2 weightCollision = new Vec2(0, 0);


        float radius = 2;
        float numRays = 12;

        rayCasts.clear();


        double angle = 0;
        double angleIncrement = 2 * Math.PI / numRays;
        for (int i = 0; i < numRays; i++) {
            angle = i * angleIncrement;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            Vec2 castDir = new Vec2((float) (x + position.x), (float) (y + position.y));
            hitShit = CastRay(position, castDir);
        }
        for (MyRayCastCallback ray : rayCasts) {


            if (ray.didHit) {
                Vec2 distanceCollision = body.getPosition().sub(ray.collisionPoint);
                float dist = distanceCollision.length();
                float weightFac = 0;
                float weightFacCol = 1 - (dist / radius);
                if (dist < 1) {
                   // System.out.println("too close");
                    weightGuardian = weightGuardian.mul(weightFac);

                }
                weightCollision = weightCollision.add(distanceCollision.mul(speed/2 * weightFacCol));
            }
        }



        Vec2 finalVel = weightGuardian.add(weightCollision);
        body.setLinearVelocity(finalVel);


        if  (playerDist > 5){

            lonelyCounter ++;

        }else{
            lonelyCounter = 1;
        }

        if (lonelyCounter %500 == 0){
            if (myScene == guardianPlayer.myScene){
                setPositionUpdate(guardianPlayer.position.add(distance));

            }else{
                setPositionUpdate(guardianPlayer.position.add(distance),guardianPlayer.myScene.getLocation());
            }
            lonelyCounter = 1 ;
        }



        ClientNetManager.get().sentMessage(new ObjectMoveMessage(ClientNetManager.get().getClientId(), this.getObjectId(), myScene.getLocation(), this.position));

    }


    public void debugRay() {

        for (MyRayCastCallback ray : rayCasts) {
            Vec2 sceneOffset = new Vec2(myScene.getLocation().x * GameWorld.covertedSize.x, myScene.getLocation().y * GameWorld.covertedSize.y);
            Vec2 myPosition = body.getPosition().sub(sceneOffset).mul(30);

            Vec2 colPoint = ray.collisionPoint.sub(sceneOffset).mul(30);
            Vec2 finalDir = ray.direction.sub(sceneOffset).mul(30);
            glBegin(GL_LINES);
            glVertex2f(myPosition.x, myPosition.y);
            if (ray.didHit) {
                glVertex2f(colPoint.x, colPoint.y);
            } else {

                glVertex2f(finalDir.x, finalDir.y);
            }
            glEnd();
        }


    }
    boolean CastRay(Vec2 origin, Vec2 direction) {

        World b2dWorld = body.getWorld();

        MyRayCastCallback rayCastCallback = new MyRayCastCallback();

        b2dWorld.raycast(rayCastCallback, origin, direction);

        rayCastCallback.direction = direction;
        rayCasts.add(rayCastCallback);
        return rayCastCallback.didHit;
    }

    @Override
    public void run() {
        while (isAlive) {
            //calculateAndCorrectCollision();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void renderAtLocalPosition(Vec2 position) {
        this.debugRay();
        super.renderAtLocalPosition(position);

    }

    @Override
    public void Destroy() {
        isAlive = false;
        super.Destroy();
    }
}
