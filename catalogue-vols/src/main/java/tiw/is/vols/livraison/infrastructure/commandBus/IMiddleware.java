package tiw.is.vols.livraison.infrastructure.commandBus;

public interface IMiddleware {
    public Object handle(ICommand command, IMiddleware next) throws Exception;
}
