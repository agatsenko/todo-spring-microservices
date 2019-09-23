/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskListRepo extends MongoRepository<TaskList, UUID> {
    boolean existsByUserIdAndIdAndVersion(UUID userId, UUID id, long version);

    Collection<TaskList> findByUserId(UUID userId);

    Optional<TaskList> findByUserIdAndId(UUID userId, UUID id);

    Optional<TaskList> findByUserIdAndIdAndVersion(UUID userId, UUID id, long version);

    void deleteByUserIdAndIdAndVersion(UUID userId, UUID id, long version);
}
