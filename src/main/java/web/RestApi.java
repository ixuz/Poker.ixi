package web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iota.ict.IctInterface;
import org.iota.ict.api.*;
import org.iota.ict.utils.Constants;
import org.iota.ict.utils.IOHelper;
import org.iota.ict.utils.properties.FinalProperties;
import org.iota.ict.utils.properties.PropertiesUser;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RestApi extends RestartableThread implements PropertiesUser {

    protected static final Logger LOGGER = LogManager.getLogger("RestAPI");
    protected Service service;
    //protected final JsonIct jsonIct;
    protected FinalProperties properties;
    protected Set<RouteImpl> routes = new HashSet<>();
    protected boolean initialized = false;

    static {
        try {
            if (!new File(Constants.WEB_GUI_PATH).exists())
                IOHelper.extractDirectoryFromJarFile(org.iota.ict.api.RestApi.class, "web/", Constants.WEB_GUI_PATH);
        } catch (IOException e) {
            LOGGER.error("Failed to extract Web GUI into " + new File(Constants.WEB_GUI_PATH).getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }

    public RestApi(/*IctInterface ict*/) {
        super(LOGGER);
        /*this.properties = ict.getProperties();
        this.jsonIct = new JsonIct(ict);*/
    }

    @Override
    public void run() {
        ;
    }

    private void initRoutes() {
        /* TODO: Add routes
        routes.add(new RouteGetInfo(jsonIct));
        routes.add(new RouteGetLogs(jsonIct));
        routes.add(new RouteUpdate(jsonIct));

        routes.add(new RouteSetConfig(jsonIct));
        routes.add(new RouteGetConfig(jsonIct));

        routes.add(new RouteGetNeighbors(jsonIct));
        routes.add(new RouteAddNeighbor(jsonIct));
        routes.add(new RouteRemoveNeighbor(jsonIct));

        routes.add(new RouteGetModules(jsonIct));
        routes.add(new RouteAddModule(jsonIct));
        routes.add(new RouteRemoveModule(jsonIct));
        routes.add(new RouteUpdateModule(jsonIct));

        routes.add(new RouteGetModuleConfig(jsonIct));
        routes.add(new RouteSetModuleConfig(jsonIct));
*/
        initialized = true;
    }

    @Override
    public void onStart() {
        /*
        if (!properties.guiEnabled())
            return;
*/
        if (!initialized)
            initRoutes();

        service = Service.ignite();
        int guiPort = 8888;
        service.port(guiPort);

        service.staticFiles.externalLocation(Constants.WEB_GUI_PATH);
        for (RouteImpl route : routes           )
            service.post(route.getPath(), route);

        service.before(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                if(request.requestMethod().equals("GET") && !request.pathInfo().matches("^[/]?$") && !request.pathInfo().startsWith("/modules/")) {
                    response.redirect("/");
                }
                String queryPassword = request.queryParams("password");
                if (!queryPassword.equals("change_me_now"))
                    service.halt(401, "Access denied: password incorrect.");
            }
        });

        service.after(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Methods", "GET");
            }
        });

        service.init();
        service.awaitInitialization();
        LOGGER.info("Started Web GUI on port " + guiPort + ". Access it by visiting '{HOST}:" + guiPort + "' from your web browser.");
    }

    @Override
    public void onTerminate() {
        if (service == null) // wasn't running
            return;
        for (RouteImpl route : routes)
            service.delete(route.getPath(), route);
        service.stop();
        service = null;
    }

    @Override
    public void updateProperties(FinalProperties newProp) {
        /* TODO: update properties and restart gui
        Properties oldProp = this.properties;
        this.properties = newProp;


        if (oldProp.guiEnabled() && !newProp.guiEnabled())
            terminate();
        else if (!oldProp.guiEnabled() && newProp.guiEnabled())
            start();
        else if (oldProp.guiEnabled() && newProp.guiEnabled() && oldProp.guiPort() != newProp.guiPort()) {
            terminate();
            start();
        }*/
    }
}
