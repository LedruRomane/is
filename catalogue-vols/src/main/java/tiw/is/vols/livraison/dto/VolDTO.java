package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Vol;

public record VolDTO(String id, String compagnie, String pointLivraisonBagages) {
    public static VolDTO fromVol(Vol vol) {
        return new VolDTO(vol.getId(), vol.getCompagnie().getId(), vol.getPointLivraisonBagages());
    }
}