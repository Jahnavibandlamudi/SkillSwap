package com.skillswap.server;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.BindException;
import java.net.ServerSocket;

/**
 * Embedded Apache Tomcat launcher for SkillSwap SRM AP.
 * Enables 1-click execution without requiring an external Tomcat installation.
 */
public class EmbeddedTomcatServer {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int requestedPort = DEFAULT_PORT;
        String portStr = System.getenv("PORT");
        if (portStr != null) {
            try {
                requestedPort = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("PORT must be a number between 1 and 65535.", e);
            }
        }
        int port = findAvailablePort(requestedPort, portStr != null);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // Initialize default connector

        String webappDirLocation = "src/main/webapp";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists()) {
            webappDir = new File("webapp");
        }

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/skillswap", webappDir.getAbsolutePath());
        // Maven's exec plugin loads Tomcat in its own classloader. Make that
        // loader the webapp parent so web.xml can resolve Tomcat descriptor types.
        ctx.setParentClassLoader(EmbeddedTomcatServer.class.getClassLoader());
        ctx.setDelegate(true);
        ctx.setReloadable(true);

        // Declare alternative location for 'WEB-INF/classes'
        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        System.out.println("=========================================================");
        System.out.println("  SKILLSWAP - SRM UNIVERSITY AP COLLABORATIVE PLATFORM  ");
        System.out.println("=========================================================");
        System.out.println("  Server URL: http://localhost:" + port + "/skillswap");
        System.out.println("  Student Login: aarav_sharma@srmap.edu.in / Password@123");
        System.out.println("  Admin Login:   admin@srmap.edu.in / Admin@123");
        System.out.println("=========================================================");

        tomcat.start();
        tomcat.getServer().await();
    }

    private static int findAvailablePort(int requestedPort, boolean explicitPort) throws Exception {
        if (requestedPort < 1 || requestedPort > 65535) {
            throw new IllegalArgumentException("PORT must be between 1 and 65535.");
        }
        int lastPort = explicitPort ? requestedPort : Math.min(requestedPort + 20, 65535);
        for (int candidate = requestedPort; candidate <= lastPort; candidate++) {
            try (ServerSocket socket = new ServerSocket(candidate)) {
                socket.setReuseAddress(false);
                return candidate;
            } catch (BindException busy) {
                if (explicitPort) {
                    throw new IllegalStateException("Port " + requestedPort + " is already in use. "
                            + "Stop the existing server or unset PORT to let SkillSwap choose a free port.", busy);
                }
            }
        }
        throw new IllegalStateException("No free port found between " + requestedPort + " and " + lastPort + ".");
    }
}
