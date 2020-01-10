package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import org.jbox2d.common.Vec2;

public class SBullet extends SGameObject {
    private Vec2 shotDir;
    public SBullet(Vec2 position, Vec2 shotDir) {
        super(position);
        this.shotDir = shotDir;
    }



}

