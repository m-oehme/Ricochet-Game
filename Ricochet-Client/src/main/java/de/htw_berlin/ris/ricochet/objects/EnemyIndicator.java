package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.Scene;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class EnemyIndicator extends GameObject{

    public EnemyIndicator(Vec2 position, Scene whichScene) {
        super(null, position.add(new Vec2(2,2)), 0.2f, 0.2f, BodyType.DYNAMIC, whichScene);
    }

    @Override
    public void Init() {
        super.Init();

        body.setLinearDamping(0.75f);
    }

    @Override
    public void Destroy() {

    }

    @Override
    protected void renderAtLocalPosition(Vec2 position) {
        renderSphere(position, 5f);
    }
}
