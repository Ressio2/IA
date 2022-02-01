/*
 *  Copyright (c) 2017-2027 Quartz Insight
 *  This file is part of projects developed by Quartz Insight.
 *  Projects developed by Quartz Insight can not be copied and/or distributed without the express permission of Quartz Insight.
 */
package com.quartzinsight.qieam.controller;

import com.quartzinsight.qieam.service.FriendService;
import com.quartzinsight.qieam.persistence.mem.FriendDaoInMem;
import com.quartzinsight.qieam.persistence.db.FriendDaoInPgSql;
import io.prometheus.client.Counter;
import spark.Request;
import spark.Response;
import spark.Route;

public class FriendController {

    public final Route getFriends;
    public final Route createFriend;
    public final Route deleteFriend;
    
    static final Counter requests = Counter.build()
            .name("requests_total_friends").help("Total requests friends.").register();

    private final FriendService friendService;
    private static final String env = "DATABASE_URL";

    public FriendController(boolean useJdbc) {
        if (useJdbc) {
            friendService = FriendDaoInPgSql.getInstance(env);
        } else {
            friendService = FriendDaoInMem.getInstance();
        }

        getFriends = (Request request, Response response) -> {
            requests.inc();
            return friendService.getFriends();
        };
        
        createFriend = (Request request, Response response) -> {
            final String friendName = request.queryParams("name");
            return friendService.createFriend(friendName);
        };
        
        deleteFriend = (Request request, Response response) -> {
            final String friendName = request.queryParams("name");
            return friendService.deleteFriend(friendName);
        };

    }
}
