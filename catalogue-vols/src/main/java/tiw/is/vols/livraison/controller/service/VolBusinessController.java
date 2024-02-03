package tiw.is.vols.livraison.controller.service;

import tiw.is.vols.livraison.dao.CatalogueBagage;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;
import java.util.Optional;

public class VolBusinessController {
    private final CatalogueVol dao;
    private final CatalogueBagage daoBagage;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets
     * @param daoBagage le dao en charge des babages
     */
    public VolBusinessController(CatalogueVol dao, CatalogueBagage daoBagage) {
        this.dao = dao;
        this.daoBagage = daoBagage;
    }



    public void fermerLivraison(FlightDTO dto) throws ResourceNotFoundException {
        Flight flight = Optional.ofNullable(dao.getVol(dto.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + dto.id() + " n'existe pas.")
        );
        flight.fermerLivraison();
        dao.saveVol(flight);
    }

    public Collection<BaggageDTO> bagagesPerdus(FlightDTO dto) throws ResourceNotFoundException {
        Flight flight = Optional.ofNullable(dao.getVol(dto.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + dto.id() + " n'existe pas.")
        );
        if(flight.isLivraisonEnCours())
            throw new IllegalStateException("Impossible de lister les bagages perdus tant que la livraison est en cours.");
        return daoBagage.getBagagesPerdusByVolId(dto.id()).stream().map(BaggageDTO::fromBaggage).toList();
    }

    public Collection<BaggageDTO> bagagesNonRecuperes(FlightDTO dto) throws ResourceNotFoundException {
        Flight flight = Optional.ofNullable(dao.getVol(dto.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + dto.id() + " n'existe pas.")
        );
        if(flight.isLivraisonEnCours())
            throw new IllegalStateException("Impossible de lister les bagages non récupérés tant que la livraison est en cours.");
        return daoBagage.getBagagesNonRecuperesByVolId(dto.id()).stream().map(BaggageDTO::fromBaggage).toList();
    }
}