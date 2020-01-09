package de.htw_berlin.ris.ricochet.objects.shared;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import org.jbox2d.common.Vec2;

public class SPlayer extends SGameObject {
    private ClientId clientId;
    public SPlayer(ClientId clientId, Vec2 scene, Vec2 position) {
        super(scene, position);
        this.clientId = clientId;
    }


    public ClientId getClientId() {
        return clientId;
    }
}
