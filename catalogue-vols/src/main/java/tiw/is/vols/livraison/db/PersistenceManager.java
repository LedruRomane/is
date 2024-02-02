package tiw.is.vols.livraison.db;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceManager {
    private PersistenceManager() {
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
        String dbName = System.getenv().getOrDefault("DB_NAME", "catalogue-db");
        String dbUrl =
                System.getenv().getOrDefault("DB_URL",
                        "jdbc:postgresql://" + dbHost + "/" + dbName);
        String dbUser =
                System.getenv().getOrDefault("DB_USER",
                        System.getenv().getOrDefault("POSTGRES_USER", "catalogue"));
        String dbPassword =
                System.getenv().getOrDefault("DB_PASSWORD",
                        System.getenv().getOrDefault("POSTGRES_PASSWORD",
                                System.getenv().getOrDefault("DB_NAME", "catalogue-mdp")));
        Map<String, Object> config = new HashMap<>();
        config.put("jakarta.persistence.jdbc.url", dbUrl);
        config.put("jakarta.persistence.jdbc.user", dbUser);
        config.put("jakarta.persistence.jdbc.password", dbPassword);
        config.put("hibernate.hbm2ddl.auto", "update"); // sql dump for Serveur Test needs.
        return Persistence
                .createEntityManagerFactory("pu-catalogue", config);
    }

}
