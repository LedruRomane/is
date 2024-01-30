package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Bagage;

public record BagageDTO(String volId, int numero, float poids, String passager) {
    public static BagageDTO fromBagage(Bagage bagage) {
        return new BagageDTO(bagage.getVol().getId(), bagage.getNumero(), bagage.getPoids(), bagage.getPassager());
    }
}