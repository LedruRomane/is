package tiw.is.vols.livraison.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import tiw.is.vols.livraison.db.BaggageKey;

import java.util.Objects;

@Entity
@IdClass(BaggageKey.class)
public class Baggage {
    @Id
    @ManyToOne
    private Flight flight;
    @Id
    private int numero;
    private float weight;
    private String passenger;
    private boolean delivre;
    private boolean recupere;

    public Baggage() {
    }

    public Baggage(Flight flight, int numero, float weight, String passenger) {
        this.flight = flight;
        this.numero = numero;
        this.weight = weight;
        this.passenger = passenger;
        this.delivre = false;
        this.recupere = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Baggage baggage = (Baggage) o;
        return numero == baggage.numero && flight.equals(baggage.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight, numero);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
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
