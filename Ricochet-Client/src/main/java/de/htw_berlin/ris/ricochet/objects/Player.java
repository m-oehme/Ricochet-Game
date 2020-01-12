package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.RicochetGameGUI;
import de.htw_berlin.ris.ricochet.items.MachineGun;
import de.htw_berlin.ris.ricochet.items.Weapon;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.glColor3f;

public class Player extends EnemyPlayer implements Runnable{
    private boolean isAlive = true;

    private int ID;
    public float speed = 1.0f;
    public int health = 5;
    protected Weapon currentWeapon;
    protected boolean fire;
    public Color playerColor;

    private EnemyIndicator enemyDirection;

    public Player(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, Scene scene) {
        super(objectId, pos, width, height, bodyType,scene);
        playerColor = new java.awt.Color((float) Math.random() * 0.1f, (float) Math.random() * 0.15f + 0.15f, (float) Math.random() * 0.75f + 0.25f);
        objectColor = playerColor;
        currentWeapon = new MachineGun(this);
        name = "PlayerObject";
    }

    public void handleInput() {
        if (body == null) return;

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
    public void Init() {
        super.Init();

        setEnemyIndicatorAlive();
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
        super.Update();

//        if (!position.equals(playerPosition)) {
//        }
        ClientNetManager.get().sentMessage(new ObjectMoveMessage(ClientNetManager.get().getClientId(), this.getObjectId(), myScene.getLocation(), this.position));
    }

    @Override
    public void Destroy() {
        isAlive = false;
        super.Destroy();
    }

    @Override
    public void run() {
        while (isAlive) {
            updateIndicatorPosition();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateIndicatorPosition() {
        ArrayList<GameObject> enemyList = new ArrayList<>();
        GameWorld.Instance.getWorldScenes().values().forEach(scene -> {
            List<GameObject> collect = scene.getSceneObjectsDynamic().stream().filter(gameObject -> gameObject instanceof EnemyPlayer && !(gameObject instanceof Player))
                    .collect(Collectors.toList());
            enemyList.addAll(collect);
        });

        if (enemyList.size() > 0) {
            enemyList.stream()
                    .min(Comparator.comparing(gameObject -> gameObject.position.sub(position).lengthSquared()))
                    .ifPresent(gameObject -> {
                        Vec2 direction = gameObject.position.sub(position).mul(1 / gameObject.position.sub(position).normalize());

                        enemyDirection.setPositionUpdate(position.add(direction.mul(2)));
                    });
        } else {
            enemyDirection.setPositionUpdate(new Vec2(0,0));
        }
    }

    @Override
    public void switchScene(Vec2 position) {
        Vec2 convertedPos = GameWorld.getLocalCoordinates(position, myScene.getLocation());

        if (convertedPos.x > GameWorld.covertedSize.x) {
            log.debug("Player Scene switch to RIGHT");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.RIGHT);
        }

        if (convertedPos.x < 0) {
            log.debug("Player Scene switch to LEFT");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.LEFT);
        }
        if (convertedPos.y > GameWorld.covertedSize.y) {
            log.debug("Player Scene switch to UP");
            GameWorld.Instance.switchScene(GameWorld.switchDirection.UP);
        }
        if (convertedPos.y < 0) {
            log.debug("Player Scene switch to DOWN");
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

    public void setEnemyIndicatorAlive() {
        if (enemyDirection == null) {
            enemyDirection = new EnemyIndicator(position, myScene);
            enemyDirection.Init();

            RicochetGameGUI.get().getMainThreadPool().execute(this);
        }
    }
}

