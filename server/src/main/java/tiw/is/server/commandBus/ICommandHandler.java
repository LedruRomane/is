package tiw.is.server.commandBus;

/**
 * @param <R> type of return value (Company, Bagage, or Collections, boolean, etc.)
 * @param <C> type of command
 *
 * @see <a href="https://github.com/cloudogu/command-bus/blob/develop/command-bus-core/src/main/java/com/cloudogu/cb/CommandHandler.java">...</a>
 */
public interface ICommandHandler<R, C> {

    public R handle(C command) throws Exception;
}
