package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Baggage;

public record BaggageDTO(String volId, int numero, float poids, String passager) {
    public static BaggageDTO fromBaggage(Baggage baggage) {
        return new BaggageDTO(baggage.getVol().getId(), baggage.getNumero(), baggage.getPoids(), baggage.getPassager());
    }
}