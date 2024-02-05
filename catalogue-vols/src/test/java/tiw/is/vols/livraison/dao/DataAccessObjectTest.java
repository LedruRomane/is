package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.stream.IntStream;

public abstract class DataAccessObjectTest {

    private static EntityManagerFactory emf;
    protected EntityManager em;
    protected  BaggageDao baggageDao;
    protected CompanyDao companyDao;
    protected FlightDao flightDao;
    protected String testName;
    protected Company[] companies;
    protected Flight[] flights;
    protected Baggage[] baggages;

    @BeforeAll
    public static void setupClass() {
        PersistenceManager persistenceManager = new PersistenceManager(
                "localhost",
                "catalogue-db",
                "catalogue",
                "catalogue-mdp"
        );
        emf = persistenceManager.createEntityManagerFactory();
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Baggage").executeUpdate();
        em.createQuery("DELETE FROM Flight").executeUpdate();
        em.createQuery("DELETE FROM Company").executeUpdate();

        em.getTransaction().commit();
        em.close();

        emf.close();
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        testName = testInfo.getDisplayName();
        em = emf.createEntityManager();
        companyDao = new CompanyDao(em);
        flightDao = new FlightDao(em);
        baggageDao = new BaggageDao(em, flightDao);
        initData();
        persistData();
    }

    protected void initData() {
        int nb_companies = 8;
        companies = IntStream.range(0, nb_companies)
                .mapToObj(i -> new Company("c-" + testName + "-" + i))
                .toArray(Company[]::new);
        flights = IntStream.range(0, nb_companies)
                .boxed()
                .flatMap(i -> IntStream.range(0, i + 1)
                        .mapToObj(j -> new Flight(
                                "v-" + testName + "-" + i + "-" + j,
                                companies[i],
                                "livraison" + "-" + i + "-" + j)))
                .toArray(Flight[]::new);
        baggages = IntStream.range(0, flights.length)
                .boxed()
                .flatMap(i -> IntStream.range(0, i + 1)
                        .mapToObj(j -> flights[i].createBagage(i * 10 + 10,
                                "p-" + testName + "-" + j)))
                .toArray(Baggage[]::new);
        // changement des booleens de baggages pour un peu de variété afin de
        // tester getBagagesPerdusByVolId et getBagagesNonRecuperesByVolId
        for (int i = 0; i < baggages.length; i++) {
            if (i % 2 == 0) {
                baggages[i].delivrer();
            }
            if (i % 3 == 0) {
                baggages[i].recuperer();
            }
        }
    }

    protected void persistData() {
        em.getTransaction().begin();
        for (Company c : companies) {
            em.persist(c);
        }
        for (Flight v : flights) {
            em.persist(v);
        }
        for (Baggage b : baggages) {
            em.persist(b);
        }
        em.getTransaction().commit();
    }

    @AfterEach
    public void teardown() {
        destroyData();
        em.close();
        em = null;
    }

    protected void destroyData() {
        em.getTransaction().begin();
        for (Baggage b : baggages) {
            if (em.contains(b)) {
                em.remove(b);
            }
        }
        em.getTransaction().commit();
        em.getTransaction().begin();
        for (Flight v : flights) {
            if (em.contains(v)) {
                em.remove(v);
            }
        }
        em.getTransaction().commit();
        em.getTransaction().begin();
        for (Company c : companies) {
            if (em.contains(c)) {
                em.remove(c);
            }
        }
        em.getTransaction().commit();
        flights = null;
        companies = null;
    }

}
