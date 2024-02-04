package tiw.is.vols.livraison.db;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceManager {
    private String dbHost;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    public PersistenceManager() {}
    public PersistenceManager(String dbHost, String dbName, String dbUser, String dbPassword) {
        this.dbHost = dbHost;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public EntityManagerFactory createEntityManagerFactory() {
        String dbHost = System.getenv().getOrDefault("DB_HOST", this.dbHost);
        String dbName = System.getenv().getOrDefault("DB_NAME", this.dbName);
        String dbUrl =
                System.getenv().getOrDefault("DB_URL",
                        "jdbc:postgresql://" + dbHost + "/" + dbName);
        String dbUser =
                System.getenv().getOrDefault("DB_USER",
                        System.getenv().getOrDefault("POSTGRES_USER", this.dbUser));
        String dbPassword =
                System.getenv().getOrDefault("DB_PASSWORD",
                        System.getenv().getOrDefault("POSTGRES_PASSWORD",
                                System.getenv().getOrDefault("DB_NAME", this.dbPassword)));
        Map<String, Object> config = new HashMap<>();
        config.put("jakarta.persistence.jdbc.url", dbUrl);
        config.put("jakarta.persistence.jdbc.user", dbUser);
        config.put("jakarta.persistence.jdbc.password", dbPassword);
        config.put("hibernate.hbm2ddl.auto", "update"); // sql dump for Serveur Test needs.
        return Persistence
                .createEntityManagerFactory("pu-catalogue", config);
    }

}
