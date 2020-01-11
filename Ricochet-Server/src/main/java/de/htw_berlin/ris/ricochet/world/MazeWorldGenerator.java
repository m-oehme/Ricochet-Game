package de.htw_berlin.ris.ricochet.world;

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
    int totalCellsX, totalCellsY;
    static final int[] WINDOW_DIMENSIONS = {1280, 960};
    public static final float unitConversion = 1 / 30f;
    static final Vec2 convertedSize = new Vec2(WINDOW_DIMENSIONS[0] * unitConversion, WINDOW_DIMENSIONS[1] * unitConversion);
    static float wallThickness = 0.2f;

    public MazeWorldGenerator(int totalCellsX, int totalCellsY) {
        this.totalCellsX = totalCellsX;
        this.totalCellsY = totalCellsY;


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

            boolean neighbor = scenes.contains(sceneLocation.add(new Vec2(0, -1)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Bottom, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

            neighbor = scenes.contains(sceneLocation.add(new Vec2(0, +1)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Top, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

            neighbor = scenes.contains(sceneLocation.add(new Vec2(-1, 0)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Left, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));

            neighbor = scenes.contains(sceneLocation.add(new Vec2(+1, 0)));
            walls.add(new SWallPrefab(sceneLocation, WallPrefabConfig.PrefabPosition.Right, neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall));
            walls.addAll(CreateCells(sceneLocation, totalCellsX, totalCellsY));
        }

        return walls;
    }


    private List<SGameObject> CreateCells(Vec2 whichScene, int totalCellsX, int totalCellsY) {
        myCells = new Cell[totalCellsX][totalCellsY];

        ArrayList<SGameObject> cellWalls = new ArrayList<>();

        tileSize = new Vec2(convertedSize.x / (float) totalCellsX, convertedSize.y / (float) totalCellsY);

        for (int y = 0; y < totalCellsY; y++) {
            for (int x = 0; x < totalCellsX; x++) {
                Cell cell = new Cell(x , y );
                Vec2 cellPos = new Vec2(x * tileSize.x, y * tileSize.y);
                cell.upperWallPos = new Vec2(cellPos.x + (tileSize.x / 2), cellPos.y + tileSize.y);
                cell.rightWallPos = new Vec2(cellPos.x + tileSize.x, cellPos.y + (tileSize.y / 2));
                cell.upperWall = new SGameObject(whichScene, cell.upperWallPos, tileSize.x, wallThickness);
                cellWalls.add(cell.upperWall);
                cell.rightWall = new SGameObject(whichScene, cell.rightWallPos, wallThickness, tileSize.y);
                cellWalls.add(cell.rightWall);
                myCells[x][y] = cell;
            }
        }

        ArrayList<SGameObject> mazeWalls = CreateMaze(cellWalls);
        return mazeWalls;
    }


    public ArrayList<SGameObject> CreateMaze(ArrayList<SGameObject> cellWalls) {
        ArrayList<SGameObject> mazeWalls = cellWalls;

        Stack<Cell> stackCells = new Stack<Cell>();

        Cell curCell = myCells[0][0];

        curCell.visited = true;
        int visited = 1;

        while (visited < totalCellsX * totalCellsY) {

            Cell cache = curCell.getRandomNeighbor(totalCellsX, totalCellsY,myCells);
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
