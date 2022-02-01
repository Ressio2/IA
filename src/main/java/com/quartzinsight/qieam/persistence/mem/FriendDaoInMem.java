/*
 *  Copyright (c) 2017-2027 Quartz Insight
 *  This file is part of projects developed by Quartz Insight.
 *  Projects developed by Quartz Insight can not be copied and/or distributed without the express permission of Quartz Insight.
 */
package com.quartzinsight.qieam.persistence.mem;

import com.quartzinsight.qieam.model.Friend;
import com.quartzinsight.qieam.model.Friends;
import com.quartzinsight.qieam.service.FriendService;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FriendDaoInMem implements FriendService {

    private static FriendDaoInMem instance;
    private static final Logger LOGGER = Logger.getLogger(FriendDaoInMem.class.getName());
    private final Friends friends;

    private FriendDaoInMem() {
        friends = new Friends();
        friends.addFriend(new Friend("Kratos"));
        friends.addFriend(new Friend("Atreus"));
        friends.addFriend(new Friend("Thanos"));
        friends.addFriend(new Friend("Minerva"));
    }

    public static FriendDaoInMem resetInstance() {
        instance = new FriendDaoInMem();
        return instance;
    }

    public static FriendDaoInMem getInstance() {
        if (instance == null) {
            instance = new FriendDaoInMem();
        }
        return instance;
    }

    @Override
    public Friends getFriends() {
        LOGGER.log(Level.INFO, "Get friends");
        return friends;
    }

    @Override
    public Friend createFriend(String friendName) {
        LOGGER.log(Level.INFO, "Create friend {0}", friendName);
        Friend friend = new Friend(friendName);
        friends.addFriend(friend);
        return friend;
    }

    @Override
    public Friend deleteFriend(String friendName) {
        LOGGER.log(Level.INFO, "Delete friend {0}", friendName);
        Optional<Friend> friendToDelete = friends.getFriends().stream().filter(friend -> friendName.equalsIgnoreCase(friend.getName())).findFirst();
        Friend friend = friendToDelete.orElse(new Friend(""));
        friends.deleteFriend(friend);
        return friend;
    }

}
