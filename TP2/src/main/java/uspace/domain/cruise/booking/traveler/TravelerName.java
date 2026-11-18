package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.Embeddable;

@Embeddable
public class TravelerName {
    private String name;

    protected TravelerName() { }

    public TravelerName(String name) {
        this.name = name;
    }

    public String toString() { return name; }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        return name.equals(((TravelerName) obj).name);
    }
}
