package tiw.is.server.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.*;

import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.model.Bagage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public abstract class FixturesTest {

    public void resetDatabase(EntityManager em) throws Exception {
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