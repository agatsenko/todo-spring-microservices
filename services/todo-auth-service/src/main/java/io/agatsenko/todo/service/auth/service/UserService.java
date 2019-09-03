/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.auth.model.UserRepo;
import io.agatsenko.todo.util.Check;

public class UserService {
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public UserService(Validator validator, PasswordEncoder passwordEncoder, UserRepo userRepo) {
        Check.argNotNull(validator, "validator");
        Check.argNotNull(passwordEncoder, "passwordEncoder");
        Check.argNotNull(userRepo, "userRepo");
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public User registerNewUser(@Valid NewUserSpec spec) {
        Check.argNotNull(spec, "spec");
        validate(spec);
        return userRepo.insert(createNewUser(spec));
    }

    public Optional<User> getUser(UUID userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> updateUser(UUID userId, UpdateUserSpec spec) {
        Check.argNotNull(spec, "spec");
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    public Optional<User> changeUserPassword(UUID userId, UpdateUserSpec spec) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    public boolean deleteUser(UUID userId) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    private String createPasswordHash(String password) {
        var hash = passwordEncoder.encode(password);
        while (passwordEncoder.upgradeEncoding(hash)) {
            hash = passwordEncoder.encode(password);
        }
        return hash;
    }

    private <T> void validate(T value) {
        final var violations = validator.validate(value);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private User createNewUser(NewUserSpec spec) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(spec.getName())
                .email(spec.getEmail())
                .passwordHash(createPasswordHash(spec.getPassword()))
                .build();
    }
}
