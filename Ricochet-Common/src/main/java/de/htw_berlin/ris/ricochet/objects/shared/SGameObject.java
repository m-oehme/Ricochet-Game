package de.htw_berlin.ris.ricochet.objects.shared;

import org.jbox2d.common.Vec2;

import java.io.Serializable;

public class SGameObject implements Serializable {
    private Vec2 scene;
    private Vec2 position;

    public SGameObject(Vec2 scene, Vec2 position) {
        this.scene = scene;
        this.position = position;
    }


    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getScene() {
        return scene;
    }
}
