package com.bosrouter.server;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class BosrouterServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        Context root = new Context(server,"/",Context.SESSIONS);
        root.addServlet(new ServletHolder(new ServletContainer(new PackagesResourceConfig("com.bosrouter.server.api.resource"))), "/");
        server.start();
    }
}
