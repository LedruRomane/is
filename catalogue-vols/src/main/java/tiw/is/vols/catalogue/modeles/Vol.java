package tiw.is.vols.catalogue.modeles;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Vol {
    @Id
    private String id;

    public Vol() {
    }

    public Vol(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vol vol = (Vol) o;
        return Objects.equals(id, vol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
