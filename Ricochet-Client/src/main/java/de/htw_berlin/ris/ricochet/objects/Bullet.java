package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends GameObject {

    public int lifeTime = 10;

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
        }, 10000);
    }


    @Override
    public void Update() {
        if (contact) {

            if (colObj != null && colObj instanceof Player) {
                Destroy();
            }
        }

    }
}

