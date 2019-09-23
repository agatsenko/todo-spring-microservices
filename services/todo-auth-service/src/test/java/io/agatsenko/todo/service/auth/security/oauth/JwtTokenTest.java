/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.oauth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import io.agatsenko.todo.service.common.security.jwt.*;
import io.agatsenko.todo.service.common.security.jwt.jjwt.JjwtTokenParser;
import io.agatsenko.todo.service.common.security.jwt.jjwt.JjwtTokenValueFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenTest {
    private final JwtSigningKeys signingKeys = new JwtSigningKeyPair(Keys.keyPairFor(SignatureAlgorithm.ES256));
    private final JwtTokenValueFactory tokenValueFactory = new JjwtTokenValueFactory(signingKeys);
    private final JwtTokenParser tokenParser = new JjwtTokenParser(JwtTokenSplitter.defaultSplitter, signingKeys);

    @Test
    public void shouldParseTokenWithCheckSignature() {
        final var srcToken = JwtToken.builder()
                .header(JwtHeader.builder().typ(JwtHeader.TYP_JWT_VALUE).build())
                .payload(
                        JwtPayload.builder()
                                .jti(UUID.randomUUID())
                                .exp(Instant.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(3600))
                                .tokenType(JwtTokenType.ACCESS)
                                .clientId(UUID.randomUUID().toString().replace("-", ""))
                                .userId(UUID.randomUUID())
                                .username("test")
                                .scope(Set.of("scope1", "scope2"))
                                .scope("scope3")
                                .authorities(Set.of("authority1", "authority2"))
                                .authority("authority3")
                                .authority(new SimpleGrantedAuthority("authority4"))
                                .put("test key", "test value")
                                .build()
                )
                .build(tokenValueFactory);
        final var parsedToken = tokenParser.parse(srcToken.getTokenValue(), true);

        assertThat(parsedToken.getHeader()).isNotNull();
        assertThat(parsedToken.getHeader().getTyp()).isEqualTo(srcToken.getHeader().getTyp());
        assertThat(parsedToken.getHeader().getAlg()).isPresent();
        assertThat(parsedToken.getHeader().getAlg().get()).isNotEmpty();

        assertThat(parsedToken.getPayload()).isNotNull();
        assertThat(parsedToken.getPayload()).isEqualToComparingFieldByField(srcToken.getPayload());

        assertThat(parsedToken.getSignature()).isEqualTo(srcToken.getSignature());

        assertThat(parsedToken.getTokenValue()).isEqualTo(srcToken.getTokenValue());
    }

    @Test
    public void shouldParseTokenWithoutCheckSignature() {
        final var srcToken = JwtToken.builder()
                .header(JwtHeader.builder().typ(JwtHeader.TYP_JWT_VALUE).build())
                .payload(
                        JwtPayload.builder()
                                .jti(UUID.randomUUID())
                                .exp(Instant.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(3600))
                                .tokenType(JwtTokenType.ACCESS)
                                .clientId(UUID.randomUUID().toString().replace("-", ""))
                                .userId(UUID.randomUUID())
                                .username("test")
                                .scope(Set.of("scope1", "scope2"))
                                .scope("scope3")
                                .authorities(Set.of("authority1", "authority2"))
                                .authority("authority3")
                                .authority(new SimpleGrantedAuthority("authority4"))
                                .put("test key", "test value")
                                .build()
                )
                .build(tokenValueFactory);
        final var parsedToken = tokenParser.parse(srcToken.getTokenValue(), false);

        assertThat(parsedToken.getHeader()).isNotNull();
        assertThat(parsedToken.getHeader().getTyp()).isEqualTo(srcToken.getHeader().getTyp());
        assertThat(parsedToken.getHeader().getAlg()).isPresent();
        assertThat(parsedToken.getHeader().getAlg().get()).isNotEmpty();

        assertThat(parsedToken.getPayload()).isNotNull();
        assertThat(parsedToken.getPayload()).isEqualToComparingFieldByField(srcToken.getPayload());

        assertThat(parsedToken.getSignature()).isEqualTo(srcToken.getSignature());

        assertThat(parsedToken.getTokenValue()).isEqualTo(srcToken.getTokenValue());
    }
}
