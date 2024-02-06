package tiw.is.server.commandBus;

import java.util.Map;

/**
 * Middleware that provide the command handler execution.
 */
public class HandlerMiddleware implements IMiddleware {
    protected static Map<Class, ICommandHandler<Object, ICommand>> handlerLocator;

    public HandlerMiddleware(Map<Class, ICommandHandler<Object, ICommand>> handlerLocator) {
        HandlerMiddleware.handlerLocator = handlerLocator;
    }

    @Override
    public Object handle(ICommand command, IMiddleware next) throws Exception {
        ICommandHandler<Object, ICommand> handler = handlerLocator.get(command.getClass());
        return handler.handle(command);
    }
}
