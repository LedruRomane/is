package tiw.is.vols.livraison.controller.service;

import tiw.is.vols.livraison.dao.CatalogueBagage;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Baggage;

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

    public void delivrer(BaggageDTO dto) throws ResourceNotFoundException {
        Baggage baggage = Optional.ofNullable(dao.getBagageById(dto.volId(), dto.numero())).orElseThrow(
                () -> new ResourceNotFoundException("Le baggage " + dto.numero() + " du vol " + dto.volId() + " n'existe pas."));
        baggage.delivrer();
        dao.updateBagage(baggage);
    }

    public void recuperer(BaggageDTO dto) throws ResourceNotFoundException, IllegalStateException {
        Baggage baggage = Optional.ofNullable(dao.getBagageById(dto.volId(), dto.numero())).orElseThrow(
                () -> new ResourceNotFoundException("Le baggage " + dto.numero() + " du vol " + dto.volId() + " n'existe pas."));
        if(!baggage.isDelivre())
            throw new IllegalStateException("Un baggage ne peut être récupéré avant d'être délivré.");
        baggage.recuperer();
        dao.updateBagage(baggage);
    }
}