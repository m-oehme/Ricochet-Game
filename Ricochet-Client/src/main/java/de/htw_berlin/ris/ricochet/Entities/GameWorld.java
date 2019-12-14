package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.math.Vector2;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

public class GameWorld {
    // TODO MAKE DELTA TIME FUNCTION!! ADD CONVERSION METHOD
    private HashSet<Scene> worldScenes;
    private Player player;
    public static GameWorld Instance;
    private World physicsWorld;
    private Scene currentScene;
    public static int[] WINDOW_DIMENSIONS;
    private float timeStep = 1 / 60f;
    public static Vec2 unitConversion;

    private Set<GameObject> objectsToBeDestroyed = new HashSet<GameObject>();

    public GameWorld(Vec2 gravity,int[] WINDOW_DIMENSIONS) {
        this.WINDOW_DIMENSIONS = WINDOW_DIMENSIONS;
        physicsWorld = new World(new Vec2(0, 0), true);
        worldScenes = new HashSet<Scene>();
        unitConversion = new Vec2(WINDOW_DIMENSIONS[0] / 30, WINDOW_DIMENSIONS[1] / 30);

    }

    public HashSet<Scene> getWorldScenes() {
        return worldScenes;
    }

    public void setWorldScenes(HashSet<Scene> worldScenes) {
        this.worldScenes = worldScenes;
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public void setPhysicsWorld(World physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(int ID) {

        for (Scene s : worldScenes) {
            if (s.getID() == ID) this.currentScene = s;
        }
        // TODO ADD log to say that there is no scene with matching ID

    }

    public void renderWorld() {
        // TODO add renderLayers
        glClear(GL_COLOR_BUFFER_BIT);

        for (GameObject O : currentScene.getSceneObjects()) {
            glPushMatrix();
            O.Render();
            glPopMatrix();
        }
        for (GameObject O : currentScene.getSceneDoors()) {
            glPushMatrix();
            O.Render();
            glPopMatrix();
        }

    }

    public void cleanUp() {
        for (GameObject g : objectsToBeDestroyed) {
            getPhysicsWorld().destroyBody(g.body);
            getCurrentScene().getSceneObjects().remove(g);
        }
        objectsToBeDestroyed.clear();
    }
    public void Destroy(GameObject G){
        objectsToBeDestroyed.add(G);
    }

    public void updateWorld() {

        for (GameObject O : currentScene.getSceneObjects()
        ) {
            O.Update();
        }
        physicsWorld.step(timeStep, 8, 3);
        cleanUp();

    }
}
