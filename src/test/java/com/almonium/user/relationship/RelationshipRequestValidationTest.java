package com.almonium.user.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.user.relationship.dto.request.FriendshipRequestDto;
import com.almonium.user.relationship.dto.request.RelationshipActionDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RelationshipRequestValidationTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void friendshipRequestRequiresRecipientId() {
        assertThat(validator.validate(new FriendshipRequestDto(null)))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("recipientId");
    }

    @Test
    void relationshipActionRequiresAction() {
        assertThat(validator.validate(new RelationshipActionDto(null)))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("action");
    }
}
