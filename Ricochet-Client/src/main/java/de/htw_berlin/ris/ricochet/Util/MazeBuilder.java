package de.htw_berlin.ris.ricochet.Util;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.WallPrefab;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MazeBuilder {
    //The tiles have been modeled as 4x4 unity unit squares
    private static Cell[][] myCells;
    private int xExt, zExt;
    public int totalCellsX, totalCellsY;

    private Vec2 tileSize;


    /*private GameObject root, Floor, environment, ball;
    [Range(1,24)]
    public int xHalfExt = 1;
    [Range(1, 24)]
    public int zHalfExt = 1;

    public GameObject outerWall;
    public GameObject innerWall;
    public GameObject exitTile;
    public GameObject[] floorTiles;
    private static Cell[,] myCells;

    private int mazeHeight = 4;
    private float animSpeed = 0.025f;
    private float tileYOffset = 25;


    private Vector3 startPos;*/


    public class Cell {
        public Vec2 pos, upperWallPos, rightWallPos;
        public  float tileSize = 2;

        public GameObject upperWall;
        public GameObject rightWall;
        private ArrayList<Cell> neighbors;

        public boolean visited;

        public Cell(float x, float y) {
            visited = false;
            pos.x = x;
            pos.y = y;
        }

        public void buildCell(){

            //
        }

        public void setNeighbors(int x, int y, int maxX, int maxY)
        {

            neighbors = new ArrayList<Cell>();
            if (x > 0)
            {
                neighbors.add(myCells[x - 1][y]);
            }

            if (y > 0)
            {
                neighbors.add(myCells[x] [y - 1]);
            }

            if (x < maxX - 1)
            {
                neighbors.add(myCells[x + 1] [ y]);
            }

            if (y < maxY - 1)
            {
                neighbors.add(myCells[x] [y + 1]);
            }

        }

        private boolean HasUnknownNeighbor(int xExt, int zExt)
        {
           // setNeighbors(pos.x, pos.y, xExt, zExt);

            for (Cell cell : neighbors)
            {
                if (cell != null)
                {
                    if (!cell.visited)
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        private Cell getRandomNeighbor(int xExt, int zExt)
        {
            ArrayList<Cell> unknownNeighbor = new ArrayList<Cell>();
            if (!HasUnknownNeighbor(xExt, zExt))
            {
                return null;
            }
            if (neighbors.size() > 0)
            {
                for (Cell cell : neighbors)
                {
                    if (cell != null)
                    {
                        if (!cell.visited)
                        {
                            unknownNeighbor.add(cell);
                        }
                    }
                }
            }
            else
            {
                return null;
            }
            return unknownNeighbor.get((int)(Math.random()*unknownNeighbor.size()));
        }
    }

    void Init(){

        tileSize = new Vec2(GameWorld.Instance.covertedSize.x/ totalCellsX,GameWorld.Instance.covertedSize.y/ totalCellsY );


    }

    private void CreateFloorAndIWalls()
    {



        for (int y = 0; y < totalCellsY ; y = y++)
        {
            for (int x = 0; x < totalCellsX; x = x++)
            {

                Cell cell = new Cell(x * tileSize.x, y * tileSize.y);
                cell.upperWallPos = new Vec2(cell.pos.x+(tileSize.x/2),cell.pos.y + tileSize.y);
                cell.rightWallPos =  new Vec2(cell.pos.x + tileSize.x,cell.pos.y+(tileSize.y/2));


                 myCells[x][y] = cell;









            }
        }
    }


    /*public static void CreateMaze()
    {
        Stack<Cell> stackCells = new Stack<Cell>();

        Cell curCell = myCells[0][0];

        curCell.visited = true;
        int visited = 1;

        while (visited < xExt * zExt)
        {
            boolean vis = false;
            Cell cache = curCell.getRandomNeighbor(xExt, zExt);
            if (!(cache == null))
            {
                stackCells.Push(curCell);

                if (cache.x > curCell.x)
                {
                    bits.Remove(curCell.rightWall);
                    Destroy(curCell.rightWall);
                    vis = true;
                }
                else if (cache.y > curCell.y)
                {
                    bits.Remove(curCell.upperWall);
                    Destroy(curCell.upperWall);
                    vis = true;

                }
                else if (cache.y < curCell.y)
                {
                    bits.Remove(cache.upperWall);
                    Destroy(cache.upperWall);
                    vis = true;
                }
                else if (cache.x < curCell.x)
                {
                    bits.Remove(cache.rightWall);
                    Destroy(cache.rightWall);
                    vis = true;
                }
                curCell = cache;
                curCell.visited = true;
                visited++;
            }
            else if (stackCells.size > 0)
            {
                curCell = stackCells.Pop();
            }
        }
    }*/


}
