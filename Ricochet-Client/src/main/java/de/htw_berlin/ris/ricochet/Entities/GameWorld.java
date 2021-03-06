package de.htw_berlin.ris.ricochet.Entities;

import de.htw_berlin.ris.ricochet.math.Conversion;
import de.htw_berlin.ris.ricochet.math.Vector2;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectDestroyMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import de.htw_berlin.ris.ricochet.net.message.world.WorldRequestScenesMessage;
import de.htw_berlin.ris.ricochet.objects.*;
import de.htw_berlin.ris.ricochet.objects.shared.SCompanionAI;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.shared.SPlayer;
import de.htw_berlin.ris.ricochet.objects.shared.SWallPrefab;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class GameWorld {
    private static Logger log = LogManager.getLogger();
    // TODO MAKE DELTA TIME FUNCTION!!
    private ConcurrentHashMap<Vec2, Scene> worldScenes;
    private ArrayList<Vec2> loadedScenes = new ArrayList<>();
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

    private Set<GameObject> objectsToBeDestroyed = ConcurrentHashMap.newKeySet();

    public GameWorld(Vec2 gravity, int[] WINDOW_DIMENSIONS) {
        this.WINDOW_DIMENSIONS = WINDOW_DIMENSIONS;
        physicsWorld = new World(new Vec2(0, 0), true);
        worldScenes = new ConcurrentHashMap<>();
        covertedSize = new Vec2(WINDOW_DIMENSIONS[0] * unitConversion, WINDOW_DIMENSIONS[1] * unitConversion);

        Conversion.convertedSize = covertedSize;

        ClientNetManager.get().getHandlerFor(ObjectDestroyMessage.class).registerObserver(objectDestroyObserver);
        ClientNetManager.get().getHandlerFor(WorldRequestScenesMessage.class).registerObserver(worldRequestScenesObserver);
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void loadSceneChunk(Vec2 chunkCenterScene) {
        ArrayList<Vec2> sceneList = new ArrayList<>();

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                Vec2 sceneLoc = chunkCenterScene.add(new Vec2(x, y));

                if (!loadedScenes.contains(sceneLoc)) {
                    sceneList.add(sceneLoc);
                }
            }
        }

        log.debug("Requesting Scenes: " + sceneList.toString());
        ClientNetManager.get().sentMessage(new WorldRequestScenesMessage(
                ClientNetManager.get().getClientId(),
                sceneList
        ));
    }

    public void unloadSceneChunks(Vec2 chunkCenterScene) {
        ArrayList<Vec2> sceneList = new ArrayList<>();

        for (int y = -2; y <= 2; y++) {
            for (int x = -2; x <= 2; x++) {
                Vec2 sceneLoc = chunkCenterScene.add(new Vec2(x, y));

                if (loadedScenes.contains(sceneLoc)) {
                    sceneList.add(sceneLoc);
                }
            }
        }

        List<Vec2> collect = loadedScenes.stream()
                .filter(sceneEntry -> !sceneList.contains(sceneEntry) && worldScenes.containsKey(sceneEntry))
                .collect(Collectors.toList());

        collect.forEach(vec2 -> worldScenes.get(vec2).unloadScene());

        log.debug("Unload Scenes: " + collect.toString());
        loadedScenes.removeAll(collect);
    }

    public Scene generateInitWorld() {
        Scene sceneCenter = new Scene(0, new Vec2((int) Math.random() * 2, (int) Math.random() * 2));
        worldScenes.put(sceneCenter.getLocation(), sceneCenter);
        return sceneCenter;
    }

    public void generateWorldScenes(int sizeX, int sizeY) {
        int indexer = 0;
        for (int y = -sizeY / 2; y < sizeY / 2; y++) {
            for (int x = -sizeX / 2; x < sizeX / 2; x++) {
                if (x == 0 && y == 0) {
                    worldScenes.get(new Vec2(0,0)).setID(indexer);
                } else {
                    Scene sceneCenter = new Scene(indexer, new Vec2(x, y));
                    worldScenes.put(sceneCenter.getLocation(), sceneCenter);
                }

                indexer++;
            }
        }
    }

    public void addPlayersToWorld(HashMap<ObjectId, SGameObject> playerList) {
        playerList.forEach((objectId, sGameObject) -> {
            Scene scene = worldScenes.get(sGameObject.getScene());

            if (sGameObject instanceof SPlayer) {
                EnemyPlayer playerObject = new EnemyPlayer(objectId, Conversion.getGlobalCoordinates(sGameObject.getPosition(), scene.getLocation()), 0.5f, 0.5f, BodyType.DYNAMIC, scene);
            }
        });
    }

    public void generateWorldObjects(HashMap<ObjectId, SGameObject> gameObjectList) {
        gameObjectList.forEach((objectId, sGameObject) -> {
            Scene scene = worldScenes.get(sGameObject.getScene());

            if (sGameObject instanceof SCompanionAI) {
                EnemyCompanionAI playerObject = new EnemyCompanionAI(objectId, Conversion.getGlobalCoordinates(sGameObject.getPosition(), scene.getLocation()), 0.5f, 0.5f, scene);
            } else if (sGameObject instanceof SWallPrefab) {
                SWallPrefab sWallPrefab = (SWallPrefab) sGameObject;

                if (sWallPrefab.getPrefabType() == WallPrefabConfig.PrefabType.CellWall) {
                    GameObject cellWall = WallPrefab.simpleWall(objectId,WallPrefabConfig.PrefabType.CellWall, sWallPrefab.getPosition(), sWallPrefab.getWidth(), sWallPrefab.getHeight(), scene);
                } else {
                    GameObject wall = WallPrefab.simpleBorderWall(objectId, sWallPrefab, scene);
                }
            }
        });
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
        ArrayList<Vec2> vec2Set = new ArrayList<>(GameWorld.Instance.getWorldScenes().keySet());
        int r  = (int) (Math.random() * vec2Set.size());

        currentScene.getSceneObjectsDynamic().remove(player);
        destroyAllDynamicBodies();
        setCurrentScene(vec2Set.get(r));
        currentScene.getSceneObjectsDynamic().add(player);

        Vec2 newPosition = new Vec2(GameWorld.covertedSize.x / 2, GameWorld.covertedSize.y / 2);

        player.setPositionUpdate(Conversion.getGlobalCoordinates(newPosition, currentScene.getLocation()), currentScene.getLocation());
        player.setEnemyIndicatorAlive();
        loadSceneChunk(currentScene.getLocation());
        gameOver = false;
    }



    private void finalizeSceneSwitch() {
        currentScene.getSceneObjectsDynamic().remove(player);
        setCurrentScene(newLocation);
        currentScene.getSceneObjectsDynamic().add(player);
        player.myScene = worldScenes.get(newLocation);
        loadSceneChunk(currentScene.getLocation());
        switchScene = false;
    }

    private void destroyAllDynamicBodies() {
        worldScenes.values().forEach(scene -> {
            scene.getSceneObjectsDynamic().stream()
                    .filter(gameObject -> gameObject instanceof Bullet)
                    .forEach(GameObject::Destroy);
        });

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
        ClientNetManager.get().sentMessage(new ObjectDestroyMessage(ClientNetManager.get().getClientId(), G.getObjectId()));
    }

    private NetMessageObserver<ObjectDestroyMessage> objectDestroyObserver = objectDestroyMessage -> {
        GameWorld.Instance.getWorldScenes().forEach((vec2, scene) -> {
            scene.getSceneObjectsDynamic().stream()
                    .filter(gameObject -> gameObject.getObjectId() != null)
                    .filter(gameObject -> gameObject.getObjectId().equals(objectDestroyMessage.getObjectId()))
                    .findFirst()
                    .ifPresent(gameObject -> objectsToBeDestroyed.add(gameObject));
        });
    };

    private NetMessageObserver<WorldRequestScenesMessage> worldRequestScenesObserver = worldRequestScenesMessage -> {
        log.debug("Received Chunk Objects: " + worldRequestScenesMessage.getSceneList().size());
        loadedScenes.addAll(worldRequestScenesMessage.getSceneList());
        GameWorld.Instance.generateWorldObjects(worldRequestScenesMessage.getGameObjectList());

        GameWorld.Instance.unloadSceneChunks(currentScene.getLocation());
    };

    public void updateWorld() {
        //TODO:: DO Static objects have to be updated?

        while (createObjectQueue.peek() != null) {
            GameObject G = createObjectQueue.poll();
            G.Init();
        }

        worldScenes.values().forEach(scene -> {
            scene.getSceneObjectsDynamic().forEach(GameObject::Update);
        });

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

    public void addGameObject(GameObject gameObject) {
        createObjectQueue.offer(gameObject);
    }

    public ArrayList<Vec2> getLoadedScenes() {
        return loadedScenes;
    }
}
