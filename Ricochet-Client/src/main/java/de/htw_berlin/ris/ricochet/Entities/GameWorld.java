package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.math.Vector2;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.*;

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
    private float timeStep = 1 / 60f;
    public static final float unitConversion = 1 / 30f;
    public static Vec2 covertedSize;
    private Vec2 switchPos;
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

    public void generateWorld (int sizeX, int sizeY){
        int indexer = 0;
        boolean leftRight = false;
        for (int y =-sizeY/2; y < sizeY/2;y++){
            for(int x = -sizeX/2; x < sizeX/2; x++){
                Scene sceneCenter = new Scene(indexer, new Vec2(x,y));
                Instance.getWorldScenes().put(sceneCenter.getLocation(),sceneCenter);
                indexer ++;
            }
        }
        for (Map.Entry<Vec2,Scene> Entry: worldScenes.entrySet()){
            Entry.getValue().buildScene();
        }
    }

    public void switchScene(switchDirection direction) {

        switch (direction) {
            case UP:
                newLocation = currentScene.getLocation().add(new Vec2(0, 1));
                switchPos = new Vec2(player.body.getPosition().x,0+0.5f);
                break;
            case DOWN:
                newLocation = currentScene.getLocation().add(new Vec2(0, -1));
                switchPos = new Vec2(player.body.getPosition().x,covertedSize.y-0.5f);
                break;
            case LEFT:
                newLocation = currentScene.getLocation().add(new Vec2(-1, 0));
                switchPos = new Vec2(covertedSize.x-0.5f,player.body.getPosition().y);
                break;
            case RIGHT:
                newLocation = currentScene.getLocation().add(new Vec2(1, 0));
                switchPos = new Vec2(0+0.5f,player.body.getPosition().y);
                break;
        }
        switchScene = true;
    }

    private void finalizeSceneSwitch(){

        currentScene.getSceneObjects().remove(player);
         destroySceneBodies(currentScene);
        setCurrentScene(newLocation);
        currentScene.init();
        currentScene.getSceneObjects().add(player);
        player.body.setTransform(switchPos,0);
        switchScene = false;
    }

    private  void destroyAllBodies(){
        Body b;
        for(b = physicsWorld.getBodyList(); b != null; b = b.getNext()){
            physicsWorld.destroyBody(b);
        }
    }

    private void destroySceneBodies(Scene scene){
        for (GameObject G: scene.getSceneObjects()) {
            if ( !(G instanceof Player)){
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
        for (GameObject O : currentScene.getSceneObjects()) {
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

    public void Destroy(GameObject G) {
        objectsToBeDestroyed.add(G);
    }

    public void updateWorld() {
        for (GameObject O : currentScene.getSceneObjects()
        ) {
            O.Update();
        }
        physicsWorld.step(timeStep, 8, 3);
        if (switchScene) finalizeSceneSwitch();
        cleanUp();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
