package de.htw_berlin.ris.ricochet.world.collision;

import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import org.jbox2d.common.Vec2;

public interface Collision {
    boolean checkObjectCollision(SGameObject sGameObject1, Vec2 newPosition, SGameObject sGameObject2);
}
