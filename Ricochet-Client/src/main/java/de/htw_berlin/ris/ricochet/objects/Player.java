package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.items.MachineGun;
import de.htw_berlin.ris.ricochet.items.Pistol;
import de.htw_berlin.ris.ricochet.items.Weapon;
import de.htw_berlin.ris.ricochet.math.Vector2;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor3f;

public class Player extends GameObject {
    private int ID;
    public float speed = 1.0f;
    // TODO :: Make Death and Reset Game
    public int health = 5;
    protected Weapon currentWeapon;
    protected boolean fire;
    public Color playerColor;

    public Player(Vec2 pos, float width, float height, BodyType bodyType, Scene scene) {
        super(pos, width, height, bodyType,scene);
        playerColor = new java.awt.Color((float) Math.random() * 0.1f, (float) Math.random() * 0.15f + 0.15f, (float) Math.random() * 0.75f + 0.25f);
        objectColor = playerColor;
        body.setFixedRotation(true);
        currentWeapon = new MachineGun(this);
    }

    public void handleInput() {

        Vec2 force = new Vec2();
        Vec2 Velocity = body.getLinearVelocity();

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) force = force.add(new Vec2(-1, 0));
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) force = force.add(new Vec2(1, 0));
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) force = force.add(new Vec2(0, 1));
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) force = force.add(new Vec2(0, -1));


        force.mul(speed);

        Velocity = Velocity.add(force);

        body.setLinearVelocity(Velocity);

        if(Mouse.isButtonDown(0) )currentWeapon.shoot();


    }





    @Override
    public void Render() {
        float[] rgbVal = new float[4];
        if (contact) {
            Color.red.getRGBColorComponents(rgbVal);
        } else {
            objectColor.getRGBColorComponents(rgbVal);
        }

        glColor3f(rgbVal[0], rgbVal[1], rgbVal[3]);
        super.Render();

    }

    @Override
    public void Update() {
        Vec2 playerPosition = new Vec2(body.getPosition().x, body.getPosition().y);

        if (playerPosition.x > GameWorld.covertedSize.x) {
            System.out.println("Outta here");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.RIGHT);
        }

        if (playerPosition.x < 0) {
            System.out.println("Outta here");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.LEFT);
        }
        if (playerPosition.y > GameWorld.covertedSize.y) {
            System.out.println("Outta here");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.UP);
        }
        if (playerPosition.y < 0) {
            System.out.println("Outta here");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.DOWN);
        }

    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void death(){
        System.out.println("I Died");
        GameWorld.Instance.setGameOver(true);
    }
}

