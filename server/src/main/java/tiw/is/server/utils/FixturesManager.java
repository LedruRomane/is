package tiw.is.server.utils;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public class FixturesManager {

    private static EntityManager em;
    public FixturesManager(EntityManager em) {
        this.em = em;
    }

    public static void resetDatabase() throws Exception {
        Session session = em.unwrap(Session.class);

        try {String sql = new String(Files.readAllBytes(Paths.get("./init.sql")));

            session.doWork(connection -> {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(sql);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw e;
        } finally {
            em.clear();
        }
    }
}