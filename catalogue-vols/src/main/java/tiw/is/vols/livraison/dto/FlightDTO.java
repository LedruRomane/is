package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Flight;

/**
 * Data Transfer Object for Flight Model.
 *
 * @param id                    ID du vol.
 * @param company               Companie du vol.
 * @param pointLivraisonBagages Nom de la ville de l'aéroport.
 */
public record FlightDTO(String id, String company, String pointLivraisonBagages) {
    public static FlightDTO fromFlight(Flight flight) {
        return new FlightDTO(flight.getId(), flight.getCompany().getId(), flight.getPointLivraisonBagages());
    }
}