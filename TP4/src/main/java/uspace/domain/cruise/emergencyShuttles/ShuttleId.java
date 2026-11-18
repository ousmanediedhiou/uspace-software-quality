package uspace.domain.cruise.emergencyShuttles;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ShuttleId implements Serializable {
    private String id;

    public ShuttleId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ShuttleId other = (ShuttleId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
