/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import java.util.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.auth.model.UserRepo;
import io.agatsenko.todo.util.Check;

@RequiredArgsConstructor
public class UserService {
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public Optional<User> getUser(UUID userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> getUser(String username) {
        return userRepo.findByUsername(username);
    }

    public Collection<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User createUser(NewUserSpec spec) {
        validateNewUserSpec(spec);
        final var user = User.builder()
                .id(UUID.randomUUID())
                .username(spec.getUsername())
                .password(spec.getPassword())
                .email(spec.getEmail())
                .enabled(spec.isEnabled())
                .roles(Set.copyOf(spec.getRoles()))
                .build();
        return userRepo.insert(user);
    }

    public Optional<User> updateUser(UpdateUserSpec spec) {
        validateUpdateUserSpec(spec);
        return userRepo.findByIdAndVersion(spec.getId(), spec.getVersion())
                .map(user -> {
                    final var updatedUser = user.toBuilder()
                            .username(spec.getUsername())
                            .email(spec.getEmail())
                            .enabled(spec.isEnabled())
                            .roles(Set.copyOf(spec.getRoles()))
                            .build();
                    return userRepo.save(updatedUser);
                });
    }

    public Optional<User> changePassword(UUID userId, ChangePasswordSpec spec) {
        return userRepo.findById(userId)
                .map(user -> {
                    validateChangePasswordSpec(user, spec);
                    final var updatedUser = user.toBuilder()
                            .password(passwordEncoder.encode(spec.getNewPassword()))
                            .build();
                    return userRepo.save(updatedUser);
                });
    }

    public boolean deleteUser(UUID userId) {
        if (!userRepo.existsById(userId)) {
            return false;
        }
        userRepo.deleteById(userId);
        return true;
    }

    private <T> void validate(T value) {
        final var violations = validator.validate(value);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void validateNewUserSpec(NewUserSpec userSpec) {
        validate(userSpec);
        Check.state(
                Objects.equals(userSpec.getPassword(), userSpec.getConfirmPassword()),
                "the new password is not equal to the confirm password"
        );
        Check.state(
                !userRepo.existsByUsername(userSpec.getUsername()),
                "a user with the same username already exists"
        );
        Check.state(
                !userRepo.existsByEmail(userSpec.getEmail()),
                "a user with the same email already exists"
        );
    }

    private void validateUpdateUserSpec(UpdateUserSpec spec) {
        validate(spec);
        Check.state(
                !userRepo.existsByIdNotAndUsername(spec.getId(), spec.getUsername()),
                "a user with the same username already exists"
        );
        Check.state(
                !userRepo.existsByIdNotAndEmail(spec.getId(), spec.getEmail()),
                "a user with the same email already exists"
        );
    }

    private void validateChangePasswordSpec(User user, ChangePasswordSpec passwordSpec) {
        validate(passwordSpec);
        Check.state(
                passwordEncoder.matches(passwordSpec.getOldPassword(), user.getPassword()),
                "the old password does not match to the existing password"
        );
        Check.state(
                Objects.equals(passwordSpec.getNewPassword(), passwordSpec.getConfirmNewPassword()),
                "the new password is not equal to the confirm password"
        );
    }
}
