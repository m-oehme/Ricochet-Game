package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.Entities.ContactListener;
import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.SPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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

    void setUpWorld(){
        GameWorld.Instance = new GameWorld(new Vec2(0, 0),WINDOW_DIMENSIONS);
        Scene sceneCenter = new Scene(0, new Vec2(0,0));
        GameWorld.Instance.getWorldScenes().put(sceneCenter.getLocation(),sceneCenter);
        Scene sceneRight = new Scene(1, new Vec2 (1,0));
        GameWorld.Instance.getWorldScenes().put(sceneRight.getLocation(),sceneRight);
        Scene sceneLeft = new Scene(-1, new Vec2 (-1,0));
        GameWorld.Instance.getWorldScenes().put(sceneLeft.getLocation(),sceneLeft);
        GameWorld.Instance.setCurrentScene(sceneCenter.getLocation());

    }

    void setUpNetworking() {
        ClientNetManager.get().getHandlerFor(ObjectCreateMessage.class).registerObserver(objectCreateObserver);
    }

    void setUpObjects() {

        GameObject lowerWall = new GameObject(new Vec2(0,0), GameWorld.covertedSize.x+1, GameWorld.covertedSize.y/30,BodyType.STATIC, 1f, 0.5f);
        GameObject topWall = new GameObject(new Vec2(0,GameWorld.covertedSize.y), GameWorld.covertedSize.x+1, GameWorld.covertedSize.y/30,BodyType.STATIC, 1f, 0.5f);
        GameObject leftWall = new GameObject(new Vec2(0,0), GameWorld.covertedSize.x/30, GameWorld.covertedSize.y,BodyType.STATIC, 1f, 0.5f);
//        GameObject rightWall = new GameObject(new Vec2(1.01f,0), GameWorld.covertedSize.x/30, (WINDOW_DIMENSIONS[1]/30),BodyType.STATIC, 1f, 0.5f);

        Vec2 playerPos = new Vec2(GameWorld.covertedSize.x/2,  GameWorld.covertedSize.y/2);
        ClientNetManager.get().sentMessage(new ObjectCreateMessage(ClientNetManager.get().getClientId(), null, new SPlayer(ClientNetManager.get().getClientId(), playerPos)));
    }

    private NetMessageObserver<ObjectCreateMessage> objectCreateObserver = objectCreateMessage -> {
        log.debug("Object Create - Type: " + objectCreateMessage.getSGameObject().getClass().getSimpleName() + " ID: " + objectCreateMessage.getObjectId());

        if (objectCreateMessage.getSGameObject() instanceof SPlayer) {

            //Vec2 playerPos = new Vec2(21,  7.5f);
            Player playerObject = new Player(objectCreateMessage.getSGameObject().getPosition(), 0.5f, 0.5f, BodyType.DYNAMIC);
            playerObject.setObjectId(objectCreateMessage.getObjectId());

            GameWorld.Instance.setPlayer(playerObject);
        }

    };

    private void renderUpdate() {
        Display.update();
    }

    void enterGameLoop() {
        // TODO use Threads
        while (!Display.isCloseRequested()) {
            render();
            logic();
            input();
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

//    public static void main(String[] args) {
//        setUpDisplay();
//        setUpWorld();
//        setUpObjects();
//        setUpMatrices();
//        setUpListeners();
//        enterGameLoop();
//        cleanUp(false);
//    }

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
}

