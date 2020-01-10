package de.htw_berlin.ris.ricochet.items;

import de.htw_berlin.ris.ricochet.objects.Player;

import java.util.Timer;
import java.util.TimerTask;

public class Weapon {

    protected boolean reloading,coolDown;
    protected float shotSpeed = 1.0f;
    protected Player player;
    protected int reloadTime;
    protected int magazineSize;
    protected int fireRate;
    protected int bulletsInMagazine;
    protected Timer t = new Timer();
 // TODO :: GUI for bullets etc.
    public  void shoot(){
        if (bulletsInMagazine<=0){
            reloading = true;
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    completeReload();
                }
            }, reloadTime);
        }else{
            coolDown = true;
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    completeCoolDown();
                }
            }, fireRate);
        }



    }

    private  void completeReload(){
        reloading = false;
        bulletsInMagazine = magazineSize;
    }

    private  void completeCoolDown(){
        coolDown = false;
        bulletsInMagazine--;
    }




}
