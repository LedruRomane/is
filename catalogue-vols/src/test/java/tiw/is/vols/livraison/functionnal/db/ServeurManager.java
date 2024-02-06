package tiw.is.vols.livraison.functionnal.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import tiw.is.server.Serveur;
import tiw.is.server.ServeurImpl;
import tiw.is.server.utils.FixturesManager;

import java.io.IOException;
import java.nio.file.Paths;

public class ServeurManager {

    public static Serveur serveur;

    @BeforeAll
    public static void setupClass() throws IOException {
        serveur = new ServeurImpl(Paths.get("src/main/resources/appConfiguration.json"));
    }

    @BeforeEach
    public void setupDatabase() throws Exception {
        serveur.getContainer().getComponent(FixturesManager.class).resetDatabase();
    }
}
