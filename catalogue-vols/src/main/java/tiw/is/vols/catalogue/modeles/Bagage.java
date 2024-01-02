package tiw.is.vols.catalogue.modeles;

import jakarta.persistence.*;

@Entity
@IdClass(BagageKey.class)
public class Bagage {
    @Id
    @ManyToOne
    private Vol vol;

    @Id
    private int numero;

    private double poids;

    private String passagerRef;

    public Bagage() {}

    public Bagage(BagageKey bagageKey, double poids, String passagerRef) {
        this.vol = bagageKey.vol;
        this.numero = bagageKey.numero;
        this.poids = poids;
        this.passagerRef = passagerRef;
    }

    public double getPoids() {
        return poids;
    }

    public String getPassagerRef() {
        return passagerRef;
    }

}
