package tiw.is.vols.catalogue.modeles;

import java.io.Serializable;
import java.util.Objects;

public class BagageKey implements Serializable {
    public Vol vol;
    public int numero;

    public BagageKey() {}

    public BagageKey(Vol vol, int numero) {
        this.vol = vol;
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BagageKey bagageKey = (BagageKey) o;
        return Objects.equals(vol, bagageKey.vol) && Objects.equals(numero, bagageKey.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vol, numero);
    }
}
