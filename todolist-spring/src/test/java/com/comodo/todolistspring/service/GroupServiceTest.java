package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock private GroupRepository groupRepository;
    private GroupService underTest;


    @BeforeEach
    void setUp() {
        underTest = new GroupService(groupRepository);
    }

    @Test
    void canSaveGroup() {
        // given
        var userId = "userId";
        var group = new Group();
        group.setUser(new User(userId));

        // when
        underTest.save(group, userId);

        // then
        var groupArgumentCaptor = ArgumentCaptor.forClass(Group.class);
        verify(groupRepository).save(groupArgumentCaptor.capture());
        var capturedGroup = groupArgumentCaptor.getValue();

        assertThat(capturedGroup).isEqualTo(group);
    }

    @Test
    void canUpdateGroup() {
        // given
        var userId = "userId";
        var groupId = "groupId";
        var group = new Group();
        group.setUser(new User(userId));
        group.setId(groupId);

        // when
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        underTest.save(group, userId);

        // then
        var userArgumentCaptor = ArgumentCaptor.forClass(Group.class);
        verify(groupRepository).save(userArgumentCaptor.capture());
        var capturedGroup = userArgumentCaptor.getValue();

        assertThat(capturedGroup).isEqualTo(group);
    }


    @Test
    void shouldThrowExceptionIfGroupNotFound() {
        // given
        var userId = "user-id";
        var groupId = "978465132";
        var group = new Group();
        group.setUser(new User(userId));
        group.setId(groupId);

        // when
        when(groupRepository.findById(group.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.save(group, userId))
                .isInstanceOf(DocumentNotFoundException.class)
                .hasMessageContaining(String.format("Cannot found the document(Group) in database: %s", groupId));

    }

    @Test
    void shouldThrowExceptionIfGroupUserNotEqualToAuthenticatedUserId() {
        // given
        var userId = "user-id";
        var anotherUserId = "user-id-another";
        var groupId = "978465132";
        var group = new Group();
        group.setUser(new User(userId));
        group.setId(groupId);

        // when
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        // then
        assertThatThrownBy(() -> underTest.save(group, anotherUserId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("You dont have the permission to modify another person groups");

    }

    @Test
    void canDeleteGroup() {
        //given
        var groupId = "group-id";
        var userId = "user-id";

        var group = new Group();
        group.setId(groupId);
        group.setUser(new User(userId));

        //when
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        underTest.deleteGroup(groupId, userId);

        //then
        verify(groupRepository, times(1)).delete(group);
    }

    @Test
    void itShouldThrowExceptionIfGroupDoesNotExist() {
        //given
        var groupId = "group-id";
        var userId = "user-id";

        var group = new Group();
        group.setId(groupId);
        group.setUser(new User(userId));

        //when
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.deleteGroup(groupId, userId))
                .isInstanceOf(DocumentNotFoundException.class)
                .hasMessageContaining(String.format("Cannot found the document(Group) in database: %s", groupId));
    }

    @Test
    void itShouldThrowExceptionIfGroupUserIdNotEqualToAuthenticatedUserId() {
        //given
        var groupId = "group-id";
        var userId = "user-id";
        var wrongUserId = "wrong-user-id";
        var group = new Group();
        group.setId(groupId);
        group.setUser(new User(userId));

        //when
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        //then
        assertThatThrownBy(() -> underTest.deleteGroup(groupId, wrongUserId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("You dont have the permission to modify another person groups");

    }

    @Test
    void canGetAllGroupsByUserId() {
        // when
        var userId = "testUserId";
        underTest.getAll(userId);
        // then
        verify(groupRepository).findAllByUserId(userId);
    }

}
