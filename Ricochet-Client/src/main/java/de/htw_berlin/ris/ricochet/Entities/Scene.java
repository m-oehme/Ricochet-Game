package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.WallPrefab;
import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Scene {
    private int ID;
    private Vec2 location;
    private Set<GameObject> sceneObjectsStatic;
    private Set<GameObject> sceneObjectsDynamic;

    public Scene(int ID, Vec2 location) {
        this.ID = ID;
        sceneObjectsStatic = new HashSet<GameObject>();
        sceneObjectsDynamic = new HashSet<GameObject>();


        this.location = location;
    }

    public void buildScene() {

        boolean neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(0, -1))) != null;

        WallPrefab lowerWall = new WallPrefab(neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall, WallPrefabConfig.PrefabPosition.Bottom, this);
        neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(0, +1))) != null;
        WallPrefab topWall = new WallPrefab(neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall, WallPrefabConfig.PrefabPosition.Top, this);
        neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(-1, 0))) != null;
        WallPrefab leftWall = new WallPrefab(neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall, WallPrefabConfig.PrefabPosition.Left, this);
        neighbor = GameWorld.Instance.getWorldScenes().get(location.add(new Vec2(+1, 0))) != null;
        WallPrefab rightWall = new WallPrefab(neighbor ? WallPrefabConfig.PrefabType.DoorWall : WallPrefabConfig.PrefabType.FullWall, WallPrefabConfig.PrefabPosition.Right, this);
    }

    public void init() {
        for (GameObject G : sceneObjectsStatic) {
            if (!(G instanceof Player)) {
                G.Init();
            }
        }
        for (GameObject G : sceneObjectsDynamic) {
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

    public Set<GameObject> getSceneObjectsStatic() {
        return sceneObjectsStatic;
    }

    public void setSceneObjectsStatic(Set<GameObject> SceneObjectsStatic) {
        this.sceneObjectsStatic = SceneObjectsStatic;
    }

    public Set<GameObject> getSceneObjectsDynamic() {
        return sceneObjectsDynamic;
    }

    public void setSceneObjectsDynamic(Set<GameObject> SceneObjectsDynamic) {
        this.sceneObjectsDynamic = SceneObjectsDynamic;
    }

    public Vec2 getLocation() {
        return location;
    }

    public void setLocation(Vec2 location) {
        this.location = location;
    }

}
