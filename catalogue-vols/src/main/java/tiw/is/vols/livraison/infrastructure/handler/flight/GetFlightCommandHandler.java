package tiw.is.vols.livraison.infrastructure.handler.flight;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

import java.util.Optional;

public class GetFlightCommandHandler implements ICommandHandler<VolDTO, GetFlightCommand> {

    private final FlightDao dao;
    public GetFlightCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    /**
     * Renvoie un vol en fonction de son id.
     *
     * @param command payload qui contient l'id du vol cherché.
     * @return le vol trouvé ou null si aucun vol n'a été trouvé.
     */
    public VolDTO handle(GetFlightCommand command) throws ResourceNotFoundException {
        Vol vol = Optional.ofNullable(dao.getOneById(command.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le vol " + command.id() + " n'existe pas.")
        );

        return VolDTO.fromVol(vol);
    }
}
