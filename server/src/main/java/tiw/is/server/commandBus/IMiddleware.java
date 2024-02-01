package tiw.is.server.commandBus;

public interface IMiddleware {
    public Object handle(ICommand command, IMiddleware next) throws Exception;

    public Object handle(ICommand command) throws Exception;
}
