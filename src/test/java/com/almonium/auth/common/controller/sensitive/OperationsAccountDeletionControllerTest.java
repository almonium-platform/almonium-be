package com.almonium.auth.common.controller.sensitive;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.service.SensitiveAuthActionsService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperationsAccountDeletionControllerTest {
    @Mock
    SensitiveAuthActionsService sensitiveAuthActionsService;

    @Mock
    UserService userService;

    @InjectMocks
    OperationsAccountDeletionController controller;

    final User operator = User.builder().id(UUID.randomUUID()).build();

    @Test
    void deletesTheTargetThroughTheSelfServicePath() {
        User target = User.builder().id(UUID.randomUUID()).build();
        when(userService.getById(target.getId())).thenReturn(target);

        controller.delete(target.getId(), operator);

        verify(sensitiveAuthActionsService).deleteAccount(target);
    }

    @Test
    void refusesToDeleteTheOperatorThemselves() {
        assertThatThrownBy(() -> controller.delete(operator.getId(), operator))
                .isInstanceOf(BadUserRequestActionException.class);

        verifyNoInteractions(sensitiveAuthActionsService, userService);
    }
}
