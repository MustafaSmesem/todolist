package com.comodo.todolistspring.repository;

import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.document.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
class GroupRepositoryTest {

    @Autowired private GroupRepository underTest;
    @Autowired private UserRepository userRepository;

    @Test
    void itShouldGetAllGroupsRelatedToUser() {
        // given
        var users = createUsers(3);
        var groups_0 = createGroups(3, users.get(0));
        var groups_1 = createGroups(5, users.get(1));
        var groups_2 = createGroups(0, users.get(2));

        // where
        var expected_group_0 = underTest.findAllByUserId(users.get(0).getId());
        var expected_group_1 = underTest.findAllByUserId(users.get(1).getId());
        var expected_group_2 = underTest.findAllByUserId(users.get(2).getId());

        //then
        assertThat(expected_group_0).isEqualTo(groups_0);
        assertThat(expected_group_1).isEqualTo(groups_1);
        assertThat(expected_group_2).isEqualTo(groups_2);

    }

    private List<User> createUsers(int size) {
        var users = new ArrayList<User>();
        for (int i=0; i< size; i++) {
            var user = new User(null, "user-" + i);
            user = userRepository.save(user);
            users.add(user);
        }
        return users;
    }

    private List<Group> createGroups(int size, User user) {
        var groups = new ArrayList<Group>();
        for (int i=0; i< size; i++) {
            var group = new Group();
            group.setUser(user);
            group.setTitle("Group-" + user.getUsername() + "_" + i);
            group = underTest.save(group);
            groups.add(group);
        }
        return groups;
    }



}
