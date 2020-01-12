package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.Entities.ContactListener;
import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectDestroyMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import de.htw_berlin.ris.ricochet.net.message.world.WorldRequestMessage;
import de.htw_berlin.ris.ricochet.objects.*;
import de.htw_berlin.ris.ricochet.objects.shared.SCompanionAI;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.shared.SPlayer;
import de.htw_berlin.ris.ricochet.objects.shared.SWallPrefab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.opengl.GL11.*;
/*


30 PIXELS == 1m!!!
 */

public class RicochetGameGUI {

    private static Logger log = LogManager.getLogger();
    private static RicochetGameGUI INSTANCE = null;
    public static RicochetGameGUI get() {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetGameGUI();
        }
        return INSTANCE;
    }

    private static final String WINDOW_TITLE = "Ricochet!";
    public static final int[] WINDOW_DIMENSIONS = {1280, 960};

    private final ExecutorService mainGUIThreadPool = Executors.newCachedThreadPool();
    private final ContactListener myListener = new ContactListener();

    private void render() {
        GameWorld.Instance.renderWorld();
    }

    private void logic() {
        GameWorld.Instance.updateWorld();
    }

    private void input() {
        // TODO make this nice pls
        if (GameWorld.Instance.getPlayer() != null) {
            GameWorld.Instance.getPlayer().handleInput();
        }
    }
    void cleanUp(boolean asCrash) {
        Display.destroy();
        System.exit(asCrash ? 1 : 0);
    }
    void setUpMatrices() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WINDOW_DIMENSIONS[0], 0, WINDOW_DIMENSIONS[1], 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    void setUpListeners(){

        GameWorld.Instance.getPhysicsWorld().setContactListener(myListener);
        Keyboard.enableRepeatEvents(true);
    }

    public void setUpWorld(){
        GameWorld.Instance = new GameWorld(new Vec2(0, 0),WINDOW_DIMENSIONS);
        Scene scene = GameWorld.Instance.generateInitWorld();
        GameWorld.Instance.setCurrentScene(scene.getLocation());
        GameWorld.Instance.getCurrentScene().init();
    }

    public void generateWorld(Vec2 worldSize, HashMap<ObjectId, SGameObject> gameObjectList) {
        GameWorld.Instance.generateWorld((int) worldSize.x, (int) worldSize.y, gameObjectList);
    }

    void setUpNetworking() {
        ClientNetManager.get().getHandlerFor(ObjectCreateMessage.class).registerObserver(objectCreateObserver);
        ClientNetManager.get().getHandlerFor(ObjectMoveMessage.class).registerObserver(objectMoveObserver);
    }

    void setUpObjects() {

        Vec2 playerPos = new Vec2(GameWorld.covertedSize.x/2,  GameWorld.covertedSize.y/2);
        ClientNetManager.get().sentMessage(new ObjectCreateMessage(ClientNetManager.get().getClientId(), null, new SPlayer(ClientNetManager.get().getClientId(), new Vec2(0,0), playerPos)));
    }

    private NetMessageObserver<ObjectMoveMessage> objectMoveObserver = objectMoveMessage -> {
        if (GameWorld.Instance.getWorldScenes().containsKey(objectMoveMessage.getScene())) {
            GameWorld.Instance.getWorldScenes().get(objectMoveMessage.getScene()).getSceneObjectsDynamic().stream()
                    .filter(gameObject -> gameObject.getObjectId() != null)
                    .filter(gameObject -> gameObject.getObjectId().equals(objectMoveMessage.getObjectId()))
                    .findFirst()
                    .ifPresent(gameObject -> {
                        gameObject.setPositionUpdate(objectMoveMessage.getPosition());
                    });
        }
    };

    private NetMessageObserver<ObjectCreateMessage> objectCreateObserver = objectCreateMessage -> {
        log.debug("Object Create - Type: " + objectCreateMessage.getSGameObject().getClass().getSimpleName() + " ID: " + objectCreateMessage.getObjectId());
        SGameObject sGameObject = objectCreateMessage.getSGameObject();

        if (sGameObject instanceof SPlayer) {
            if (GameWorld.Instance.getPlayer() == null) {
                Player playerObject = new Player(objectCreateMessage.getObjectId(), objectCreateMessage.getSGameObject().getPosition(), 0.5f, 0.5f, BodyType.DYNAMIC, GameWorld.Instance.getWorldScenes().get(objectCreateMessage.getSGameObject().getScene()));
                GameWorld.Instance.setPlayer(playerObject);

                ClientNetManager.get().sentMessage(new ObjectCreateMessage(
                        ClientNetManager.get().getClientId(),
                        null,
                        new SCompanionAI(playerObject.myScene.getLocation(), objectCreateMessage.getSGameObject().getPosition().add(new Vec2(5,5)), 0.5f, 0.5f, playerObject.getObjectId())
                        ));
            } else {
                EnemyPlayer playerObject = new EnemyPlayer(objectCreateMessage.getObjectId(), objectCreateMessage.getSGameObject().getPosition(), 0.5f, 0.5f, BodyType.DYNAMIC, GameWorld.Instance.getWorldScenes().get(objectCreateMessage.getSGameObject().getScene()));
            }
        } else if (sGameObject instanceof SCompanionAI) {
            if (((SCompanionAI) sGameObject).getGuardianPlayer().equals(GameWorld.Instance.getPlayer().getObjectId())){
                CompanionAI companionAI = new CompanionAI(objectCreateMessage.getObjectId(), objectCreateMessage.getSGameObject().getPosition(), 0.5f, 0.5f, GameWorld.Instance.getWorldScenes().get(objectCreateMessage.getSGameObject().getScene()), GameWorld.Instance.getPlayer());
            } else {
                EnemyCompanionAI playerObject = new EnemyCompanionAI(objectCreateMessage.getObjectId(), objectCreateMessage.getSGameObject().getPosition(), 0.5f, 0.5f, GameWorld.Instance.getWorldScenes().get(objectCreateMessage.getSGameObject().getScene()));
            }
        }
    };

    private void renderUpdate() {
        Display.update();
    }

    void enterGameLoop() {
        // TODO use Threads
        while (!Display.isCloseRequested()) {
            logic();
            input();
            render();
            renderUpdate();
         //   System.out.println(Mouse.getX() + " , "+ Mouse.getY());
        }
    }


    void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0], WINDOW_DIMENSIONS[1]));
            Display.setVSyncEnabled(true);
           // Display.setTitle(WINDOW_TITLE);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            cleanUp(true);
        }
    }


    public void init(){
        setUpDisplay();
        setUpWorld();
        setUpNetworking();
        setUpObjects();
        setUpMatrices();
        setUpListeners();
    }
    public void Run() {
        enterGameLoop();
        cleanUp(false);
    }
    public void exit(){
        cleanUp(false);
    }

    public ExecutorService getMainGUIThreadPool() {
        return mainGUIThreadPool;
    }
}

