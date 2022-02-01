/*
 *  Copyright (c) 2017-2027 Quartz Insight
 *  This file is part of projects developed by Quartz Insight.
 *  Projects developed by Quartz Insight can not be copied and/or distributed without the express permission of Quartz Insight.
 */
package com.quartzinsight.qieam.controller;

import com.quartzinsight.qieam.persistence.db.StoreDaoInPgSql;
import com.quartzinsight.qieam.persistence.mem.StoreDaoInMem;
import com.quartzinsight.qieam.service.StoreService;
import io.prometheus.client.Counter;
import spark.Request;
import spark.Response;
import spark.Route;

public class StoreController {

    public final Route getGames;
    static final Counter requests = Counter.build()
            .name("requests_total_store").help("Total requests store.").register();

    private final StoreService storeService;
    private static final String env = "DATABASE_URL";

    public StoreController(boolean useJdbc) {
        if (useJdbc) {
            storeService = StoreDaoInPgSql.getInstance(env);
        } else {
            storeService = StoreDaoInMem.getInstance();
        }

        getGames = (Request request, Response response) -> {
            requests.inc();
            return storeService.getGames();
        };

    }
}
