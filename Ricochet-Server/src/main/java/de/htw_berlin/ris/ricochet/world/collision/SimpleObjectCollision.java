package de.htw_berlin.ris.ricochet.world.collision;

import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import javafx.scene.shape.Rectangle;
import org.jbox2d.common.Vec2;

public class SimpleObjectCollision implements Collision {
    @Override
    public boolean checkObjectCollision(SGameObject sGameObject1, Vec2 newPosition,  SGameObject sGameObject2) {
//        Vec2 future = newPosition.add(newPosition.sub(sGameObject1.getPosition()));

//        Rectangle obj1 = new Rectangle(newPosition.x, newPosition.y, sGameObject1.getWidth(), sGameObject1.getHeight());
//
//        return obj1.intersects(sGameObject2.getPosition().x, sGameObject2.getPosition().y, sGameObject2.getWidth(), sGameObject2.getHeight());
        return false;
    }
}
