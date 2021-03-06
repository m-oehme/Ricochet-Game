package de.htw_berlin.ris.ricochet.items;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.objects.GameObject;
import de.htw_berlin.ris.ricochet.objects.Player;
import de.htw_berlin.ris.ricochet.objects.shared.SBullet;
import org.jbox2d.common.Vec2;
import org.lwjgl.input.Mouse;

public class Pistol extends Weapon {


    public Pistol(GameObject owner){
        fireRate = 300;
        magazineSize = 6;
        bulletsInMagazine = magazineSize;
        reloadTime = 3000;
        shotSpeed = 10;
        this.owner = owner;

    }


    public Pistol(GameObject owner, int fireRate){
        this.fireRate = fireRate;
        magazineSize = 6;
        bulletsInMagazine = magazineSize;
        reloadTime = 3000;
        shotSpeed = 10;
        this.owner = owner;

    }

    @Override
    public void shoot(){
        if (reloading|| coolDown) return;
        super.shoot();

        Vec2 playerPosition = new Vec2(owner.body.getPosition().x, owner.body.getPosition().y);
        Vec2 mousePosition = new Vec2(Mouse.getX(), Mouse.getY()).mul(1 / 30f);

        Vec2 sceneOffset = new Vec2(owner.myScene.getLocation().x * GameWorld.covertedSize.x, owner.myScene.getLocation().y * GameWorld.covertedSize.y);
        Vec2 shotDir = mousePosition.add(sceneOffset).sub(playerPosition);
        shotDir.normalize();

        ClientNetManager.get().sentMessage(new ObjectCreateMessage(ClientNetManager.get().getClientId(), null, new SBullet(
                GameWorld.Instance.getCurrentScene().getLocation(),
                playerPosition,
                shotDir,
                0.25f,
                0.25f)));


    }

    @Override
    public void shoot(Vec2 Dir){
        if (reloading|| coolDown) return;
        super.shoot();
        Vec2 shotDir = Dir;
        shotDir.normalize();
        Vec2 playerPosition = new Vec2(owner.body.getPosition().x, owner.body.getPosition().y);
        ClientNetManager.get().sentMessage(new ObjectCreateMessage(ClientNetManager.get().getClientId(), null, new SBullet(
                GameWorld.Instance.getCurrentScene().getLocation(),
                playerPosition,
                shotDir,
                0.25f,
                0.25f)));


    }
}
