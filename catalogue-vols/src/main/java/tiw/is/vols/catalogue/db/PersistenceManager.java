package tiw.is.vols.catalogue.db;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceManager {

    public static EntityManagerFactory createEntityManagerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put("hibernate.hbm2ddl.auto", "update");
        config.put("jakarta.persistence.jdbc.url", "jdbc:postgresql:catalogue-db");
        config.put("jakarta.persistence.jdbc.user", "catalogue");
        config.put("jakarta.persistence.jdbc.password", "catalogue-mdp");
        config.put("hibernate.hbm2ddl.auto", "create");
        return Persistence
                .createEntityManagerFactory("pu-catalogue", config);
    }

}
