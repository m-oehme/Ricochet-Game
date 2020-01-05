package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.Entities.ContactListener;
import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.WallPrefab;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
        GameWorld.Instance.getPlayer().handleInput();
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
        GameWorld.Instance.generateWorld(4,4);
        GameWorld.Instance.setCurrentScene(new Vec2(0,0));
        GameWorld.Instance.getCurrentScene().init();


    }

    void setUpObjects() {
        Vec2 playerPos = new Vec2(GameWorld.covertedSize.x/2,  GameWorld.covertedSize.y/2);
        Player playerObject = new Player(playerPos, 0.5f, 0.5f, BodyType.DYNAMIC, GameWorld.Instance.getCurrentScene());
        GameWorld.Instance.setPlayer(playerObject);
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

