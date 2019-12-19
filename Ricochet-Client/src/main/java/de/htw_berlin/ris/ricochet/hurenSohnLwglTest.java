package de.htw_berlin.ris.ricochet;

import org.jbox2d.common.Vec2;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class hurenSohnLwglTest {
    public void start() {
        try {

            Display.setDisplayMode(new DisplayMode(1280,720));
            Display.setVSyncEnabled(true);
            Display.create();
            glMatrixMode(GL_PROJECTION);
            glOrtho(0, 1280, 0, 720, 1, -1);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // init OpenGL here

        while (!Display.isCloseRequested()) {

            // render OpenGL here
            System.out.println(Mouse.getX());
            Display.update();
        }

        Display.destroy();
    }

    public static void main(String[] argv) {
        hurenSohnLwglTest displayExample = new hurenSohnLwglTest();
        displayExample.start();
    }
}

