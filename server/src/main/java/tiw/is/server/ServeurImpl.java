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

    private static JsonFormatter<Compagnie> compagnieFormatter;

    public ServeurImpl() throws SQLException {

        this.picoContainer = new PicoBuilder(new ConstructorInjection()).withCaching().build();

        EntityManager em = PersistenceManager.createEntityManagerFactory().createEntityManager();

        picoContainer.addComponent(em);
        picoContainer.addComponent(CatalogueCompanie.class);
        picoContainer.addComponent(CompagnieOperationController.class);

        compagnieFormatter = new JsonFormatter<>();
    }

    private CompagnieOperationController getCompagnieOperationController() {
        return picoContainer.getComponent(CompagnieOperationController.class);
    }

    private EntityManager getEntityManager() {
        return picoContainer.getComponent(EntityManager.class);
    }

    private CatalogueCompanie getCatalogueCompanie() {

        return picoContainer.getComponent(CatalogueCompanie.class);
    }

    public MutablePicoContainer getPicoContainer() {
        return picoContainer;
    }

    public Object processRequest(String command, Map<String, Object> params) throws IOException, ResourceAlreadyExistsException {
        getEntityManager().getTransaction().begin();
        try {
            return switch (command) {
                case "createCompagnie",
                        "getCompagnies",
                        "getCompagnie",
                        "deleteCompagnie" -> getCompagnieOperationController().process(command, params);
                default -> "Command doesn't exist";
            };
        } catch (Exception e){
            getEntityManager().getTransaction().rollback();
            throw e;
        } finally {
            getEntityManager().getTransaction().commit();
        }
    }
}

