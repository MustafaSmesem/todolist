package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public Group save(Group group, String userId) {
        if (group.getId() != null && !group.getId().isEmpty()) {
            var groupOptional = groupRepository.findById(group.getId());
            if (groupOptional.isEmpty())
                throw new DocumentNotFoundException("Group", group.getId());
            if (!groupOptional.get().getUser().getId().equals(userId))
                throw new BadRequestException("You dont have the permission to modify another person groups");
        }
        group.setUser(new User(userId));
        return groupRepository.save(group);
    }

    public void deleteGroup(String groupId, String userId) {
        var groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isEmpty())
            throw new DocumentNotFoundException("Group", groupId);
        var group = groupOptional.get();
        if (!group.getUser().getId().equals(userId))
            throw new BadRequestException("You dont have the permission to modify another person groups");
        groupRepository.delete(group);
    }

    public List<Group> getAll(String userId) {
        return groupRepository.findAllByUserId(userId);
    }

}
