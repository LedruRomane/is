package tiw.is.vols.livraison.db;

import tiw.is.vols.livraison.model.Flight;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class BaggageKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1779876715614136070L;
    private Flight flight;
    private int numero;

    public BaggageKey() {
    }

    public BaggageKey(Flight flight, int numero) {
        this.flight = flight;
        this.numero = numero;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaggageKey baggageKey = (BaggageKey) o;
        return numero == baggageKey.numero && flight.equals(baggageKey.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight, numero);
    }
}
