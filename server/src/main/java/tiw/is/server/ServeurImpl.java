package tiw.is.server;

import jakarta.persistence.EntityManager;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import tiw.is.server.command.company.CreateCompanyCommand;
import tiw.is.server.commandBus.CommandBus;
import tiw.is.server.commandBus.HandlerInterface;
import tiw.is.server.handler.company.CreateCompanyCommandHandler;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.controller.resource.CompanyOperationController;
import tiw.is.vols.livraison.dao.CatalogCompany;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.model.Company;

import java.util.HashMap;
import java.util.Map;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;

    private static JsonFormatter<Company> companyFormatter;

    public ServeurImpl() {

        this.picoContainer = new PicoBuilder(new ConstructorInjection()).withCaching().build();

        EntityManager em = PersistenceManager.createEntityManagerFactory().createEntityManager();
        picoContainer.addComponent(em);

        picoContainer.addComponent(CatalogCompany.class);
        picoContainer.addComponent(CompanyOperationController.class);
        picoContainer.addComponent(CreateCompanyCommandHandler.class);

        Map<Class, HandlerInterface> handlerLocator = new HashMap<>();

        handlerLocator.put(CreateCompanyCommand.class, picoContainer.getComponent(CreateCompanyCommandHandler.class));

        CommandBus cm = new CommandBus(handlerLocator, em);

        picoContainer.addComponent(cm);

        companyFormatter = new JsonFormatter<>();
    }

    private CommandBus getCommandBus() {
        return picoContainer.getComponent(CommandBus.class);
    }

    public Object processRequest(String command, Map<String, Object> params) {
        try {
            switch (command) {
                case "createCompany" :
                    Object data = this.getCommandBus().handle(new CreateCompanyCommand((String) params.get("id")));
                    return companyFormatter.serializeObject(data);
                case "deleteCompany":
                    //todo: delete company
                // etc.
                default:
                    return "Command doesn't exist";
            }
        } catch (Exception e){
            return "KO";
        }
    }
}

