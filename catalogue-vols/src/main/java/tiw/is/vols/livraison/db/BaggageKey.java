package tiw.is.vols.livraison.db;

import tiw.is.vols.livraison.model.Vol;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class BaggageKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1779876715614136070L;
    private Vol vol;
    private int numero;

    public BaggageKey() {
    }

    public BaggageKey(Vol vol, int numero) {
        this.vol = vol;
        this.numero = numero;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaggageKey baggageKey = (BaggageKey) o;
        return numero == baggageKey.numero && vol.equals(baggageKey.vol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vol, numero);
    }
}
