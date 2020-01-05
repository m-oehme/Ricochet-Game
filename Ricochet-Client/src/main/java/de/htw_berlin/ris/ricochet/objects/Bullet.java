package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
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

    public Bullet(Vec2 pos, float width, float height, BodyType bodyType) {
        super(pos, width, height, bodyType, GameWorld.Instance.getCurrentScene());
    }

    @Override
    public void Init() {
        super.Init();
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                Destroy();
            }
        }, 2000);
    }


    @Override
    public void Update() {
        if (contact) {
            if (colObj == null ) return;
            if (colObj instanceof Player) {
                Destroy();
                if(didBounce){
                    ((Player) colObj).health--;
                    if (((Player) colObj).health <= 0)((Player) colObj).death();
                }
            }else{
                didBounce = true;
            }

        }
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
}

