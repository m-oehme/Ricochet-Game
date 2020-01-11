package de.htw_berlin.ris.ricochet.objects.shared;

import org.jbox2d.common.Vec2;

import java.io.Serializable;

public class SGameObject implements Serializable {
    private Vec2 scene;
    private Vec2 position;
    private float width, height;

    public SGameObject(Vec2 scene, Vec2 position) {
        this.scene = scene;
        this.position = position;
    }

    public SGameObject(Vec2 scene, Vec2 position,float width, float height) {
        this.scene = scene;
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
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

    public void setScene(Vec2 scene) {
        this.scene = scene;
    }
}
