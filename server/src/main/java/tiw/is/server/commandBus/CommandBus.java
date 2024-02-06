package tiw.is.server.commandBus;

import java.util.Collection;
import java.util.Iterator;

/**
 * @param <R> return value.
 * @param <C> Command provided.
 */
public class CommandBus<R, C extends ICommand> {
    private final Collection<IMiddleware> middleware;

    /**
     * Wrapp commandbus execution with middleware.
     * @param middleware    Collection of middleware.
     */
    public CommandBus(Collection<IMiddleware> middleware) {
        this.middleware = middleware;
    }

    /**
     * Find and execute the handler which corresponds to command C.
     *
     * @param command command C.
     * @return the object R the handler return.
     * @throws Exception
     */
    public Object handle(C command) throws Exception {
        Iterator<IMiddleware> iterator = middleware.iterator();
        IMiddleware current = iterator.next();
        try {
            if (iterator.hasNext()) {
                return current.handle(command, iterator.next());
            }
            return current.handle(command, null);

        } catch (Exception e) {
            throw e;
        }
    }
}
