package tiw.is.server.commandBus;

import java.util.Map;

public class HandlerMiddleware implements IMiddleware {
    protected static Map<Class, ICommandHandler<Object, ICommand>> handlerLocator;

    public HandlerMiddleware(Map<Class, ICommandHandler<Object, ICommand>> handlerLocator) {
        HandlerMiddleware.handlerLocator = handlerLocator;
    }

    @Override
    public Object handle(ICommand command, IMiddleware next) throws Exception {
        throw new IllegalAccessException("Unexpected call");
    }

    @Override
    public Object handle(ICommand command) throws Exception {
        ICommandHandler<Object, ICommand> handler = handlerLocator.get(command.getClass());
        return handler.handle(command);
    }
}
