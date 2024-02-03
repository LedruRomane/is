package tiw.is.vols.livraison.controller.resource;

import tiw.is.vols.livraison.dao.CatalogCompany;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;
import java.util.Optional;

public class VolOperationController {
    private final CatalogueVol dao;
    private final CatalogCompany daoCompanie;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets
     * @param daoCompanie le DAO en charge des objets Companie
     */
    public VolOperationController(CatalogueVol dao,
                                  CatalogCompany daoCompanie) {
        this.dao = dao;
        this.daoCompanie = daoCompanie;
    }

    /**
     * Renvoie l'ensemble des vols présents en base.
     *
     * @return la liste contenant des DTO de vols
     */
    public Collection<FlightDTO> getVols() {
        return dao.getVols().stream().map(FlightDTO::fromFlight).toList();
    }

    /**
     * Renvoie un vol en fonction de son id
     *
     * @param dto le dto du vol cherché
     * @return le vol ou null s'il n'a pas été trouvé
     */
    public FlightDTO getVol(FlightDTO dto) throws ResourceNotFoundException {
        String id = dto.id();
        Flight flight = Optional.ofNullable(dao.getVol(id)).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + id + " n'existe pas.")
        );
        return FlightDTO.fromFlight(flight);
    }

    /**
     * Crée ou modifie un vol.
     *
     * @param voldto le DTO du vol à sauvegarder
     */
    public void updateVol(FlightDTO voldto) throws ResourceNotFoundException {
        Company company = Optional.ofNullable(daoCompanie.getCompany(voldto.company())).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + voldto.company() + " n'existe pas.")
        );
        dao.saveVol(new Flight(voldto.id(), company, voldto.pointLivraisonBagages()));
    }

    /**
     * Supprime un vol
     *
     * @param flightDto le vol à supprimer
     * @throws ResourceNotFoundException s'il n'existe pas
     */
    public void deleteVol(FlightDTO flightDto) throws ResourceNotFoundException {
        if(!dao.deleteVolById(flightDto.id()))
            throw new ResourceNotFoundException("Le vol " + flightDto.id() + " n'existe pas.");
    }
}
