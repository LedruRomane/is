package tiw.is.server.commandBus;

import org.picocontainer.Startable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command Handler interface.
 * ndlr : les controlleurs qui devaient extends Startable sont les command Handler.
 *
 * @param <R> type of return value (Company, Baggage, or Collections, boolean, etc.)
 * @param <C> type of command
 *
 * @see <a href="https://github.com/cloudogu/command-bus/blob/develop/command-bus-core/src/main/java/com/cloudogu/cb/CommandHandler.java">...</a>
 */
public interface ICommandHandler<R, C> extends Startable {
    Logger logger = LoggerFactory.getLogger(ICommandHandler.class);

    R handle(C command) throws Exception;


    default void start() {
        logger.info("Composant démarré : {}", this.getClass().getName());
    }

    default void stop() {}
}