package tiw.is.vols.catalogue.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.catalogue.Catalogue;
import tiw.is.vols.catalogue.dto.VolDTO;
import tiw.is.vols.catalogue.modeles.Companie;
import tiw.is.vols.catalogue.modeles.Vol;

import java.io.IOException;
import java.util.Collection;

public class VolServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private static final Logger log = LoggerFactory.getLogger(VolServlet.class);

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("pu-vol");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.split("/").length <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Vol ID");
            return;
        }
        String volId = pathInfo.split("/")[1];

        try (var em = emf.createEntityManager()) {
            var catalogue = new Catalogue(em);
            var vol = catalogue.getVol(volId);
            if (vol != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), vol);
            } else {
                Collection<Vol> vols = catalogue.getVols();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), vols);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.split("/").length <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Vol ID");
            return;
        }
        String volId = pathInfo.split("/")[1];

        VolDTO volDTO;
        try {
            volDTO = objectMapper.readValue(req.getInputStream(), VolDTO.class);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Vol data");
            return;
        }

        try (EntityManager em = emf.createEntityManager()) {
            Catalogue catalogue = new Catalogue(em);
            Companie companie = catalogue.getCompany(volDTO.companie());
            if (companie == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Company ID");
                return;
            }

           Vol vol = new Vol(volId, companie, volDTO.isDepart(), volDTO.pointLivraisonBagages());

            // persist
            em.getTransaction().begin();
            catalogue.saveVol(vol);
            em.getTransaction().commit();

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), volDTO);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.split("/").length <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Vol ID");
            return;
        }
        String volId = pathInfo.split("/")[1];

        try (var em = emf.createEntityManager()) {
            var catalogue = new Catalogue(em);
            boolean isDeleted = catalogue.deleteVolById(volId);
            if (isDeleted) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Vol not found");
            }
        }
    }

    @Override
    public void destroy() {
        emf.close();
    }
}
