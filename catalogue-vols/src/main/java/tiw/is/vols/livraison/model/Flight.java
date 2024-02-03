package tiw.is.vols.livraison.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Flight implements Serializable {
    @Serial
    private static final long serialVersionUID = 2149469184950107122L;
    @Id
    private String id;

    @ManyToOne
    private Company company;

    private String pointLivraisonBagages;

    private int nextNumeroBagage;

    private boolean livraisonEnCours;
    public Flight() {
    }

    public Flight(String id, Company company, String pointLivraisonBagages) {
        this.id = id;
        this.company = company;
        this.pointLivraisonBagages = pointLivraisonBagages;
        this.nextNumeroBagage = 1;
        this.livraisonEnCours = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPointLivraisonBagages() {
        return pointLivraisonBagages;
    }

    public void setPointLivraisonBagages(String pointLivraisonBagages) {
        this.pointLivraisonBagages = pointLivraisonBagages;
    }

    public void fermerLivraison() {
        this.livraisonEnCours = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id);
    }

    public boolean isLivraisonEnCours() {
        return livraisonEnCours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Créée un nouveau bagage rattaché à ce vol
     * @param poids le poids du bagage
     * @param passenger la référence au passager du bagage
     * @return le bagage créé.
     */
    public Baggage createBagage(float weight, String passenger) {
        Baggage b = new Baggage(this, nextNumeroBagage, weight, passenger);
        nextNumeroBagage++;
        return b;
    }
}
