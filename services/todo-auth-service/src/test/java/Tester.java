import java.security.Key;
import java.security.KeyPair;
import java.time.Instant;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwt;
import io.jsonwebtoken.security.Keys;
import lombok.Value;

import io.agatsenko.todo.util.Check;

/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */

// FIXME: this class must be removed
public class Tester {
    public static void main(String[] args) {
        testWithCustomKeys();
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        testWithSeveralAlgoritm();
    }

    static void testWithCustomKeys() {
        final var keysFactory = new KeyStoreKeyFactory(
                new ClassPathResource("/jwt-keystore.jks"),
                "todo-spring-microservices".toCharArray()
        );
        final var keyPair = keysFactory.getKeyPair("jwt", "todo-spring-microservices".toCharArray());
        final var jwtValue = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(Map.of(
                        "jti", UUID.randomUUID().toString(),
                        "exp", new Date(Instant.now().plusSeconds(3600).toEpochMilli()),
                        "username", "test_user",
                        "authorities", List.of("ROLE_USER"),
                        "clientid", "swagger",
                        "scope", Set.of("server", "ui")
                ))
                .signWith(keyPair.getPrivate())
                .compact();
        final var jwt = Jwts.parser().setSigningKey(keyPair.getPublic()).parse(jwtValue);

        System.out.println("token value: " + jwtValue);
        System.out.println("jwt:         " + jwt);
    }

    static void testWithSeveralAlgoritm() {
        for (final var algorithm : SignatureAlgorithm.values()) {
            if (!algorithm.isJdkStandard()) {
                continue;
            }

            final var signer = createJjwtSigner(algorithm);

            final var jwtBuilder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(Map.of(
                        "jti", UUID.randomUUID().toString(),
                        "exp", new Date(Instant.now().minusSeconds(3600).toEpochMilli()),
                        "username", "test_user",
                        "authorities", List.of("ROLE_USER"),
                        "clientid", "swagger",
                        "scope", Set.of("server", "ui")
                ));
            final var jwtValue = signer.sign(jwtBuilder).compact();
            final var jwt = parseJwtValue(signer, jwtValue);

            System.out.println("algorithm:   " + algorithm);
            System.out.println("token value: " + jwtValue);
            System.out.println("jwt:         " + jwt);
            System.out.println();
        }
    }

    static Signer createJjwtSigner(SignatureAlgorithm algorithm) {
        return algorithm.isHmac() ? new HmacSigner(algorithm) : new KeyPairSigner(algorithm);
    }

    @SuppressWarnings("unchecked")
    static Jwt parseJwtValue(Signer signer, String jwtValue) {
        try {
            return signer.setSignerKey(Jwts.parser()).parse(jwtValue);
        }
        catch (ExpiredJwtException ex) {
            return new DefaultJwt(ex.getHeader(), ex.getClaims());
        }
    }

    interface Signer {
        SignatureAlgorithm getAlgorithm();

        JwtBuilder sign(JwtBuilder builder);

        JwtParser setSignerKey(JwtParser parser);
    }

    @Value
    static class HmacSigner implements Signer {
        private final SignatureAlgorithm algorithm;
        private final Key key;

        public HmacSigner(SignatureAlgorithm algorithm) {
            Check.argNotNull(algorithm, "algorithm");
            Check.arg(algorithm.isJdkStandard(), "algorithm should belong to the JDK standard algorithms");
            Check.arg(algorithm.isHmac(), "algorithm should belong to the HMAC family");
            this.algorithm = algorithm;
            this.key = Keys.secretKeyFor(algorithm);
        }

        @Override
        public SignatureAlgorithm getAlgorithm() {
            return algorithm;
        }

        @Override
        public JwtBuilder sign(JwtBuilder builder) {
            return builder.signWith(key, algorithm);
        }

        @Override
        public JwtParser setSignerKey(JwtParser parser) {
            return parser.setSigningKey(key);
        }
    }

    @Value
    static class KeyPairSigner implements Signer {
        private final SignatureAlgorithm algorithm;
        private final KeyPair keyPair;

        public KeyPairSigner(SignatureAlgorithm algorithm) {
            Check.argNotNull(algorithm, "algorithm");
            Check.arg(algorithm.isJdkStandard(), "algorithm should belong to the JDK standard algorithms");
            Check.arg(
                    algorithm.isRsa() || algorithm.isEllipticCurve(),
                    "algorithm should belong to the RSA or Elliptic Curve families"
            );
            this.algorithm = algorithm;
            this.keyPair = Keys.keyPairFor(algorithm);
        }

        @Override
        public SignatureAlgorithm getAlgorithm() {
            return algorithm;
        }

        @Override
        public JwtBuilder sign(JwtBuilder builder) {
            return builder.signWith(keyPair.getPrivate(), algorithm);
        }

        @Override
        public JwtParser setSignerKey(JwtParser parser) {
            return parser.setSigningKey(keyPair.getPublic());
        }
    }
}
