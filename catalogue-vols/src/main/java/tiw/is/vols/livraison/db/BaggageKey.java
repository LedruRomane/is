package tiw.is.vols.livraison.db;

import tiw.is.vols.livraison.model.Vol;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class BagageKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1779876715614136070L;
    private Vol vol;
    private int numero;

    public BagageKey() {
    }

    public BagageKey(Vol vol, int numero) {
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
        BagageKey bagageKey = (BagageKey) o;
        return numero == bagageKey.numero && vol.equals(bagageKey.vol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vol, numero);
    }
}
