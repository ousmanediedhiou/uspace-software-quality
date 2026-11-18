package uspace.domain.cruise.booking;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class BookingId implements Serializable {

    private String id;

    protected BookingId() {
        this.id = UUID.randomUUID().toString();
    }

    public BookingId(String id) {
        this.id = id;
    }

    public String toString() { return id; }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        return id.equals(((BookingId) obj).id);
    }

    public int hashCode() { return id.hashCode(); }
}
