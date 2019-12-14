package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.objects.GameObject;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class Door  extends GameObject {
// TODO rethink SCENE links

    public Door(Vec2 pos, float width, float height, BodyType bodyType) {
        super(pos, width, height, bodyType);
    }

    public Door(Vec2 pos, float width, float height, BodyType bodyType, float density, float restitution) {
        super(pos, width, height, bodyType, density, restitution);
    }
}
