package tiw.is.server.commandBus;

import jakarta.persistence.EntityManager;

public class TransactionMiddleware implements IMiddleware {
    private final EntityManager em;

    public TransactionMiddleware(EntityManager em) {
        this.em = em;
    }

    /**
     * middleware that wrap the handler execution into a transaction.
     * @param command provide it directly to the handler.
     * @param next middleware to execute (letsgo -> into :D)
     * @return the return from the next.
     * @throws Exception
     */
    @Override
    public Object handle(ICommand command, IMiddleware next) throws Exception {
        em.getTransaction().begin();

        try {
            return next.handle(command);
        } catch (Exception e) {
            em.getTransaction().rollback();

            throw e;
        } finally {
            em.getTransaction().commit();
        }
    }

    /**
     * Transaction Middleware can't be the leaf middleware.
     * @param command
     * @throws Exception
     */
    @Override
    public Object handle(ICommand command) throws Exception {
        throw new IllegalAccessException("Unexpected call");
    }
}
