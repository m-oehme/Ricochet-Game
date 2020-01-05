package de.htw_berlin.ris.ricochet.items;

import de.htw_berlin.ris.ricochet.objects.Bullet;
import de.htw_berlin.ris.ricochet.objects.Player;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.input.Mouse;

public class Pistol extends Weapon{


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
        Bullet Bullet = new Bullet(playerPosition.add((shotDir.mul(1.5f))), 0.25f, 0.25f, BodyType.DYNAMIC);
        shotDir = shotDir.mul(shotSpeed * 10);
        Bullet.body.applyForce(shotDir, Bullet.body.getPosition());

    }
}
