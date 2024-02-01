package tiw.is.vols.livraison.controller.resource;

import tiw.is.vols.livraison.dao.CatalogueBagage;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.dto.BagageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;
import java.util.Optional;

public class BagageOperationController {
    private final CatalogueBagage dao;
    private final CatalogueVol daoVol;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets Bagage
     * @param daoVol le DAO en charge de la gestion des objets Vol
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
    public Collection<BagageDTO> getBagages() {
        return dao.getBagages().stream().map(BagageDTO::fromBagage).toList();
    }

    /**
     * Renvoie un bagage cherché par identifiant de vol et numéro
     *
     * @param dto Un BagageDTO comportant l'identifiant du vol auquel ce bagage est rattaché et le numéro du bagage
     * @return le bagage cherché
     * @throws ResourceNotFoundException si aucun bagage trouvé
     */
    public BagageDTO getBagage(BagageDTO dto) throws ResourceNotFoundException {
        return BagageDTO.fromBagage(Optional.ofNullable(dao.getBagageById(dto.volId(), dto.numero())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + dto.volId() + " n'existe pas.")
        ));
    }

    /**
     * Crée un bagage
     *
     * @param dto le DTO correspondant au bagage - ATTENTION : le numéro est (ré)initialisé par Vol.createBagage()
     */
    public BagageDTO createBagage(BagageDTO dto) throws ResourceNotFoundException {
        Vol vol = Optional.ofNullable(daoVol.getVol(dto.volId())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + dto.volId() + " n'existe pas.")
        );
        return BagageDTO.fromBagage(dao.createBagage(vol, dto.poids(), dto.passager()));
    }

    /**
     * Supprime un bagage
     *
     * @param dto Un BagageDTO comportant l'identifiant du vol auquel ce bagage est rattaché et le numéro du bagage
     * @throws ResourceNotFoundException s'il n'a pas été trouvé
     */
    public void deleteBagage(BagageDTO dto) throws ResourceNotFoundException {
        if(!dao.deleteBagageById(dto.volId(), dto.numero()))
            throw new ResourceNotFoundException("Le bagage du vol " + dto.volId() + " et avec le numéro " + dto.numero() + " n'existe pas.");
    }
}
