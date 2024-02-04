package tiw.is.server.utils;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class FixturesManager {

    private FixturesManager() {}

    public static void resetDatabase(EntityManager em) throws Exception {
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