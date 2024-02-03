package tiw.is.vols.livraison.infrastructure.handler.flight;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompaniesCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.GetFlightsCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.Collection;

public class GetFlightsCommandHandler implements ICommandHandler<Collection<VolDTO>, GetFlightsCommand> {
    private final FlightDao dao;

    public GetFlightsCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    public Collection<VolDTO> handle(GetFlightsCommand command) throws ResourceNotFoundException {
        return dao.getAll().stream().map(VolDTO::fromVol).toList();
    }
}
