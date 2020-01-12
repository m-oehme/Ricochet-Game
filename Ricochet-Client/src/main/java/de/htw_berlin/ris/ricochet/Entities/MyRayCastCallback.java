package de.htw_berlin.ris.ricochet.Entities;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MyRayCastCallback implements RayCastCallback {
    float  BOX_TO_WORLD =30; //As our box to world ratio is  1:100
    Vec2 collisionPoint;
    boolean isColliding;
    ArrayList<Body> collisionBodies;

    public MyRayCastCallback(ArrayList<Body> collisionBodies,Vec2 ep){
        this.collisionBodies=collisionBodies;
        collisionPoint=new Vec2(ep.x,ep.y).mul(BOX_TO_WORLD);
        isColliding=false;
    }

    public Vec2 GetCollisionPoint(){
        return collisionPoint;
    }

    public boolean DidRayCollide(){
        return isColliding;
    }

    @Override
    public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        //return -1 - to ignore the current fixture
        //return 0 - to terminate the raycast
        //return fraction - to clip the raycast at current point
        //return 1 - don't clip the ray and continue
        isColliding=true;
        collisionPoint.set(point).mul(BOX_TO_WORLD);
        if(!collisionBodies.contains(fixture.getBody()))
            collisionBodies.add(fixture.getBody());
        return fraction;
    }
}