package linguarium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import linguarium.auth.common.entity.Principal;
import linguarium.user.core.dto.LanguageUpdateRequest;
import linguarium.user.core.dto.UserInfo;
import linguarium.user.core.dto.UsernameAvailability;
import linguarium.user.core.dto.UsernameUpdateRequest;
import linguarium.user.core.service.LearnerService;
import linguarium.user.core.service.UserService;
import linguarium.util.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    LearnerService learnerService;

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getCurrentUser(@CurrentUser Principal auth) {
        return ResponseEntity.ok(userService.buildUserInfoFromUser(auth.getUser()));
    }

    @GetMapping("/{username}/availability/")
    public ResponseEntity<UsernameAvailability> checkUsernameAvailability(@PathVariable String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(new UsernameAvailability(isAvailable));
    }

    @PutMapping("/me/username")
    public ResponseEntity<Void> updateUsername(
            @RequestBody UsernameUpdateRequest request, @CurrentUser Principal auth) {
        userService.changeUsernameById(request.newUsername(), auth.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/account")
    public ResponseEntity<Void> deleteCurrentUserAccount(@CurrentUser Principal auth) {
        userService.deleteAccount(auth.getUser());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/target-langs")
    public ResponseEntity<Void> updateTargetLanguages(
            @RequestBody LanguageUpdateRequest request, @CurrentUser Principal auth) {
        learnerService.updateTargetLanguages(request.langCodes(), auth.getUser().getLearner());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/fluent-langs")
    public ResponseEntity<Void> updateFluentLanguages(
            @RequestBody LanguageUpdateRequest request, @CurrentUser Principal auth) {
        learnerService.updateFluentLanguages(request.langCodes(), auth.getUser().getLearner());
        return ResponseEntity.noContent().build();
    }
}
