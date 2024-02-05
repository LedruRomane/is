package tiw.is.server.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import tiw.is.server.Serveur;
import tiw.is.server.ServeurImpl;
import tiw.is.server.utils.FixturesManager;

import java.io.IOException;

public class ServeurManager {

    public static Serveur serveur;


    @BeforeAll
    public static void setupClass() throws IOException {
        serveur = new ServeurImpl();
    }


    @BeforeEach
    public void setupDatabase() throws Exception {
        serveur.getContainer().getComponent(FixturesManager.class).resetDatabase();
    }

}
