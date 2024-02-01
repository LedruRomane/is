package tiw.is.vols.livraison.controller.resource;

import tiw.is.vols.livraison.dao.CatalogueCompanie;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Compagnie;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class CompagnieOperationController {
    private final CatalogueCompanie dao;

    public Object process(String command,  Map<String, Object> params ) throws ResourceAlreadyExistsException {
        switch (command){
            case "createCompagnie":
                Compagnie compagnie = new Compagnie((String) params.get("id"));
                createCompagnie(compagnie);

                return compagnie.getId();
        }

        throw new RuntimeException("Command does not exists");
    }

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets
     */
    public CompagnieOperationController(CatalogueCompanie dao) {
        this.dao = dao;
    }

    /**
     * Renvoie la collection de toutes compagnies
     *
     * @return toutes les compagnies
     */
    public Collection<Compagnie> getCompagnies() {
        return dao.getCompagnies();
    }

    /**
     * Renvoie une compagnie en fonction de son id.
     *
     * @param id l'id de la compagnie cherchée
     * @return la compagnie trouvée ou null si aucune compagnie n'a été trouvée
     */
    public Compagnie getCompagnie(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(dao.getCompagnie(id)).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + id + " n'existe pas.")
        );
    }

    /**
     * Crée une nouvelle compagnie
     *
     * @param compagnie la compagnie à persister
     * @throws ResourceAlreadyExistsException si une compagnie avec le même id existe déjà
     */
    public void createCompagnie(Compagnie compagnie) throws ResourceAlreadyExistsException {
        if (dao.getCompagnie(compagnie.getId()) != null)
            throw new ResourceAlreadyExistsException("Une compagnie avec l'ID " + compagnie.getId() + " existe déjà.");
        dao.saveCompagnie(compagnie);
        //todo: ask how do I put this in private with unit tests using it...
    }

    /**
     * Supprime une compagnie et tous les vols qu'elle contient, et tous les bagages que contiennent ces vols.
     *
     * @param compagnie la compagnie à supprimer
     * @throws ResourceNotFoundException si elle n'existe pas
     */
    public void deleteCompagnie(Compagnie compagnie) throws ResourceNotFoundException {
        if(!dao.deleteCompagnieById(compagnie.getId()))
            throw new ResourceNotFoundException("La compagnie " + compagnie.getId() + " n'existe pas.");
    }
}
