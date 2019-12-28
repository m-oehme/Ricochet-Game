package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.objects.GameObject;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Scene {
    private  int ID;
    private Vec2 location;
    private Set<GameObject> sceneObjects;

    public Scene(int ID, Vec2 location) {
        this.ID = ID;
        sceneObjects = Collections.newSetFromMap(new ConcurrentHashMap<>());

        this.location = location;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public synchronized Set<GameObject> getSceneObjects() {
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
