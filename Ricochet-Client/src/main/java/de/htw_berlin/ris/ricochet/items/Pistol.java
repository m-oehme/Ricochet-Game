package de.htw_berlin.ris.ricochet.items;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.shared.SBullet;
import org.jbox2d.common.Vec2;
import org.lwjgl.input.Mouse;

public class Pistol extends Weapon {


    public Pistol(Player player){
        fireRate = 300;
        magazineSize = 6;
        reloadTime = 3000;
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
        shotDir.normalize();

        ClientNetManager.get().sentMessage(new ObjectCreateMessage(ClientNetManager.get().getClientId(), null, new SBullet(GameWorld.Instance.getCurrentScene().getLocation(), playerPosition, shotDir)));


    }
}
