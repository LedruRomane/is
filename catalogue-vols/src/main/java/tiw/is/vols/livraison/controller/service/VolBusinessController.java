package tiw.is.vols.livraison.controller.service;

import tiw.is.vols.livraison.dao.CatalogueBagage;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.dto.BagageDTO;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Vol;

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



    public void fermerLivraison(VolDTO dto) throws ResourceNotFoundException {
        Vol vol = Optional.ofNullable(dao.getVol(dto.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + dto.id() + " n'existe pas.")
        );
        vol.fermerLivraison();
        dao.saveVol(vol);
    }

    public Collection<BagageDTO> bagagesPerdus(VolDTO dto) throws ResourceNotFoundException {
        Vol vol = Optional.ofNullable(dao.getVol(dto.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + dto.id() + " n'existe pas.")
        );
        if(vol.isLivraisonEnCours())
            throw new IllegalStateException("Impossible de lister les bagages perdus tant que la livraison est en cours.");
        return daoBagage.getBagagesPerdusByVolId(dto.id()).stream().map(BagageDTO::fromBagage).toList();
    }

    public Collection<BagageDTO> bagagesNonRecuperes(VolDTO dto) throws ResourceNotFoundException {
        Vol vol = Optional.ofNullable(dao.getVol(dto.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + dto.id() + " n'existe pas.")
        );
        if(vol.isLivraisonEnCours())
            throw new IllegalStateException("Impossible de lister les bagages non récupérés tant que la livraison est en cours.");
        return daoBagage.getBagagesNonRecuperesByVolId(dto.id()).stream().map(BagageDTO::fromBagage).toList();
    }
}