package de.htw_berlin.ris.ricochet.world.generation;

import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;

import java.util.List;

public interface WorldGenerator {
    List<SGameObject> generateWorld(int width, int height);
}
