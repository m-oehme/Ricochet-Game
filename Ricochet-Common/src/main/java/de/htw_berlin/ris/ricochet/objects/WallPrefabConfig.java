package de.htw_berlin.ris.ricochet.objects;

public abstract class WallPrefabConfig {

    public enum PrefabType {
        FullWall,
        HalfWall,
        DoorWall,
        QuarterWall,
        CellWall
    }

    public enum PrefabPosition{
        Left,
        Right,
        Bottom,
        Top
    }

}
