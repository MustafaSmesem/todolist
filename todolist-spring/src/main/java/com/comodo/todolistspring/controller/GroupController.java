package com.comodo.todolistspring.controller;

import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.service.GroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = "/save", produces = "application/json")
    public EntityModel<Group> saveGroup(@RequestAttribute("userId") String userId, @Valid @RequestBody Group formGroup) {
        var group = groupService.save(formGroup, userId);
        var model = EntityModel.of(group);
        var linkToAllGroups = linkTo(methodOn(this.getClass()).getAllGroups("user-id"));
        model.add(linkToAllGroups.withRel("all-groups"));
        return model;
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteGroup(@RequestAttribute("userId") String userId, @PathVariable("id") String groupId) {
       groupService.deleteGroup(groupId, userId);
       return ResponseEntity.ok(String.format("Group [%s] has been removed successfully", groupId));
    }

    @GetMapping(value = "/all", produces = "application/json")
    @ApiOperation(value = "Get all groups by userId from Jwt token")
    public ResponseEntity<?> getAllGroups(@RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(groupService.getAll(userId));
    }
}
