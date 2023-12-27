package tiw.is.vols.catalogue.modeles;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class Vol {
    @Id
    private String id;

    @ManyToOne
    private Companie companie;

    private boolean isDepart;

    private String pointLivraisonBagages;

    public Vol() {
    }

    public Vol(String id, Companie companie, boolean isDepart, String pointLivraisonBagages) {
        this.id = id;
        this.companie = companie;
        this.isDepart = isDepart;
        this.pointLivraisonBagages = pointLivraisonBagages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Companie getCompanie() {
        return companie;
    }

    public void setCompanie(Companie companie) {
        this.companie = companie;
    }

    public boolean isDepart() {
        return isDepart;
    }

    public void setDepart(boolean depart) {
        isDepart = depart;
    }

    public String getPointLivraisonBagages() {
        return pointLivraisonBagages;
    }

    public void setPointLivraisonBagages(String pointLivraisonBagages) {
        this.pointLivraisonBagages = pointLivraisonBagages;
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
