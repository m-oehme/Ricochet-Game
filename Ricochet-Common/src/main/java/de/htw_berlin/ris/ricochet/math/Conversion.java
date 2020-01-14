package de.htw_berlin.ris.ricochet.math;

import org.jbox2d.common.Vec2;

public class Conversion {
    public static Vec2 convertedSize;

    public static Vec2 getLocalCoordinates(Vec2 position, Vec2 scene) {
        Vec2 sceneOffset = new Vec2(scene.x * convertedSize.x, scene.y * convertedSize.y);
        return position.sub(sceneOffset);
    }

    public static Vec2 getGlobalCoordinates(Vec2 position, Vec2 scene) {
        Vec2 sceneOffset = new Vec2(scene.x * convertedSize.x, scene.y * convertedSize.y);
        return position.add(sceneOffset);
    }
}
