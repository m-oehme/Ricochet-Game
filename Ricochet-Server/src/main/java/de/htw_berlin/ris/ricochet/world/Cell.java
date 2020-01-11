package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class Cell {
    Vec2 upperWallPos, rightWallPos;

    int x, y;

    SGameObject upperWall;
    SGameObject rightWall;
    private ArrayList<Cell> neighbors;

     boolean visited;


     Cell(int x, int y) {
        this.x = x;
        this.y = y;
        visited = false;
    }


     void setNeighbors(int x, int y, int maxX, int maxY, Cell[][] myCells) {

        neighbors = new ArrayList<Cell>();
        if (x > 0) {
            neighbors.add(myCells[x - 1][y]);
        }

        if (y > 0) {
            neighbors.add(myCells[x][y - 1]);
        }

        if (x < maxX - 1) {
            neighbors.add(myCells[x + 1][y]);
        }

        if (y < maxY - 1) {
            neighbors.add(myCells[x][y + 1]);
        }
    }

    private boolean HasUnknownNeighbor(int totalCellsX, int totalCellsY, Cell[][] myCells) {
        setNeighbors(x, y, totalCellsX, totalCellsY, myCells);

        for (Cell cell : neighbors) {
            if (cell != null) {
                if (!cell.visited) {
                    return true;
                }
            }
        }
        return false;
    }

    Cell getRandomNeighbor(int totalCellsX, int totalCellsY, Cell[][] myCells) {
        ArrayList<Cell> unknownNeighbor = new ArrayList<Cell>();
        if (!HasUnknownNeighbor(totalCellsX, totalCellsY, myCells)) {
            return null;
        }
        if (neighbors.size() > 0) {
            for (Cell cell : neighbors) {
                if (cell != null) {
                    if (!cell.visited) {
                        unknownNeighbor.add(cell);
                    }
                }
            }
        } else {
            return null;
        }
        return unknownNeighbor.get((int) (Math.random() * unknownNeighbor.size()));
    }
}