package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Bagage;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueBagageTest extends CatalogueTest {

    private Collection<Bagage> getBagageByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Bagage b WHERE b.vol.id = :vId",
                Bagage.class);
        dq.setParameter("vId", volId);
        return dq.getResultList();
    }

    @Test
    void getBagages() {
        var bags = catalogueBagage.getBagages();
        for (Bagage b : bagages) {
            assertTrue(bags.contains(b));
        }
    }

    @Test
    void createBagage() {
        Vol v = vols[0];
        em.getTransaction().begin();
        String passager = "p-" + testName + "nouveau";
        Bagage b = catalogueBagage.createBagage(v, 1, passager);
        em.getTransaction().commit();
        assertNotNull(b);
        assertEquals(1, b.getPoids());
        assertEquals(passager, b.getPassager());
        em.getTransaction().begin();
        Bagage b2 = catalogueBagage.createBagage(v, 1, passager);
        em.getTransaction().commit();
        assertNotEquals(b, b2);
        assertNotEquals(b.getNumero(),b2.getNumero());
        em.getTransaction().begin();
        em.remove(b);
        em.remove(b2);
        em.getTransaction().commit();
    }

    @Test
    void getBagageById() {
        Bagage b = bagages[0];
        assertNotNull(catalogueBagage.getBagageById(b.getVol().getId(), b.getNumero()));
        assertNull(catalogueBagage.getBagageById("vol inexistant", 1));
        assertNull(catalogueBagage.getBagageById(b.getVol().getId(), -1));
    }

    @Test
    void deleteBagageById() {
        Bagage b = bagages[2];
        em.getTransaction().begin();
        assertTrue(catalogueBagage.deleteBagageById(b.getVol().getId(), b.getNumero()));
        em.getTransaction().commit();
        em.getTransaction().begin();
        assertFalse(catalogueBagage.deleteBagageById(b.getVol().getId(), b.getNumero()));
        em.getTransaction().commit();
    }

    @Test
    void getBagagesPerdusByVolId() {
        String lastVolId = vols[vols.length-1].getId();
        Collection<Bagage> bagageByVolId = getBagageByVolId(lastVolId);
        assertTrue(bagageByVolId.stream().anyMatch(Bagage::isDelivre), "Il n'y a pas de bagage délivré");
        assertTrue(bagageByVolId.stream().anyMatch(b -> !b.isDelivre()),
                "Il n'y a pas de bagage non délivré");
        var bgg = catalogueBagage.getBagagesPerdusByVolId(lastVolId);
        assertTrue(bgg.size() > 0);
        for(Bagage b:bgg) {
            assertFalse(b.isDelivre(),
                    "Le bagage "+b.getVol()+"-"+b.getNumero()+" ne devrait " +
                            "pas figurer dans les bagages perdus");
        }
    }

    @Test
    void getBagagesNonRecuperesByVolId() {
        String lastVolId = vols[vols.length-1].getId();
        Collection<Bagage> bagageByVolId = getBagageByVolId(lastVolId);
        assertTrue(bagageByVolId.stream().anyMatch(Bagage::isRecupere), "Il " +
                "n'y a pas de bagage récupéré");
        assertTrue(bagageByVolId.stream().anyMatch(b -> !b.isRecupere()),
                "Il n'y a pas de bagage non récupéré");
        var bgg = catalogueBagage.getBagagesNonRecuperesByVolId(lastVolId);
        assertTrue(bgg.size() > 0);
        for(Bagage b:bgg) {
            assertFalse(b.isRecupere(),
                    "Le bagage "+b.getVol()+"-"+b.getNumero()+" ne devrait " +
                            "pas figurer dans les bagages non recuperes");
        }
    }
}