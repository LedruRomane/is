package tiw.is.vols.livraison.infrastructure.handler.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Vol;

import java.util.Optional;

public class GetBaggageCommandHandler implements ICommandHandler<BaggageDTO, GetBaggageCommand> {

    private final BaggageDao dao;
    public GetBaggageCommandHandler(BaggageDao dao) {
        this.dao = dao;
    }

    /**
     * Renvoie un bagage en fonction de l'id du vol et de son numéro.
     *
     * @param command payload qui contient l'id du vol et numéro du bagage.
     * @return le bagage trouvé ou null si aucun bagage n'a été trouvé.
     */
    public BaggageDTO handle(GetBaggageCommand command) throws ResourceNotFoundException {
        Baggage baggage = Optional.ofNullable(dao.getOneById(command.id(), command.num())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + command.id() + " n'existe pas, ou le bagage " + command.num() + " n'existe pas.")
        );

        return BaggageDTO.fromBaggage(baggage);
    }
}
