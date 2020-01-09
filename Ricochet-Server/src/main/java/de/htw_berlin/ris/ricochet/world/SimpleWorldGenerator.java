package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.shared.SWallPrefab;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

public class SimpleWorldGenerator implements WorldGenerator {

    @Override
    public List<SGameObject> generateWorld(int width, int height) {
        List<Vec2> scenes = createScenes(width, height);

        ArrayList<SGameObject> staticObjects = new ArrayList<>(fillScenesWithWalls(scenes));

        return staticObjects;
    }

    private List<Vec2> createScenes(int width, int height) {
        ArrayList<Vec2> sceneArr = new ArrayList<>();
        for (int y = -height / 2; y < height / 2; y++) {
            for (int x = -width / 2; x < width / 2; x++) {
                sceneArr.add(new Vec2(x, y));
            }
        }
        return sceneArr;
    }

    private List<SGameObject> fillScenesWithWalls(List<Vec2> scenes) {
        ArrayList<SGameObject> walls = new ArrayList<>();

        for (Vec2 sceneLocation : scenes) {
            boolean neighbor = scenes.contains(sceneLocation.add(new Vec2(0, -1)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Bottom, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

            neighbor = scenes.contains(sceneLocation.add(new Vec2(0, +1)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Top, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

            neighbor = scenes.contains(sceneLocation.add(new Vec2(-1, 0)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Left, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

            neighbor = scenes.contains(sceneLocation.add(new Vec2(+1, 0)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Right, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

        }

        return walls;
    }
}
