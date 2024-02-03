package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

import java.util.stream.IntStream;

public abstract class CatalogueTest {
    private static EntityManagerFactory emf;
    protected EntityManager em;
    protected CatalogCompany catalogCompany;//todo: change
    protected CatalogueVol catalogueVol;
    protected CatalogueBagage catalogueBagage;
    protected String testName;
    protected Company[] companies;
    protected Vol[] vols;
    protected Baggage[] baggages;

    @BeforeAll
    public static void setupClass() {
        emf = PersistenceManager.createEntityManagerFactory();
    }

    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        testName = testInfo.getDisplayName();
        em = emf.createEntityManager();
        catalogCompany = new CatalogCompany(em);
        catalogueVol =  new CatalogueVol(em);
        catalogueBagage = new CatalogueBagage(em,catalogueVol);
        initData();
        persistData();
    }

    protected void initData() {
        int nb_companies = 8;
        companies = IntStream.range(0, nb_companies)
                .mapToObj(i -> new Company("c-" + testName + "-" + i))
                .toArray(Company[]::new);
        vols = IntStream.range(0, nb_companies)
                .boxed()
                .flatMap(i -> IntStream.range(0, i + 1)
                        .mapToObj(j -> new Vol(
                                "v-" + testName + "-" + i + "-" + j,
                                companies[i],
                                "livraison" + "-" + i + "-" + j)))
                .toArray(Vol[]::new);
        baggages = IntStream.range(0, vols.length)
                .boxed()
                .flatMap(i -> IntStream.range(0, i + 1)
                        .mapToObj(j -> vols[i].createBagage(i * 10 + 10,
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
        for (Vol v : vols) {
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
        for (Vol v : vols) {
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
        vols = null;
        companies = null;
    }
}