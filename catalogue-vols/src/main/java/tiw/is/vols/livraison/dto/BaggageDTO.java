package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Baggage;

public record BaggageDTO(String flightId, int numero, float weight, String passenger) {
    public static BaggageDTO fromBaggage(Baggage baggage) {
        return new BaggageDTO(baggage.getFlight().getId(), baggage.getNumero(), baggage.getWeight(), baggage.getPassenger());
    }
}