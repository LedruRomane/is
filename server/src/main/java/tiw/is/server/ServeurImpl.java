package tiw.is.server;

import jakarta.persistence.EntityManager;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.controller.resource.CompagnieOperationController;
import tiw.is.vols.livraison.dao.CatalogueCompanie;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.model.Compagnie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;

    private final CatalogueCompanie catalogueCompanie;

    private final CompagnieOperationController compagnieOperationController;

    private static JsonFormatter<Compagnie> compagnieFormatter;

    public ServeurImpl() throws SQLException {

        this.picoContainer = new PicoBuilder(new ConstructorInjection()).withCaching().build();

        EntityManager em = PersistenceManager.createEntityManagerFactory().createEntityManager();
        catalogueCompanie = new CatalogueCompanie(em);
        compagnieOperationController = new CompagnieOperationController(catalogueCompanie);

        picoContainer.addComponent(em);

        picoContainer.addComponent(catalogueCompanie);
        compagnieFormatter = new JsonFormatter<>();
    }

    public CompagnieOperationController getCompagnieOperationController() {
        return compagnieOperationController;
    }


    public CatalogueCompanie getCatalogueCompanie() {
        return catalogueCompanie;
    }

    public MutablePicoContainer getPicoContainer() {
        return picoContainer;
    }

    public Compagnie createCompagnie(String json) throws IOException, ResourceAlreadyExistsException {

        Compagnie compagnie = compagnieFormatter.deserializeJson(json, Compagnie.class);
        return compagnieOperationController.createCompagnie(compagnie);
    }

    public Object processRequest(String command, Map<Integer, String> params) throws IOException, ResourceAlreadyExistsException {
        switch (command) {
            case "createCompagnies":
                Map<Integer, Compagnie> allCompagnies = new HashMap<>();
                params.forEach((key, value) -> allCompagnies.put(key, new Compagnie(value)));
                return compagnieFormatter.serializeObject(allCompagnies);
            case "createVol":
                return false;
            default:
                return "Command doesn't exist";
        }
    }
}

