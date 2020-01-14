package de.htw_berlin.ris.ricochet.items;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.objects.Bullet;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.shared.SBullet;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.input.Mouse;

import java.util.Timer;
import java.util.TimerTask;

public class Weapon {

    protected boolean reloading,coolDown;
    protected float shotSpeed = 1.0f;
    protected GameObject owner;
    protected int reloadTime;
    protected int magazineSize;
    protected int fireRate;
    protected int bulletsInMagazine;
    protected Timer t = new Timer();

    public Weapon() {
        ClientNetManager.get().getHandlerFor(ObjectCreateMessage.class).registerObserver(objectCreateObserver);

    }

    // TODO :: GUI for bullets etc.
    public  void shoot(){
        if (bulletsInMagazine<=0){
            reloading = true;
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    completeReload();
                }
            }, reloadTime);
        }else{
            coolDown = true;
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    completeCoolDown();
                }
            }, fireRate);
        }



    }

    public  void shoot(Vec2 shotDir){
        if (bulletsInMagazine<=0){
            reloading = true;
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    completeReload();
                }
            }, reloadTime);
        }else{
            coolDown = true;
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    completeCoolDown();
                }
            }, fireRate);
        }



    }

    private  void completeReload(){
        reloading = false;
        bulletsInMagazine = magazineSize;
    }

    private  void completeCoolDown(){
        coolDown = false;
        bulletsInMagazine--;
    }


    private NetMessageObserver<ObjectCreateMessage> objectCreateObserver = objectCreateMessage -> {
        if (objectCreateMessage.getSGameObject() instanceof SBullet) {
            if (GameWorld.Instance.getLoadedScenes().contains(objectCreateMessage.getSGameObject().getScene())) {
                SBullet sBullet = (SBullet) objectCreateMessage.getSGameObject();
                Scene scene = GameWorld.Instance.getWorldScenes().get(sBullet.getScene());

                Bullet Bullet = new Bullet(
                        objectCreateMessage.getObjectId(),
                        sBullet.getPosition().add((sBullet.getShootDirection().mul(1.5f))),
                        0.25f,
                        0.25f,
                        BodyType.DYNAMIC,
                        scene);
                Vec2 shotDir = sBullet.getShootDirection().mul(shotSpeed * 10);
                Bullet.shootInDirection(shotDir);
            }
        }
    };
}
