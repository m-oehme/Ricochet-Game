package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.WallPrefab;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Scene {
    private int ID;
    private Vec2 location;
    private Set<GameObject> sceneObjects;

    public Scene(int ID, Vec2 location) {
        this.ID = ID;
        sceneObjects = new HashSet<GameObject>();


        this.location = location;
    }

    public void buildScene() {

        boolean neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(0, -1))) != null;

        WallPrefab lowerWall = new WallPrefab(neighbor ? WallPrefab.PrefabType.DoorWall : WallPrefab.PrefabType.FullWall, WallPrefab.PrefabPosition.Bottom, this);
        neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(0, +1))) != null;
        WallPrefab topWall = new WallPrefab(neighbor ? WallPrefab.PrefabType.DoorWall : WallPrefab.PrefabType.FullWall, WallPrefab.PrefabPosition.Top, this);
        neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(-1, 0))) != null;
        WallPrefab leftWall = new WallPrefab(neighbor ? WallPrefab.PrefabType.DoorWall : WallPrefab.PrefabType.FullWall, WallPrefab.PrefabPosition.Left, this);
        neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(+1, 0))) != null;
        WallPrefab rightWall = new WallPrefab(neighbor ? WallPrefab.PrefabType.DoorWall : WallPrefab.PrefabType.FullWall, WallPrefab.PrefabPosition.Right, this);
    }

    public void init() {
        for (GameObject G : sceneObjects) {
            if (!(G instanceof Player)) {
                G.Init();
            }
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Set<GameObject> getSceneObjects() {
        return sceneObjects;
    }

    public void setSceneObjects(Set<GameObject> SceneObjects) {
        this.sceneObjects = SceneObjects;
    }

    public Vec2 getLocation() {
        return location;
    }

    public void setLocation(Vec2 location) {
        this.location = location;
    }

}
