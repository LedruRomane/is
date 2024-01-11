package tiw.is.vols.catalogue.db.fixtures;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import tiw.is.vols.catalogue.modeles.Bagage;
import tiw.is.vols.catalogue.modeles.BagageKey;
import tiw.is.vols.catalogue.modeles.Companie;
import tiw.is.vols.catalogue.modeles.Vol;


public class AppFixtures {

    public static void resetDatabase() throws Exception {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu-vol")) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.createQuery("DELETE FROM Bagage").executeUpdate();
                em.createQuery("DELETE FROM Vol").executeUpdate();
                em.createQuery("DELETE FROM Companie").executeUpdate();
                em.getTransaction().commit();
            } catch (Exception e) {
                throw new Exception("Erreur lors du reset de la base de données");
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors du reset de la base de données");
        }
    }

    public static void loadFixtures() throws Exception {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu-vol")) {
            try (EntityManager em = emf.createEntityManager()) {
                Companie c1 = new Companie("Compagnie A");
                Companie c2 = new Companie("Compagnie B");
                Companie c3 = new Companie("Compagnie C");

                Vol v1 = new Vol("vol1", c1, true, "b");
                Vol v2 = new Vol("vol2", c2, true, "b");
                Vol v3 = new Vol("vol3", c3, true, "b");

                BagageKey b1Key = new BagageKey(v1, 1);
                BagageKey b2Key = new BagageKey(v2, 2);
                BagageKey b3Key = new BagageKey(v3, 3);

                Bagage b1 = new Bagage(b1Key, 10, "passager1");
                Bagage b2 = new Bagage(b2Key, 20, "passager2");
                Bagage b3 = new Bagage(b3Key, 30, "passager3");

                em.getTransaction().begin();
                em.persist(c1);
                em.persist(c2);
                em.persist(c3);
                em.persist(v1);
                em.persist(v2);
                em.persist(v3);
                em.persist(b1);
                em.persist(b2);
                em.persist(b3);
                em.getTransaction().commit();
            } catch (Exception e) {
                throw new Exception("Erreur lors du chargement des données de test");
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors du chargement des données de test");
        }

    }
}
