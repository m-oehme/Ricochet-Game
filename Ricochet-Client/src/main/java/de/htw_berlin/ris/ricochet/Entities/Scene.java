package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.WallPrefab;
import de.htw_berlin.ris.ricochet.objects.WallPrefabConfig;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Scene {
    private int ID;
    private Vec2 location;
    private Set<GameObject> sceneObjectsStatic;
    private Set<GameObject> sceneObjectsDynamic;

    public Scene(int ID, Vec2 location) {
        this.ID = ID;
        sceneObjectsStatic = ConcurrentHashMap.newKeySet();
        sceneObjectsDynamic = ConcurrentHashMap.newKeySet();


        this.location = location;
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

    public void unloadScene() {
        sceneObjectsDynamic.forEach(gameObject -> {
            GameWorld.Instance.getPhysicsWorld().destroyBody(gameObject.body);
        });

        sceneObjectsStatic.forEach(gameObject -> {
            GameWorld.Instance.getPhysicsWorld().destroyBody(gameObject.body);
        });
        sceneObjectsDynamic.clear();
        sceneObjectsStatic.clear();
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
