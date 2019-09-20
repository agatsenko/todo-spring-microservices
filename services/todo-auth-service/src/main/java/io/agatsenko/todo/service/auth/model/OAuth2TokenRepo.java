/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.model;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuth2TokenRepo extends MongoRepository<OAuth2Token, String> {
    Optional<OAuth2Token> findByAccessTokenId(UUID id);

    Optional<OAuth2Token> findByRefreshTokenId(UUID id);

    Collection<OAuth2Token> findByClientId(String clientId);

    Collection<OAuth2Token> findByClientIdAndUsername(String clientId, String username);
}
