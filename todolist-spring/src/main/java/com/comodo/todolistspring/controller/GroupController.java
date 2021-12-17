package com.comodo.todolistspring.controller;

import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.service.GroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<?> saveGroup(@RequestAttribute("userId") String userId, @RequestBody Group formGroup) {
        try{
            var group = groupService.save(formGroup, userId);
            return ResponseEntity.status(HttpStatus.OK).body(group);
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteGroup(@RequestAttribute("userId") String userId, @PathVariable("id") String groupId) {
        try{
            groupService.deleteGroup(groupId, userId);
            return ResponseEntity.ok(String.format("Group [%s] has been removed successfully", groupId));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = "application/json")
    @ApiOperation(value = "Get all groups by userId from Jwt token")
    public ResponseEntity<?> getAllGroups(@RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(groupService.getAll(userId));
    }
}
