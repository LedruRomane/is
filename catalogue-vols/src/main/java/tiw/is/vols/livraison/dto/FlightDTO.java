package tiw.is.vols.livraison.dto;

import tiw.is.vols.livraison.model.Flight;

public record FlightDTO(String id, String company, String pointLivraisonBagages) {
    public static FlightDTO fromFlight(Flight flight) {
        return new FlightDTO(flight.getId(), flight.getCompany().getId(), flight.getPointLivraisonBagages());
    }
}