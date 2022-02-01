/*
 *  Copyright (c) 2017-2027 Quartz Insight
 *  This file is part of projects developed by Quartz Insight.
 *  Projects developed by Quartz Insight can not be copied and/or distributed without the express permission of Quartz Insight.
 */
package com.quartzinsight.qieam;

import com.quartzinsight.qieam.controller.FriendController;
import com.quartzinsight.qieam.controller.StoreController;
import com.quartzinsight.qieam.controller.LibraryController;
import com.quartzinsight.qieam.filter.AuthenticationFilter;
import com.quartzinsight.qieam.filter.TerminateAuthenticationFilter;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;

import java.io.IOException;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;

public class Qieam {

    private static final String APP_JSON = "application/json";

    public static void main(String[] args) throws IOException {

        IniRealm realm = new IniRealm("classpath:shiro.ini");
        DefaultSecurityManager securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);
        String useJdbcEnv = System.getenv().getOrDefault("USE_JDBC", "false");
        boolean useJdbc = Boolean.parseBoolean(useJdbcEnv);
        String customPort = System.getenv("PORT");
        if (customPort != null) {
            port(Integer.parseInt(customPort));
        }
        LibraryController libraryController = new LibraryController(useJdbc);
        StoreController storeController = new StoreController(useJdbc);
        FriendController friendController = new FriendController(useJdbc);

        JsonTransformer jsonTransformer = new JsonTransformer();

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null)
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null)
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        before("/api/*", new AuthenticationFilter());

        path("/api", () -> {
            get("/library/games", APP_JSON, libraryController.getGames, jsonTransformer);

            get("/store/games", APP_JSON, storeController.getGames, jsonTransformer);

            get("/friends", APP_JSON, friendController.getFriends, jsonTransformer);
            
            post("/friend", APP_JSON, friendController.createFriend, jsonTransformer);
            
            delete("/friend", APP_JSON, friendController.deleteFriend, jsonTransformer);
        });
        after(new TerminateAuthenticationFilter());

        HTTPServer server = new HTTPServer(1234);
    }

}
