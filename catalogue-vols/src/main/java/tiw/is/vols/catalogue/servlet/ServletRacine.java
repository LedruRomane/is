package tiw.is.vols.catalogue.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class ServletRacine extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ServletRacine.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter pw = resp.getWriter()) {
            pw.write("<html><body>Requête sur ");
            pw.write(req.getPathInfo());
            pw.write(" avec ");
            pw.write(req.getQueryString() == null ? "pas de query string" : req.getQueryString());
            pw.write("<ul>");
            for (var e : req.getParameterMap().entrySet()) {
                pw.write("<li>" + e.getKey() + ": " + Arrays.stream(e.getValue()).reduce((x, y) -> x + ", " + y).get() + "</li>");
            }
            pw.write("</ul>");
            pw.write("</body></html>");
        }
    }

}
