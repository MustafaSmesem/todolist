package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Role;
import com.comodo.todolistspring.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
