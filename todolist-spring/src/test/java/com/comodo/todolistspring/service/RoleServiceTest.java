package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Role;
import com.comodo.todolistspring.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock private RoleRepository roleRepository;
    private RoleService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RoleService(roleRepository);
    }

    @Test
    void canSaveRole() {
        // given
        var role = new Role();

        // when
        underTest.save(role);

        // then
        var userArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(userArgumentCaptor.capture());
        var capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(role);
    }
}
