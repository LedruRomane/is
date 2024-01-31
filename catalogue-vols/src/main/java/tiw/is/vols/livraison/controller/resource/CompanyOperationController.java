package tiw.is.vols.livraison.controller.resource;

import tiw.is.vols.livraison.dao.CatalogCompany;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Company;

import java.util.Collection;
import java.util.Optional;

public class CompanyOperationController {
    private final CatalogCompany dao;

    /**
     * Créée une instance du contrôleur qui utilisera le DAO passé
     * en argument pour gérer la persistence des objets.
     *
     * @param dao le DAO en charge de la gestion des objets
     */
    public CompanyOperationController(CatalogCompany dao) {
        this.dao = dao;
    }

    /**
     * Renvoie la collection de toutes compagnies
     *
     * @return toutes les compagnies
     */
    public Collection<Company> getCompanies() {
        return dao.getCompanies();
    }

    /**
     * Renvoie une compagnie en fonction de son id.
     *
     * @param id l'id de la compagnie cherchée
     * @return la compagnie trouvée ou null si aucune compagnie n'a été trouvée
     */
    public Company getCompany(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(dao.getCompany(id)).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + id + " n'existe pas.")
        );
    }

    /**
     * Crée une nouvelle compagnie
     *
     * @param company la compagnie à persister
     * @throws ResourceAlreadyExistsException si une compagnie avec le même id existe déjà
     */
    public void createCompany(Company company) throws ResourceAlreadyExistsException {
        if (dao.getCompany(company.getId()) != null)
            throw new ResourceAlreadyExistsException("Une compagnie avec l'ID " + company.getId() + " existe déjà.");
        dao.saveCompany(company);
    }

    /**
     * Supprime une compagnie et tous les vols qu'elle contient, et tous les bagages que contiennent ces vols.
     *
     * @param company la compagnie à supprimer
     * @throws ResourceNotFoundException si elle n'existe pas
     */
    public void deleteCompany(Company company) throws ResourceNotFoundException {
        if(!dao.deleteCompanyById(company.getId()))
            throw new ResourceNotFoundException("La compagnie " + company.getId() + " n'existe pas.");
    }
}
