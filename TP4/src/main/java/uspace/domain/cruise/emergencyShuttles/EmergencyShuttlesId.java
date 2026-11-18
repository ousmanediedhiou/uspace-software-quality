package uspace.domain.cruise.emergencyShuttles;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class EmergencyShuttlesId implements Serializable {
    public String id;

    public EmergencyShuttlesId()  {
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
        EmergencyShuttlesId other = (EmergencyShuttlesId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
