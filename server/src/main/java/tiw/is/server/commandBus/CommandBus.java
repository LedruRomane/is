package tiw.is.server.commandBus;

import jakarta.persistence.EntityManager;
import java.util.Map;

public class CommandBus<R, C extends Command<R>> {
    public Map<Class, HandlerInterface<R, C>> handlerLocator;
    public EntityManager em;

    public CommandBus(Map<Class, HandlerInterface<R, C>> handlerLocator, EntityManager em) {
        this.handlerLocator = handlerLocator;
        this.em = em;
    }

    public R handle(C command) throws Exception {
        em.getTransaction().begin();

        try {
            HandlerInterface<R, C> handler = handlerLocator.get(command.getClass());

            return handler.handle(command);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.getTransaction().commit();
        }
    }
}
