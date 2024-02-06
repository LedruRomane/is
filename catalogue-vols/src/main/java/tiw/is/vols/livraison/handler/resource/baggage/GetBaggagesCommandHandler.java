package tiw.is.vols.livraison.handler.resource.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.resource.baggage.GetBaggagesCommand;
import tiw.is.server.commandBus.ICommandHandler;

import java.util.Collection;

public class GetBaggagesCommandHandler implements ICommandHandler<Collection<BaggageDTO>, GetBaggagesCommand> {
    private final BaggageDao dao;

    public GetBaggagesCommandHandler(BaggageDao dao) {
        this.dao = dao;
    }

    /**
     * Execution findAll Baggages. Usually get command payload, calls DAO, and return DTO.
     *
     * @param command
     * @return Collection of BaggageDTO.
     * @throws ResourceNotFoundException
     */
    public Collection<BaggageDTO> handle(GetBaggagesCommand command) throws ResourceNotFoundException {
        return dao.getAll().stream().map(BaggageDTO::fromBaggage).toList();
    }
}
