package tiw.is.server.service;

import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.exception.CommandNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.CreateBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.DeleteBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.CreateCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.GetCompaniesCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.GetCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.CreateOrUpdateFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.DeleteFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightsCommand;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.DeliverBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.RetrievalBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.CloseShipmentCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetLostBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetUnclaimedBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.CommandBus;

import java.util.Map;

public abstract class Dispatcher {
    private static final JsonFormatter<Object> formatter = new JsonFormatter<>();

    private Dispatcher() {}

    public static Object dispatch(String command, Map<String, Object> params, CommandBus commandBus) throws Exception {
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

    public static Object dispatchCompanyResource(String command, Map<String, Object> params, CommandBus commandBus) throws Exception {
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

    public static Object dispatchFlightResource(String command, Map<String, Object> params, CommandBus commandBus) throws Exception {
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

    public static Object dispatchBaggageResource(String command, Map<String, Object> params, CommandBus commandBus) throws Exception {
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
