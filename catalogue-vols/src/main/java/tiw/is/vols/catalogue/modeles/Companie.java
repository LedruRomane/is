package tiw.is.vols.catalogue.modeles;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Companie {
    @Id
    private String id;

    public Companie() {
    }

    public Companie(String id) {
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
        Companie companie = (Companie) o;
        return Objects.equals(id, companie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
