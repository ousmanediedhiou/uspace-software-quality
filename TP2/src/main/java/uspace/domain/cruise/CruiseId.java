package uspace.domain.cruise;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CruiseId implements Serializable {

    private String id;

    protected CruiseId() { }

    public CruiseId(String id) { this.id = id; }

    public String toString() { return id; }

    public int hashCode() { return id.hashCode(); }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        return id.equals(((CruiseId) obj).id);
    }
}
