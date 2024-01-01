package tiw.is.vols.catalogue.db;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceManager {

    public static EntityManagerFactory createEntityManagerFactory() {
        Map<String, Object> config = new HashMap<>();
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("POSTGRES_USER");
        String dbPassword = System.getenv("POSTGRES_PASSWORD");

        //print
        System.out.println("DB_URL: " + dbUrl);
        System.out.println("POSTGRES_DB: " + System.getenv("POSTGRES_DB"));
        System.out.println("POSTGRES_USER: " + System.getenv("POSTGRES_USER"));

        config.put("hibernate.hbm2ddl.auto", "update");
        config.put("jakarta.persistence.jdbc.url", (dbUrl != null) ? dbUrl : "jdbc:postgresql:catalogue-db");
        config.put("jakarta.persistence.jdbc.user", dbUser != null ? dbUser : "catalogue");
        config.put("jakarta.persistence.jdbc.password", dbPassword != null ? dbPassword : "catalogue-mdp");
        config.put("hibernate.hbm2ddl.auto", "create");
        return Persistence.createEntityManagerFactory("pu-catalogue", config);

    }
}
