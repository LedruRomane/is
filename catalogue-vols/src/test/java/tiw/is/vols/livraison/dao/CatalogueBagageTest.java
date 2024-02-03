/*
package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueBagageTest extends CatalogueTest {

    private Collection<Baggage> getBagageByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Baggage b WHERE b.vol.id = :vId",
                Baggage.class);
        dq.setParameter("vId", volId);
        return dq.getResultList();
    }

@Test
    void getBagages() {
        var bags = catalogueBagage.getBagages();
        for (Baggage b : baggages) {
            assertTrue(bags.contains(b));
        }
    }


    @Test
    void createBagage() {
        Vol v = vols[0];
        em.getTransaction().begin();
        String passager = "p-" + testName + "nouveau";
        Baggage b = catalogueBagage.createBagage(v, 1, passager);
        em.getTransaction().commit();
        assertNotNull(b);
        assertEquals(1, b.getPoids());
        assertEquals(passager, b.getPassager());
        em.getTransaction().begin();
        Baggage b2 = catalogueBagage.createBagage(v, 1, passager);
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
        Baggage b = baggages[0];
        assertNotNull(catalogueBagage.getBagageById(b.getVol().getId(), b.getNumero()));
        assertNull(catalogueBagage.getBagageById("vol inexistant", 1));
        assertNull(catalogueBagage.getBagageById(b.getVol().getId(), -1));
    }

@Test
    void deleteBagageById() {
        Baggage b = baggages[2];
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
        Collection<Baggage> bagageByVolId = getBagageByVolId(lastVolId);
        assertTrue(bagageByVolId.stream().anyMatch(Baggage::isDelivre), "Il n'y a pas de bagage délivré");
        assertTrue(bagageByVolId.stream().anyMatch(b -> !b.isDelivre()),
                "Il n'y a pas de bagage non délivré");
        var bgg = catalogueBagage.getBagagesPerdusByVolId(lastVolId);
        assertTrue(bgg.size() > 0);
        for(Baggage b:bgg) {
            assertFalse(b.isDelivre(),
                    "Le bagage "+b.getVol()+"-"+b.getNumero()+" ne devrait " +
                            "pas figurer dans les baggages perdus");
        }
    }

    @Test
    void getBagagesNonRecuperesByVolId() {
        String lastVolId = vols[vols.length-1].getId();
        Collection<Baggage> bagageByVolId = getBagageByVolId(lastVolId);
        assertTrue(bagageByVolId.stream().anyMatch(Baggage::isRecupere), "Il " +
                "n'y a pas de bagage récupéré");
        assertTrue(bagageByVolId.stream().anyMatch(b -> !b.isRecupere()),
                "Il n'y a pas de bagage non récupéré");
        var bgg = catalogueBagage.getBagagesNonRecuperesByVolId(lastVolId);
        assertTrue(bgg.size() > 0);
        for(Baggage b:bgg) {
            assertFalse(b.isRecupere(),
                    "Le bagage "+b.getVol()+"-"+b.getNumero()+" ne devrait " +
                            "pas figurer dans les baggages non recuperes");
        }
    }
}
*/
