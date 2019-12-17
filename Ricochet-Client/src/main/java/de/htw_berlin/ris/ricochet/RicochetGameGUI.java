package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.Entities.ContactListener;
import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
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

    private static RicochetGameGUI INSTANCE = null;
    public static RicochetGameGUI get() {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetGameGUI();
        }
        return INSTANCE;
    }

    private static final String WINDOW_TITLE = "Ricochet!";
    public static final int[] WINDOW_DIMENSIONS = {1280, 720};


    private final ContactListener myListener = new ContactListener();
    private Player player;

    private void render() {
        GameWorld.Instance.renderWorld();
    }

    private void logic() {
        GameWorld.Instance.updateWorld();
    }

    private void input() {
        // TODO make this nice pls
        player.handleInput();
    }


    void cleanUp(boolean asCrash) {
        Display.destroy();
        System.exit(asCrash ? 1 : 0);
    }
    void setUpMatrices() {
        glMatrixMode(GL_PROJECTION);
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
        GameWorld.Instance.getWorldScenes().add(sceneCenter);
        Scene sceneRight = new Scene(1, new Vec2 (1,0));
        GameWorld.Instance.getWorldScenes().add(sceneRight);
        Scene sceneLeft = new Scene(-1, new Vec2 (1,0));
        GameWorld.Instance.getWorldScenes().add(sceneLeft);
        GameWorld.Instance.setCurrentScene(0);

    }

    void setUpObjects() {
/// TODO ADD CONVERSION METHOD TO GAMEWORLD
        Vec2 playerPos = new Vec2(0.5f,  0.5f);
        //Vec2 playerPos = new Vec2(21,  7.5f);
        Player playerObject = new Player(playerPos, 0.5f, 0.5f, BodyType.DYNAMIC);
        player = playerObject;

        GameObject lowerWall = new GameObject(new Vec2(0,0), WINDOW_DIMENSIONS[0]/30+1, (WINDOW_DIMENSIONS[1]/30)/30,BodyType.STATIC, 1f, 0.5f);
        GameObject topWall = new GameObject(new Vec2(0,1), WINDOW_DIMENSIONS[0]/30+1, (WINDOW_DIMENSIONS[1]/30)/30,BodyType.STATIC, 1f, 0.5f);
        GameObject leftWall = new GameObject(new Vec2(0,0), WINDOW_DIMENSIONS[0]/30/30, (WINDOW_DIMENSIONS[1]/30),BodyType.STATIC, 1f, 0.5f);
        GameObject rightWall = new GameObject(new Vec2(1.01f,0), WINDOW_DIMENSIONS[0]/30/30, (WINDOW_DIMENSIONS[1]/30),BodyType.STATIC, 1f, 0.5f);


    }

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
        }
    }


    void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0], WINDOW_DIMENSIONS[1]));
            Display.setVSyncEnabled(true);
            Display.setTitle(WINDOW_TITLE);
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

