package tiw.is.vols.livraison.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import tiw.is.vols.livraison.db.BagageKey;

import java.util.Objects;

@Entity
@IdClass(BagageKey.class)
public class Bagage {
    @Id
    @ManyToOne
    private Vol vol;
    @Id
    private int numero;
    private float poids;
    private String passager;
    private boolean delivre, recupere;

    public Bagage() {
    }

    public Bagage(Vol vol, int numero, float poids, String passager) {
        this.vol = vol;
        this.numero = numero;
        this.poids = poids;
        this.passager = passager;
        this.delivre = false;
        this.recupere = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bagage bagage = (Bagage) o;
        return numero == bagage.numero && vol.equals(bagage.vol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vol, numero);
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public String getPassager() {
        return passager;
    }

    public void setPassager(String passager) {
        this.passager = passager;
    }

    public boolean isDelivre() {
        return delivre;
    }

    public boolean isRecupere() {
        return recupere;
    }

    // Opérations métier

    public void delivrer() {
        this.delivre = true;
    }

    public void recuperer() {
        this.recupere = true;
    }
}
