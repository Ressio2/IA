/*
 *  Copyright (c) 2017-2027 Quartz Insight
 *  This file is part of projects developed by Quartz Insight.
 *  Projects developed by Quartz Insight can not be copied and/or distributed without the express permission of Quartz Insight.
 */
package com.quartzinsight.qieam.controller;

import com.quartzinsight.qieam.persistence.db.LibraryDaoInPgSql;
import com.quartzinsight.qieam.persistence.mem.LibraryDaoInMem;
import com.quartzinsight.qieam.service.LibraryService;
import io.prometheus.client.Counter;
import spark.Request;
import spark.Response;
import spark.Route;

public class LibraryController {

    public final Route getGames;
    static final Counter requests = Counter.build()
            .name("requests_total_library").help("Total requests library.").register();

    private final LibraryService libraryService;
    private static final String env = "DATABASE_URL";

    public LibraryController(boolean useJdbc) {
        if (useJdbc) {
            libraryService = LibraryDaoInPgSql.getInstance(env);
        } else {
            libraryService = LibraryDaoInMem.getInstance();
        }

        getGames = (Request request, Response response) -> {
            requests.inc();
            return libraryService.getGames();
        };

    }
}
