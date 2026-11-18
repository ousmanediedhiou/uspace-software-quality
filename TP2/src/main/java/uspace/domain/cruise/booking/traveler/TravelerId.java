package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class TravelerId implements Serializable {

    private String id;

    public TravelerId() {
        this.id = UUID.randomUUID().toString();
    }

    public TravelerId(String id) {
        this.id = id;
    }

    public String toString() { return id; }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        return id.equals(((TravelerId) obj).id);
    }

    public int hashCode() { return id.hashCode(); }
}
