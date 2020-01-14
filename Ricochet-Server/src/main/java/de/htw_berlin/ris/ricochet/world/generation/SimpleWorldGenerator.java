package de.htw_berlin.ris.ricochet.world.generation;

import de.htw_berlin.ris.ricochet.math.Conversion;
import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.shared.SWallPrefab;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import java.util.ArrayList;
import java.util.List;

public class SimpleWorldGenerator implements WorldGenerator {

    static final int[] WINDOW_DIMENSIONS = {1280, 960};
    public static final float unitConversion = 1 / 30f;
    static final Vec2 convertedSize = new Vec2(WINDOW_DIMENSIONS[0] * unitConversion, WINDOW_DIMENSIONS[1] * unitConversion);

    public SimpleWorldGenerator() {
        Conversion.convertedSize = convertedSize;
    }

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

            // Bottom
            if (scenes.contains(sceneLocation.add(new Vec2(0, -1)))) {
                Vec2 postion1 = new Vec2 ((convertedSize.x/6),0);
                Vec2 postion2 = new Vec2 ((convertedSize.x/6)*5, 0);

                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion1, sceneLocation),
                        convertedSize.x/6,
                        convertedSize.y/30,
                        WallPrefabConfig.PrefabType.DoorWall
                ));

                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion2, sceneLocation),
                        convertedSize.x/6,
                        convertedSize.y/30,
                        WallPrefabConfig.PrefabType.DoorWall
                ));
            } else {
                Vec2 position = new Vec2 (convertedSize.x/2,0);

                float width = convertedSize.x/2;
                float height = convertedSize.y/30;
                walls.add(new SWallPrefab(sceneLocation, Conversion.getGlobalCoordinates(position, sceneLocation), width, height, WallPrefabConfig.PrefabType.FullWall));
            }

            // TOP
            if (scenes.contains(sceneLocation.add(new Vec2(0, +1)))) {
                Vec2 postion1 = new Vec2 ((convertedSize.x/6),convertedSize.y);
                Vec2 postion2 = new Vec2 ((convertedSize.x/6)*5, convertedSize.y);


                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion1, sceneLocation),
                        convertedSize.x/6,
                        convertedSize.y/30,
                        WallPrefabConfig.PrefabType.FullWall
                ));

                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion2, sceneLocation),
                        convertedSize.x/6,
                        convertedSize.y/30,
                        WallPrefabConfig.PrefabType.FullWall
                ));
            } else {
                Vec2 position = new Vec2 (convertedSize.x/2,convertedSize.y);

                float width = convertedSize.x/2;
                float height = convertedSize.y/30;
                walls.add(new SWallPrefab(sceneLocation, Conversion.getGlobalCoordinates(position, sceneLocation), width, height, WallPrefabConfig.PrefabType.FullWall));
            }

            // LEFT
            if (scenes.contains(sceneLocation.add(new Vec2(-1, 0)))) {
                Vec2 postion1 = new Vec2 (0,(convertedSize.y/6));
                Vec2 postion2 = new Vec2 (0, (convertedSize.y/6)*5);


                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion1, sceneLocation),
                        convertedSize.x/30,
                        convertedSize.y/6,
                        WallPrefabConfig.PrefabType.FullWall
                ));

                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion2, sceneLocation),
                        convertedSize.x/30,
                        convertedSize.y/6,
                        WallPrefabConfig.PrefabType.FullWall
                ));
            } else {
                Vec2 position = new Vec2 (0,convertedSize.y/2);

                float width = convertedSize.x/30;
                float height = convertedSize.y/2;
                walls.add(new SWallPrefab(sceneLocation, Conversion.getGlobalCoordinates(position, sceneLocation), width, height, WallPrefabConfig.PrefabType.FullWall));
            }

            // RIGHT
            if (scenes.contains(sceneLocation.add(new Vec2(+1, 0)))) {
                Vec2 postion1 = new Vec2 (convertedSize.x,(convertedSize.y/6));
                Vec2 postion2 = new Vec2 (convertedSize.x, (convertedSize.y/6)*5);

                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion1, sceneLocation),
                        convertedSize.x/30,
                        convertedSize.y/6,
                        WallPrefabConfig.PrefabType.FullWall
                ));

                walls.add(new SWallPrefab(
                        sceneLocation,
                        Conversion.getGlobalCoordinates(postion2, sceneLocation),
                        convertedSize.x/30,
                        convertedSize.y/6,
                        WallPrefabConfig.PrefabType.FullWall
                ));
            } else {
                Vec2 position = new Vec2 (convertedSize.x,convertedSize.y/2);

                float width = convertedSize.x/30;
                float height = convertedSize.y/2;
                walls.add(new SWallPrefab(sceneLocation, Conversion.getGlobalCoordinates(position, sceneLocation), width, height, WallPrefabConfig.PrefabType.FullWall));
            }

        }

        return walls;
    }



}
