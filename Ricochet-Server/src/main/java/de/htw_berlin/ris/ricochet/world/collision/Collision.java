package de.htw_berlin.ris.ricochet.world.collision;

import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;

public interface Collision {
    boolean checkObjectCollision(SGameObject sGameObject1, SGameObject sGameObject2);
}
