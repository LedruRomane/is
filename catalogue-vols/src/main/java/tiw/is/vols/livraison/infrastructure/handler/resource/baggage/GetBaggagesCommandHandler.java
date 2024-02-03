package tiw.is.vols.livraison.infrastructure.handler.resource.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;

import java.util.Collection;

public class GetBaggagesCommandHandler implements ICommandHandler<Collection<BaggageDTO>, GetBaggagesCommand> {
    private final BaggageDao dao;

    public GetBaggagesCommandHandler(BaggageDao dao) {
        this.dao = dao;
    }

    public Collection<BaggageDTO> handle(GetBaggagesCommand command) throws ResourceNotFoundException {
        return dao.getAll().stream().map(BaggageDTO::fromBaggage).toList();
    }
}
