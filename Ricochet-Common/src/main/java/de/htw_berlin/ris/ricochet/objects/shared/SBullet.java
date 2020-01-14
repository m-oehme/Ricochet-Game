package de.htw_berlin.ris.ricochet.objects.shared;

import org.jbox2d.common.Vec2;

public class SBullet extends SGameObject {
    private Vec2 shootDirection;

    public SBullet(Vec2 scene, Vec2 position, Vec2 shootDirection, float width, float height) {
        super(scene, position, width, height);
        this.shootDirection = shootDirection;
    }

    public Vec2 getShootDirection() {
        return shootDirection;
    }
}
