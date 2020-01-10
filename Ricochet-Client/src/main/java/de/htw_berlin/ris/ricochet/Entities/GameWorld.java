package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.math.Vector2;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.SGameObject;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.opengl.GL11.*;

public class GameWorld {
    // TODO MAKE DELTA TIME FUNCTION!!
    private Map<Vec2, Scene> worldScenes;
    private Player player;
    public static GameWorld Instance;
    private World physicsWorld;
    private Scene currentScene;
    private boolean switchScene;
    private Vec2 newLocation;
    public static int[] WINDOW_DIMENSIONS;
    private float timeStep = 1 / 30f;
    public static final float unitConversion = 1 / 30f;
    public static Vec2 covertedSize;
    private Vec2 switchPos;
    private boolean gameOver;
    public ConcurrentLinkedQueue<GameObject> createObjectQueue = new ConcurrentLinkedQueue<GameObject>();

    public enum switchDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Set<GameObject> objectsToBeDestroyed = new HashSet<GameObject>();

    public GameWorld(Vec2 gravity, int[] WINDOW_DIMENSIONS) {
        this.WINDOW_DIMENSIONS = WINDOW_DIMENSIONS;
        physicsWorld = new World(new Vec2(0, 0), true);
        worldScenes = new Hashtable<>();
        covertedSize = new Vec2(WINDOW_DIMENSIONS[0] * unitConversion, WINDOW_DIMENSIONS[1] * unitConversion);
    }

    public Map<Vec2, Scene> getWorldScenes() {
        return worldScenes;
    }

    public void setWorldScenes(Dictionary<Vec2, Scene> s) {
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

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public void generateWorld(int sizeX, int sizeY) {
        int indexer = 0;
        boolean leftRight = false;
        for (int y = -sizeY / 2; y < sizeY / 2; y++) {
            for (int x = -sizeX / 2; x < sizeX / 2; x++) {
                Scene sceneCenter = new Scene(indexer, new Vec2(x, y));
                Instance.getWorldScenes().put(sceneCenter.getLocation(), sceneCenter);
                indexer++;
            }
        }
        for (Map.Entry<Vec2, Scene> Entry : worldScenes.entrySet()) {
            Entry.getValue().buildScene();
        }
    }

    public void switchScene(switchDirection direction) {

        switch (direction) {
            case UP:
                newLocation = currentScene.getLocation().add(new Vec2(0, 1));
                switchPos = new Vec2(player.body.getPosition().x, 0 + 0.5f);
                break;
            case DOWN:
                newLocation = currentScene.getLocation().add(new Vec2(0, -1));
                switchPos = new Vec2(player.body.getPosition().x, covertedSize.y - 0.5f);
                break;
            case LEFT:
                newLocation = currentScene.getLocation().add(new Vec2(-1, 0));
                switchPos = new Vec2(covertedSize.x - 0.5f, player.body.getPosition().y);
                break;
            case RIGHT:
                newLocation = currentScene.getLocation().add(new Vec2(1, 0));
                switchPos = new Vec2(0 + 0.5f, player.body.getPosition().y);
                break;
        }
        switchScene = true;
    }

    public void Reset() {
        currentScene.getSceneObjectsDynamic().remove(player);
        destroySceneBodies(currentScene);
        destroyAllDynamicBodies();
        setCurrentScene(new Vec2(0,0));
        currentScene.init();
        currentScene.getSceneObjectsDynamic().add(player);
        player.body.setTransform(new Vec2(GameWorld.covertedSize.x/2,  GameWorld.covertedSize.y/2), 0);
        gameOver = false;
    }

    private void finalizeSceneSwitch() {

        currentScene.getSceneObjectsDynamic().remove(player);
        destroySceneBodies(currentScene);
        destroyAllDynamicBodies();
        setCurrentScene(newLocation);
        currentScene.init();
        currentScene.getSceneObjectsDynamic().add(player);
        player.body.setTransform(switchPos, 0);
        switchScene = false;
    }

    private void destroyAllBodies() {
        Body b;
        for (b = physicsWorld.getBodyList(); b != null; b = b.getNext()) {
            physicsWorld.destroyBody(b);
        }
    }

    private void destroyAllDynamicBodies(){
        for (GameObject G : currentScene.getSceneObjectsDynamic()) {
            Destroy(G);
        }

    }

    private void destroySceneBodies(Scene scene) {
        for (GameObject G : scene.getSceneObjectsStatic()) {

            physicsWorld.destroyBody(G.body);

        }

        for (GameObject G : scene.getSceneObjectsDynamic()) {
            if (!(G instanceof Player)) {
                physicsWorld.destroyBody(G.body);
            }
        }
    }


    public void setCurrentScene(Vec2 location) {
        currentScene = worldScenes.get(location);
    }

    public void renderWorld() {
        // TODO add renderLayers
        glClear(GL_COLOR_BUFFER_BIT);
        for (GameObject O : currentScene.getSceneObjectsStatic()) {
            glPushMatrix();
            O.Render();
            glPopMatrix();
        }
        for (GameObject O : currentScene.getSceneObjectsDynamic()) {
            glPushMatrix();
            O.Render();
            glPopMatrix();
        }
    }

    public void cleanUp() {
        for (GameObject g : objectsToBeDestroyed) {
            getPhysicsWorld().destroyBody(g.body);
            g.myScene.getSceneObjectsDynamic().remove(g);
        }
        objectsToBeDestroyed.clear();
    }

    public void Destroy(GameObject G) {
        objectsToBeDestroyed.add(G);
    }

    public void updateWorld() {
        //TODO:: DO Static objects have to be updated?



       while( createObjectQueue.peek() != null){
           GameObject G  = createObjectQueue.poll();
           G.Init();
       }

        for (GameObject O : currentScene.getSceneObjectsDynamic()
        ) {
            O.Update();
        }
        for (GameObject O : currentScene.getSceneObjectsStatic()
        ) {
            O.Update();
        }
        physicsWorld.step(timeStep, 4, 3);
        if (switchScene) finalizeSceneSwitch();
        if (gameOver) Reset();
        cleanUp();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
