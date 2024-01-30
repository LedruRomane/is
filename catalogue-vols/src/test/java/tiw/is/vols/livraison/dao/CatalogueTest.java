package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.model.Bagage;
import tiw.is.vols.livraison.model.Compagnie;
import tiw.is.vols.livraison.model.Vol;

import java.util.stream.IntStream;

public abstract class CatalogueTest {
    private static EntityManagerFactory emf;
    protected EntityManager em;
    protected CatalogueCompanie catalogueCompanie;
    protected CatalogueVol catalogueVol;
    protected CatalogueBagage catalogueBagage;
    protected String testName;
    protected Compagnie[] compagnies;
    protected Vol[] vols;
    protected Bagage[] bagages;

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
        catalogueCompanie = new CatalogueCompanie(em);
        catalogueVol =  new CatalogueVol(em);
        catalogueBagage = new CatalogueBagage(em,catalogueVol);
        initData();
        persistData();
    }

    protected void initData() {
        int nb_compagnies = 8;
        compagnies = IntStream.range(0, nb_compagnies)
                .mapToObj(i -> new Compagnie("c-" + testName + "-" + i))
                .toArray(Compagnie[]::new);
        vols = IntStream.range(0, nb_compagnies)
                .boxed()
                .flatMap(i -> IntStream.range(0, i + 1)
                        .mapToObj(j -> new Vol(
                                "v-" + testName + "-" + i + "-" + j,
                                compagnies[i],
                                "livraison" + "-" + i + "-" + j)))
                .toArray(Vol[]::new);
        bagages = IntStream.range(0, vols.length)
                .boxed()
                .flatMap(i -> IntStream.range(0, i + 1)
                        .mapToObj(j -> vols[i].createBagage(i * 10 + 10,
                                "p-" + testName + "-" + j)))
                .toArray(Bagage[]::new);
        // changement des booleens de bagages pour un peu de variété afin de
        // tester getBagagesPerdusByVolId et getBagagesNonRecuperesByVolId
        for (int i = 0; i < bagages.length; i++) {
            if (i % 2 == 0) {
                bagages[i].delivrer();
            }
            if (i % 3 == 0) {
                bagages[i].recuperer();
            }
        }
    }

    protected void persistData() {
        em.getTransaction().begin();
        for (Compagnie c : compagnies) {
            em.persist(c);
        }
        for (Vol v : vols) {
            em.persist(v);
        }
        for (Bagage b : bagages) {
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
        for (Bagage b : bagages) {
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
        for (Compagnie c : compagnies) {
            if (em.contains(c)) {
                em.remove(c);
            }
        }
        em.getTransaction().commit();
        vols = null;
        compagnies = null;
    }
}