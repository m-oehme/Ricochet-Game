package de.htw_berlin.ris.ricochet.objects.shared;

import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import org.jbox2d.common.Vec2;

public class SWallPrefab extends SGameObject {
    private WallPrefabConfig.PrefabType prefabType;

    public SWallPrefab(Vec2 scene, Vec2 position, float width, float height, WallPrefabConfig.PrefabType prefabType) {
        super(scene, position, width, height);
        this.prefabType = prefabType;
    }

    public WallPrefabConfig.PrefabType getPrefabType() {
        return prefabType;
    }
}
