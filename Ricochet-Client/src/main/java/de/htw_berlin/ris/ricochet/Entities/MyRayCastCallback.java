package de.htw_berlin.ris.ricochet.Entities;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MyRayCastCallback implements RayCastCallback {
    public Vec2 collisionPoint = new Vec2(0,0);
    public boolean didHit;
    public Vec2 direction;

    @Override
    public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        //return -1 - to ignore the current fixture
        //return 0 - to terminate the raycast
        //return fraction - to clip the raycast at current point
        //return 1 - don't clip the ray and continue
        if (fraction < 1.0f) {
            collisionPoint.set(point) ;

            didHit = true;
            return fraction;
        }

        return 1;
    }
}