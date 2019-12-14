package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.objects.GameObject;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Scene {
    private  int ID;
    private Vec2 location;
    private Set<GameObject> sceneObjects;
    private ArrayList<Door> sceneDoors;


    public Scene(int ID, Vec2 location) {
        this.ID = ID;
        sceneObjects = new HashSet<GameObject>();
        sceneDoors  = new ArrayList<Door>();
        this.location = location;
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

    public ArrayList<Door> getSceneDoors() {
        return sceneDoors;
    }

    public Vec2 getLocation() {
        return location;
    }

    public void setLocation(Vec2 location) {
        this.location = location;
    }

    public void setSceneDoors(ArrayList<Door> sceneDoors) {
        this.sceneDoors = sceneDoors;
    }
}
