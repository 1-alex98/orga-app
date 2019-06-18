package org.group_orga.grouporgaapp.service;

import org.group_orga.grouporgaapp.api.OrgaAPIAccessor;
import org.group_orga.grouporgaapp.api.data.GroupOfUsers;

import java.util.List;
import java.util.Locale;

import java8.util.concurrent.CompletableFuture;

public class GroupService {
    private static final GroupService ourInstance = new GroupService();

    private GroupService() {
    }

    public static GroupService getInstance() {
        return ourInstance;
    }

    public CompletableFuture<List<GroupOfUsers>> getMyGroups(){
        String id = UserService.getInstance().getCurrentAccount().getId();
        String filter = String.format(Locale.US, "groupMemberShips.account.id==%s", id);
        return OrgaAPIAccessor.getInstance().getGroupsOfUsersWithFilter(filter);
    }

    public CompletableFuture<GroupOfUsers> createGroup(String name,String password) {
        return OrgaAPIAccessor.getInstance().createGroup(name, password);
    }
}
