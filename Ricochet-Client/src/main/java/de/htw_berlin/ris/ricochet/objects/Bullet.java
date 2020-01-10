package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectDestroyMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.opengl.GL11.glColor3f;

public class Bullet extends GameObject {

    public int lifeTime = 10;
    //TODO make didBounce visually determinable
    public boolean didBounce;
    private Vec2 shootDir = null;

    public Bullet(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType) {
        super(objectId, pos, width, height, bodyType, GameWorld.Instance.getCurrentScene());
    }

    @Override
    public void Init() {
        super.Init();
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                ClientNetManager.get().sentMessage(new ObjectDestroyMessage(ClientNetManager.get().getClientId(), getObjectId()));
            }
        }, 2000);
    }


    @Override
    public void Update() {
        if (shootDir != null) {
            body.applyForce(shootDir, body.getPosition());

            shootDir = null;
        }
        if (contact) {
            if (colObj == null ) return;
            if (colObj instanceof Player) {
                ClientNetManager.get().sentMessage(new ObjectDestroyMessage(ClientNetManager.get().getClientId(), getObjectId()));
                if(didBounce){
                    ((Player) colObj).health--;
                    if (((Player) colObj).health <= 0)((Player) colObj).death();
                }
            }else{
                didBounce = true;
            }

        }

//        if (body != null) {
//            ClientNetManager.get().sentMessage(new ObjectMoveMessage(ClientNetManager.get().getClientId(), this.getObjectId(), myScene.getLocation(), body.getPosition()));
//        }
    }
    @Override
    public void Render(){
        float[] rgbVal = new float[4];
        if (didBounce) {
            Color.red.getRGBColorComponents(rgbVal);
        } else {
            objectColor.getRGBColorComponents(rgbVal);
        }

        glColor3f(rgbVal[0], rgbVal[1], rgbVal[3]);
        super.Render();
    }

    public void shootInDirection(Vec2 shootDir) {
        this.shootDir = shootDir;
    }
}

