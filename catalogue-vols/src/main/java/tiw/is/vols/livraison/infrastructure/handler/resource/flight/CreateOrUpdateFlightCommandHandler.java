package tiw.is.vols.livraison.infrastructure.handler.resource.flight;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.CreateOrUpdateFlightCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.Optional;

public class CreateOrUpdateFlightCommandHandler implements ICommandHandler<FlightDTO, CreateOrUpdateFlightCommand> {

    private final FlightDao dao;
    private final CompanyDao companyDao;

    public CreateOrUpdateFlightCommandHandler(
            FlightDao dao,
            CompanyDao companyDao
    ) {
        this.dao = dao;
        this.companyDao = companyDao;
    }

    public FlightDTO handle(CreateOrUpdateFlightCommand command) throws ResourceNotFoundException {
        FlightDTO dto = new FlightDTO(command.id(), command.companyID(), command.pointLivraisonBagages());

        Company company = Optional.ofNullable(companyDao.getOneById(dto.company())).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + dto.company() + " n'existe pas.")
        );
        dao.save(new Flight(dto.id(), company, dto.pointLivraisonBagages()));

        return dto;
    }
}
