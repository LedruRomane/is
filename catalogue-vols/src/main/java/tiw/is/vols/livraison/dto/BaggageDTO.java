package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Baggage;

/**
 * Data Transfer Object for Baggage Model.
 *
 * @param flightId  flight where the baggage is register.
 * @param numero    id baggage.
 * @param weight    Baggage's weight.
 * @param passenger Baggage's owner's name.
 */
public record BaggageDTO(String flightId, int numero, float weight, String passenger) {
    public static BaggageDTO fromBaggage(Baggage baggage) {
        return new BaggageDTO(baggage.getFlight().getId(), baggage.getNumero(), baggage.getWeight(), baggage.getPassenger());
    }
}