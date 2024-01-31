package tiw.is.server.commandBus;

/**
 * @param <R> type of return value
 * @param <C> type of command
 *
 * @see <a href="https://github.com/cloudogu/command-bus/blob/develop/command-bus-core/src/main/java/com/cloudogu/cb/CommandHandler.java">...</a>
 */
public interface HandlerInterface<R, C extends Command<R>> {

    public R handle(C command) throws Exception;
}
