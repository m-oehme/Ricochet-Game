package de.htw_berlin.ris.ricochet.world.collision;

import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

public class SimpleObjectCollision implements Collision {
    @Override
    public boolean checkObjectCollision(SGameObject sGameObject1, SGameObject sGameObject2) {
        Rectangle obj1 = new Rectangle(sGameObject1.getPosition().x, sGameObject1.getPosition().y, sGameObject1.getWidth(), sGameObject1.getHeight());

//        return obj1.intersects(sGameObject2.getPosition().x, sGameObject2.getPosition().y, sGameObject2.getWidth(), sGameObject2.getHeight());
        return false;
    }
}
