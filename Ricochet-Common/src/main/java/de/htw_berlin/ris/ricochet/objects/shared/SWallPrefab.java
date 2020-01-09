package de.htw_berlin.ris.ricochet.objects.shared;

import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import org.jbox2d.common.Vec2;

public class SWallPrefab extends SGameObject {
    private WallPrefabConfig.PrefabPosition prefabPosition;
    private WallPrefabConfig.PrefabType prefabType;

    public SWallPrefab(Vec2 scene, WallPrefabConfig.PrefabPosition prefabPosition, WallPrefabConfig.PrefabType prefabType) {
        super(scene, null);
        this.prefabPosition = prefabPosition;
        this.prefabType = prefabType;
    }

    public WallPrefabConfig.PrefabPosition getPrefabPosition() {
        return prefabPosition;
    }

    public WallPrefabConfig.PrefabType getPrefabType() {
        return prefabType;
    }
}
