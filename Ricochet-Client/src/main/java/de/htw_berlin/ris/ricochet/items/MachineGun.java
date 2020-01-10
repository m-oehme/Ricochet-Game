package de.htw_berlin.ris.ricochet.items;

import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.objects.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.input.Mouse;

public class MachineGun extends  Weapon {


    public MachineGun(Player player){
        fireRate = 100;
        magazineSize = 10;
        reloadTime = 5000;
        bulletSize = 0.25f;
        shotSpeed = 10;
        this.player = player;

    }

    @Override
    public void shoot(){
        if (reloading|| coolDown) return;
        super.shoot();

        Vec2 playerPosition = new Vec2(player.body.getPosition().x, player.body.getPosition().y);
        Vec2 mousePosition = new Vec2(Mouse.getX(), Mouse.getY()).mul(1 / 30f);
        Vec2 shotDir = mousePosition.sub(playerPosition);
        Vec2 shotOffset = shotDir;
        shotDir.normalize();
        shotDir = shotDir.mul(shotSpeed * 10);
        ClientNetManager.get().sentMessage(new ObjectCreateMessage(ClientNetManager.get().getClientId(), null, new SBullet(playerPosition.add(shotOffset.mul(1.5f)),shotDir)));




    }
}
