package tiw.is.vols.livraison.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Compagnie implements Serializable {
    @Serial
    private static final long serialVersionUID = 3885491406861965219L;
    @Id
    private String id;

    public Compagnie() {
    }

    public Compagnie(String id) {
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
        Compagnie compagnie = (Compagnie) o;
        return Objects.equals(id, compagnie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
