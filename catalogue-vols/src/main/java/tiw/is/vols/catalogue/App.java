package tiw.is.vols.catalogue;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.catalogue.servlet.ServletRacine;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        // on utilise le répertoire courant comme répertoire de travail
        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        // une servlet pour tester
        Tomcat.addServlet(ctx, "RootS", new ServletRacine());
        ctx.addServletMappingDecoded("/*", "RootS");

        log.info("Tomcat start...");
        tomcat.start();
        log.info("Tomcat started");
        tomcat.getServer().await();
        log.info("Tomcat terminated");
    }

}
