package tiw.is.vols.livraison.controller.resource;

import tiw.is.vols.livraison.dao.CatalogueCompanie;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Compagnie;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;
import java.util.Optional;

public class VolOperationController {
    private final CatalogueVol dao;
    private final CatalogueCompanie daoCompanie;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets
     * @param daoCompanie le DAO en charge des objets Companie
     */
    public VolOperationController(CatalogueVol dao,
                                  CatalogueCompanie daoCompanie) {
        this.dao = dao;
        this.daoCompanie = daoCompanie;
    }

    /**
     * Renvoie l'ensemble des vols présents en base.
     *
     * @return la liste contenant des DTO de vols
     */
    public Collection<VolDTO> getVols() {
        return dao.getVols().stream().map(VolDTO::fromVol).toList();
    }

    /**
     * Renvoie un vol en fonction de son id
     *
     * @param dto le dto du vol cherché
     * @return le vol ou null s'il n'a pas été trouvé
     */
    public VolDTO getVol(VolDTO dto) throws ResourceNotFoundException {
        String id = dto.id();
        Vol vol = Optional.ofNullable(dao.getVol(id)).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + id + " n'existe pas.")
        );
        return VolDTO.fromVol(vol);
    }

    /**
     * Crée ou modifie un vol.
     *
     * @param voldto le DTO du vol à sauvegarder
     */
    public void updateVol(VolDTO voldto) throws ResourceNotFoundException {
        Compagnie compagnie = Optional.ofNullable(daoCompanie.getCompagnie(voldto.compagnie())).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + voldto.compagnie() + " n'existe pas.")
        );
        dao.saveVol(new Vol(voldto.id(), compagnie, voldto.pointLivraisonBagages()));
    }

    /**
     * Supprime un vol
     *
     * @param volDto le vol à supprimer
     * @throws ResourceNotFoundException s'il n'existe pas
     */
    public void deleteVol(VolDTO volDto) throws ResourceNotFoundException {
        if(!dao.deleteVolById(volDto.id()))
            throw new ResourceNotFoundException("Le vol " + volDto.id() + " n'existe pas.");
    }
}
