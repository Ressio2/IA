package com.quartzinsight.qieam.persistence.mem;

import com.quartzinsight.qieam.model.Friend;
import com.quartzinsight.qieam.model.Friends;
import org.junit.Test;
import static org.junit.Assert.*;

public class FriendDaoInMemTest {
    
    @Test
    public void testGetFriends() {
        FriendDaoInMem instance = FriendDaoInMem.resetInstance();
        Friends friends = instance.getFriends();
        final int actual = friends.getFriends().size();
        assertEquals(4, actual);
    }

    @Test
    public void testDeleteFriend() {
        FriendDaoInMem instance = FriendDaoInMem.resetInstance();
        Friend deletedFriend = instance.deleteFriend("Kratos");
        assertEquals("Kratos", deletedFriend.getName());
        assertEquals(3, instance.getFriends().getFriends().size());
    }

    @Test
    public void testCreateFriend() {
        FriendDaoInMem instance = FriendDaoInMem.resetInstance();
        Friend newFriend = instance.createFriend("Thor");
        assertEquals("Thor", newFriend.getName());
        assertEquals(5, instance.getFriends().getFriends().size());
        
    }
    
}
