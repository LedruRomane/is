package tiw.is.vols.livraison;

import tiw.is.server.service.IDispatcher;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.server.exception.CommandNotFoundException;
import tiw.is.vols.livraison.command.resource.baggage.CreateBaggageCommand;
import tiw.is.vols.livraison.command.resource.baggage.DeleteBaggageCommand;
import tiw.is.vols.livraison.command.resource.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.command.resource.baggage.GetBaggagesCommand;
import tiw.is.vols.livraison.command.resource.company.CreateCompanyCommand;
import tiw.is.vols.livraison.command.resource.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.command.resource.company.GetCompaniesCommand;
import tiw.is.vols.livraison.command.resource.company.GetCompanyCommand;
import tiw.is.vols.livraison.command.resource.flight.CreateOrUpdateFlightCommand;
import tiw.is.vols.livraison.command.resource.flight.DeleteFlightCommand;
import tiw.is.vols.livraison.command.resource.flight.GetFlightCommand;
import tiw.is.vols.livraison.command.resource.flight.GetFlightsCommand;
import tiw.is.vols.livraison.command.service.baggage.DeliverBaggageCommand;
import tiw.is.vols.livraison.command.service.baggage.RetrievalBaggageCommand;
import tiw.is.vols.livraison.command.service.flight.CloseShipmentCommand;
import tiw.is.vols.livraison.command.service.flight.GetLostBaggagesCommand;
import tiw.is.vols.livraison.command.service.flight.GetUnclaimedBaggagesCommand;
import tiw.is.server.commandBus.CommandBus;
import tiw.is.server.commandBus.ICommand;

import java.util.Map;

public class Dispatcher implements IDispatcher {
    private static final JsonFormatter<Object> formatter = new JsonFormatter<>();

    private CommandBus commandBus;

    public Dispatcher(CommandBus<?, ? extends ICommand> commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public Object dispatch(String resource, String command, Map<String, Object> params) throws Exception {
        return switch (resource) {
            case "company" -> dispatchCompanyResource(command, params);
            case "flight" -> dispatchFlightResource(command, params);
            case "baggage" -> dispatchBaggageResource(command, params);
            case "flightBusiness", "baggagesBusiness" -> dispatchFlightBaggageService(command, params);
            default -> throw new CommandNotFoundException(resource + " does not exist.");
        };
    }

    public Object dispatchFlightBaggageService(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "deliver" -> formatter.serializeObject(
                    commandBus.handle(new DeliverBaggageCommand(
                            (String) params.get("id"),
                            (int) params.get("num")
                    ))
            );
            case "retrieval" -> formatter.serializeObject(
                    commandBus.handle(new RetrievalBaggageCommand(
                            (String) params.get("id"),
                            (int) params.get("num")
                    ))
            );
            case "closeShipment" -> formatter.serializeObject(
                    commandBus.handle(new CloseShipmentCommand(
                            (String) params.get("id")
                    ))
            );
            case "getLostBaggages" -> formatter.serializeObject(
                    commandBus.handle(new GetLostBaggagesCommand(
                            (String) params.get("id")
                    ))
            );
            case "getUnclaimedBaggages" -> formatter.serializeObject(
                    commandBus.handle(new GetUnclaimedBaggagesCommand(
                            (String) params.get("id")
                    ))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }

    public Object dispatchCompanyResource(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "create" -> formatter.serializeObject(
                    commandBus.handle(new CreateCompanyCommand((String) params.get("id")))
            );
            case "get" -> formatter.serializeObject(
                    commandBus.handle(new GetCompanyCommand((String) params.get("id")))
            );
            case "getAll" -> formatter.serializeObject(
                    commandBus.handle(new GetCompaniesCommand())
            );
            case "delete" -> formatter.serializeObject(
                    commandBus.handle(new DeleteCompanyCommand((String) params.get("id")))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }

    public Object dispatchFlightResource(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "create", "update" -> formatter.serializeObject(
                    commandBus.handle(new CreateOrUpdateFlightCommand(
                            (String) params.get("id"),
                            (String) params.get("companyId"),
                            (String) params.get("pointLivraisonBagages")
                    ))
            );
            case "getAll" -> formatter.serializeObject(
                    commandBus.handle(new GetFlightsCommand())
            );
            case "get" -> formatter.serializeObject(
                    commandBus.handle(new GetFlightCommand((String) params.get("id")))
            );
            case "delete" -> formatter.serializeObject(
                    commandBus.handle(new DeleteFlightCommand((String) params.get("id")))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }

    public Object dispatchBaggageResource(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "create" -> formatter.serializeObject(
                    commandBus.handle(new CreateBaggageCommand(
                            (String) params.get("id"),
                            (String) params.get("weight"),
                            (String) params.get("passenger")
                    ))
            );
            case "getAll" -> formatter.serializeObject(
                    commandBus.handle(new GetBaggagesCommand())
            );
            case "get" -> formatter.serializeObject(
                    commandBus.handle(new GetBaggageCommand(
                            (String) params.get("id"),
                            (int) params.get("num")
                    ))
            );
            case "delete" -> formatter.serializeObject(
                    commandBus.handle(new DeleteBaggageCommand(
                            (String) params.get("id"),
                            (int) params.get("num")
                    ))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }
}
