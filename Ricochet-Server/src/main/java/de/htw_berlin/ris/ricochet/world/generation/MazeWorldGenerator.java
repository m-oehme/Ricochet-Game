package de.htw_berlin.ris.ricochet.world.generation;

import de.htw_berlin.ris.ricochet.math.Conversion;
import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.shared.SWallPrefab;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MazeWorldGenerator implements WorldGenerator {
    private static Cell[][] myCells;
    private Vec2 tileSize;
    //int totalCellsX, totalCellsY;
    static final int[] WINDOW_DIMENSIONS = {1280, 960};
    public static final float unitConversion = 1 / 30f;
    static final Vec2 convertedSize = new Vec2(WINDOW_DIMENSIONS[0] * unitConversion, WINDOW_DIMENSIONS[1] * unitConversion);
    static float wallThickness = 0.2f;

    private List<Vec2> scenes;

    public MazeWorldGenerator() {
        Conversion.convertedSize = convertedSize;
    }

    @Override
    public List<SGameObject> generateWorld(int width, int height) {
        scenes = createScenes(width, height);
        ArrayList<SGameObject> staticObjects = new ArrayList<>();

        for (Vec2 sceneLocation : scenes) {
            int totalCellsX = (int) (Math.random() * 6)+2;
            int totalCellsY = (int) (Math.random() * 6)+2;

            staticObjects.addAll(fillScenesWithWalls(sceneLocation));

            staticObjects.addAll(CreateCells(sceneLocation, totalCellsX, totalCellsY));
        }

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

    private List<SGameObject> fillScenesWithWalls(Vec2 sceneLocation) {
        ArrayList<SGameObject> walls = new ArrayList<>();

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

        return walls;
    }


    private List<SGameObject> CreateCells(Vec2 whichScene, int totalCellsX, int totalCellsY) {

        myCells = new Cell[totalCellsX][totalCellsY];

        ArrayList<SGameObject> cellWalls = new ArrayList<>();

        tileSize = new Vec2(convertedSize.x / (float) totalCellsX, convertedSize.y / (float) totalCellsY);

        for (int y = 0; y < totalCellsY; y++) {
            for (int x = 0; x < totalCellsX; x++) {
                Cell cell = new Cell(x, y);
                Vec2 cellPos = new Vec2(x * tileSize.x, y * tileSize.y);
                cell.upperWallPos = new Vec2(cellPos.x + (tileSize.x / 2), cellPos.y + tileSize.y);
                cell.rightWallPos = new Vec2(cellPos.x + tileSize.x, cellPos.y + (tileSize.y / 2));
                cell.upperWall = new SWallPrefab(whichScene, Conversion.getGlobalCoordinates(cell.upperWallPos, whichScene), tileSize.x / 2, wallThickness, WallPrefabConfig.PrefabType.CellWall);
                if (y != totalCellsY - 1) {
                    cellWalls.add(cell.upperWall);
                }
                cell.rightWall = new SWallPrefab(whichScene, Conversion.getGlobalCoordinates(cell.rightWallPos, whichScene), wallThickness, tileSize.y / 2, WallPrefabConfig.PrefabType.CellWall);
                if (x != totalCellsX - 1) {
                    cellWalls.add(cell.rightWall);
                }
                myCells[x][y] = cell;
            }
        }
        ArrayList<SGameObject> mazeWalls = CreateMaze(cellWalls, totalCellsX, totalCellsY);
        return mazeWalls;
    }


    public ArrayList<SGameObject> CreateMaze(ArrayList<SGameObject> cellWalls, int totalCellsX, int totalCellsY) {
        ArrayList<SGameObject> mazeWalls = cellWalls;
        Stack<Cell> stackCells = new Stack<Cell>();
        Cell curCell = myCells[0][0];
        curCell.visited = true;
        int visited = 1;

        while (visited < totalCellsX * totalCellsY) {

            Cell cache = curCell.getRandomNeighbor(totalCellsX, totalCellsY, myCells);
            if (!(cache == null)) {
                stackCells.push(curCell);
                if (cache.x > curCell.x) {
                    mazeWalls.remove(curCell.rightWall);
                } else if (cache.y > curCell.y) {
                    mazeWalls.remove(curCell.upperWall);
                } else if (cache.y < curCell.y) {
                    mazeWalls.remove(cache.upperWall);
                } else if (cache.x < curCell.x) {
                    mazeWalls.remove(cache.rightWall);
                }
                curCell = cache;
                curCell.visited = true;
                visited++;
            } else if (stackCells.size() > 0) {
                curCell = stackCells.pop();
            }
        }
        return mazeWalls;
    }


}
