package de.htw_berlin.ris.ricochet.objects.shared;

import de.htw_berlin.ris.ricochet.objects.ObjectId;
import org.jbox2d.common.Vec2;

public class SCompanionAI extends SGameObject {
    private ObjectId guardianPlayer;

    public SCompanionAI(Vec2 scene, Vec2 position, float width, float height, ObjectId guardianPlayer) {
        super(scene, position, width, height);
        this.guardianPlayer = guardianPlayer;
    }

    public ObjectId getGuardianPlayer() {
        return guardianPlayer;
    }
}
