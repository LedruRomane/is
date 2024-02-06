package tiw.is.server.commandBus;

import jakarta.persistence.EntityManager;

public class TransactionMiddleware implements IMiddleware {
    private final EntityManager em;

    public TransactionMiddleware(EntityManager em) {
        this.em = em;
    }

    /**
     * middleware that wrap the handler execution into a transaction.
     *
     * @param command provide it directly to the handler.
     * @param next    middleware to execute (letsgo -> into :D)
     * @return the return from the next.
     * @throws Exception
     */
    @Override
    public Object handle(ICommand command, IMiddleware next) throws Exception {
        em.getTransaction().begin();

        try {
            return next.handle(command, null);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
        }
    }
}
