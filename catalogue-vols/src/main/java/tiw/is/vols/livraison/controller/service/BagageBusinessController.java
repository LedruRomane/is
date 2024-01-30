package tiw.is.vols.livraison.controller.service;

import tiw.is.vols.livraison.dao.CatalogueBagage;
import tiw.is.vols.livraison.dto.BagageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Bagage;

import java.util.Optional;

public class BagageBusinessController {
    private final CatalogueBagage dao;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets
     */
    public BagageBusinessController(CatalogueBagage dao) {
        this.dao = dao;
    }

    public void delivrer(BagageDTO dto) throws ResourceNotFoundException {
        Bagage bagage = Optional.ofNullable(dao.getBagageById(dto.volId(), dto.numero())).orElseThrow(
                () -> new ResourceNotFoundException("Le bagage " + dto.numero() + " du vol " + dto.volId() + " n'existe pas."));
        bagage.delivrer();
        dao.updateBagage(bagage);
    }

    public void recuperer(BagageDTO dto) throws ResourceNotFoundException, IllegalStateException {
        Bagage bagage = Optional.ofNullable(dao.getBagageById(dto.volId(), dto.numero())).orElseThrow(
                () -> new ResourceNotFoundException("Le bagage " + dto.numero() + " du vol " + dto.volId() + " n'existe pas."));
        if(!bagage.isDelivre())
            throw new IllegalStateException("Un bagage ne peut être récupéré avant d'être délivré.");
        bagage.recuperer();
        dao.updateBagage(bagage);
    }
}