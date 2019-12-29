package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

import java.io.Serializable;
import java.util.Objects;

public class ObjectId implements Comparable<ObjectId>, Serializable {
    private String objectId;

    public ObjectId(SGameObject o) {
        objectId = Integer.toHexString(o.hashCode()) + Long.toHexString((long) (Math.random() * Long.MAX_VALUE));
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public int compareTo(ObjectId o) {
        return o == null ? -1 : objectId.compareTo(o.objectId);
    }

    @Override
    public String toString() {
        return objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ObjectId) {
            return this.objectId.equals(((ObjectId) o).getObjectId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
