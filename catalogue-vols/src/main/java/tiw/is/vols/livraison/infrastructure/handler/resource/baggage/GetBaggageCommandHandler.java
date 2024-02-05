package tiw.is.vols.livraison.infrastructure.handler.resource.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Baggage;

import java.util.Optional;

public class GetBaggageCommandHandler implements ICommandHandler<BaggageDTO, GetBaggageCommand> {

    private final BaggageDao dao;
    public GetBaggageCommandHandler(BaggageDao dao) {
        this.dao = dao;
    }

    /**
     * Execution found one baggage. Usually get command payload, calls DAO, and return DTO.
     *
     * @param command payload with Flight's ID and Baggage's num.
     * @return BaggageDTO.
     */
    public BaggageDTO handle(GetBaggageCommand command) throws ResourceNotFoundException {
        Baggage baggage = Optional.ofNullable(dao.getOneById(command.id(), command.num())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + command.id() + " n'existe pas, ou le bagage " + command.num() + " n'existe pas.")
        );

        return BaggageDTO.fromBaggage(baggage);
    }
}
