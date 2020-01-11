package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import javafx.geometry.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class WallPrefab {
    private static Logger log = LogManager.getLogger();

    // TODO : Add halfWall and Quaterwall
    public WallPrefab(WallPrefabConfig.PrefabType type, WallPrefabConfig.PrefabPosition position, Scene whichScene) {
        Vec2 Postion1 = new Vec2();
        Vec2 Postion2 = new Vec2();

        Vec2 sceneOffset = new Vec2(whichScene.getLocation().x * GameWorld.covertedSize.x, whichScene.getLocation().y * GameWorld.covertedSize.y);


        boolean horizontal = false;

        switch (type) {


            case FullWall:

                switch (position){
                    case Left:

                        Postion1 = new Vec2 (0,GameWorld.covertedSize.y/2);
                        log.debug("LEFT Scene: " + whichScene.getLocation() + " Pos: " + Postion1 +  " Offset: " + sceneOffset + " NewPos: " + Postion1.add(sceneOffset));
                        break;

                    case Right:

                        Postion1 = new Vec2 (GameWorld.covertedSize.x,GameWorld.covertedSize.y/2);
                        log.debug("RIGHT Scene: " + whichScene.getLocation() + " Pos: " + Postion1 +  " Offset: " + sceneOffset + " NewPos: " + Postion1.add(sceneOffset));
                        break;
                    case Top:

                        Postion1 = new Vec2 (GameWorld.covertedSize.x/2,GameWorld.covertedSize.y);
                        horizontal = true;
                        log.debug("TOP Scene: " + whichScene.getLocation() + " Pos: " + Postion1 +  " Offset: " + sceneOffset + " NewPos: " + Postion1.add(sceneOffset));

                        break;

                    case Bottom:
                        Postion1 = new Vec2 (GameWorld.covertedSize.x/2,0);
                        horizontal = true;
                        log.debug("BOTTOM Scene: " + whichScene.getLocation() + " Pos: " + Postion1 +  " Offset: " + sceneOffset);
                        break;
                }

                GameObject Wall =  new GameObject(
                        null,
                        Postion1.add(sceneOffset),
                        (horizontal) ? GameWorld.covertedSize.x/2:GameWorld.covertedSize.x/30,
                        (horizontal) ? GameWorld.covertedSize.y/30 : GameWorld.covertedSize.y/2,BodyType.STATIC,
                        1f,
                        0.5f,
                        whichScene );

                break;

            case DoorWall:


                switch (position){
                    case Left:

                        Postion1 = new Vec2 (0,(GameWorld.covertedSize.y/6));
                        Postion2 = new Vec2 (0, (GameWorld.covertedSize.y/6)*5);
                        break;

                    case Right:

                        Postion1 = new Vec2 (GameWorld.covertedSize.x,(GameWorld.covertedSize.y/6));
                        Postion2 = new Vec2 (GameWorld.covertedSize.x, (GameWorld.covertedSize.y/6)*5);
                        break;
                    case Top:

                        Postion1 = new Vec2 (GameWorld.covertedSize.x/6,GameWorld.covertedSize.y);
                        Postion2 = new Vec2 (GameWorld.covertedSize.x/6*5, GameWorld.covertedSize.y);
                        horizontal = true;
                        break;

                    case Bottom:
                        Postion1 = new Vec2 (GameWorld.covertedSize.x/6,0);
                        Postion2 = new Vec2 (GameWorld.covertedSize.x/6*5, 0);
                        horizontal = true;
                        break;
                }
                GameObject wallBottom =  new GameObject(null, Postion1.add(sceneOffset), (horizontal) ? GameWorld.covertedSize.x/6 :GameWorld.covertedSize.x/30,  (horizontal) ? GameWorld.covertedSize.y/30 : GameWorld.covertedSize.y/6,BodyType.STATIC, 1f, 0.5f,whichScene );
                GameObject wallTop =  new GameObject(null, Postion2.add(sceneOffset), (horizontal) ? GameWorld.covertedSize.x/6:GameWorld.covertedSize.x/30,  (horizontal) ? GameWorld.covertedSize.y/30 : GameWorld.covertedSize.y/6,BodyType.STATIC, 1f, 0.5f,whichScene );
                break;



        }


    }


}
