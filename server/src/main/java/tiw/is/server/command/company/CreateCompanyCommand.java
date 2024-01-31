package tiw.is.server.command.company;

import tiw.is.server.commandBus.Command;
import tiw.is.vols.livraison.model.Company;

public record CreateCompanyCommand(String id) implements Command<Company> {}
