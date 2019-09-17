/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.model;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, UUID> {
    boolean existsByUsername(String username);

    boolean existsByIdNotAndUsername(UUID id, String username);

    boolean existsByEmail(String email);

    boolean existsByIdNotAndEmail(UUID id, String email);

    Optional<User> findByIdAndVersion(UUID id, long version);

    Optional<User> findByUsername(String username);
}
