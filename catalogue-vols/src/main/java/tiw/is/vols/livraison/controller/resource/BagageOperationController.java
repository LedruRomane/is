package tiw.is.vols.livraison.controller.resource;

import tiw.is.vols.livraison.dao.CatalogueBagage;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;
import java.util.Optional;

public class BagageOperationController {
    private final CatalogueBagage dao;
    private final CatalogueVol daoVol;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets Baggage
     * @param daoVol le DAO en charge de la gestion des objets Flight
     */
    public BagageOperationController(CatalogueBagage dao, CatalogueVol daoVol) {
        this.dao = dao;
        this.daoVol = daoVol;
    }

    /**
     * Renvoie l'ensemble des bagages présents en base.
     *
     * @return la liste contenant des DTO de bagages
     */
    public Collection<BaggageDTO> getBagages() {
        return dao.getBagages().stream().map(BaggageDTO::fromBaggage).toList();
    }

    /**
     * Renvoie un bagage cherché par identifiant de vol et numéro
     *
     * @param dto Un BaggageDTO comportant l'identifiant du vol auquel ce bagage est rattaché et le numéro du bagage
     * @return le bagage cherché
     * @throws ResourceNotFoundException si aucun bagage trouvé
     */
    public BaggageDTO getBagage(BaggageDTO dto) throws ResourceNotFoundException {
        return BaggageDTO.fromBaggage(Optional.ofNullable(dao.getBagageById(dto.flightId(), dto.numero())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + dto.flightId() + " n'existe pas.")
        ));
    }

    /**
     * Crée un bagage
     *
     * @param dto le DTO correspondant au bagage - ATTENTION : le numéro est (ré)initialisé par Flight.createBagage()
     */
    public BaggageDTO createBagage(BaggageDTO dto) throws ResourceNotFoundException {
        Flight flight = Optional.ofNullable(daoVol.getVol(dto.flightId())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + dto.flightId() + " n'existe pas.")
        );
        return BaggageDTO.fromBaggage(dao.createBagage(flight, dto.weight(), dto.passenger()));
    }

    /**
     * Supprime un bagage
     *
     * @param dto Un BaggageDTO comportant l'identifiant du vol auquel ce bagage est rattaché et le numéro du bagage
     * @throws ResourceNotFoundException s'il n'a pas été trouvé
     */
    public void deleteBagage(BaggageDTO dto) throws ResourceNotFoundException {
        if(!dao.deleteBagageById(dto.flightId(), dto.numero()))
            throw new ResourceNotFoundException("Le bagage du vol " + dto.flightId() + " et avec le numéro " + dto.numero() + " n'existe pas.");
    }
}
