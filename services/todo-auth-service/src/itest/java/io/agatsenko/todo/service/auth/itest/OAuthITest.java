/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-11-03
 */
package io.agatsenko.todo.service.auth.itest;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.restassured.RestAssured;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import io.agatsenko.todo.service.auth.security.oauth.OAuthGrantTypes;
import io.agatsenko.todo.service.auth.security.oauth.OAuthScope;
import io.agatsenko.todo.service.common.security.jwt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class OAuthITest extends BaseITest {
    private static final String BROWSER_CLIENT_ID = "browser";
    private static final String BROWSER_CLIENT_PASSWORD = "";

    @Autowired
    private JwtTokenParser tokenParser;

    static Stream<UserInfo> validUserInfos() {
        return Stream.of(
                UserInfo.builder()
                        .username("test_user")
                        .password("test_user")
                        .authority("ROLE_USER")
                        .build(),
                UserInfo.builder()
                        .username("test_usersmanager")
                        .password("test_usersmanager")
                        .authority("ROLE_USERS_MANAGER")
                        .build(),
                UserInfo.builder()
                        .username("test_user_usersmanager")
                        .password("test_user_usersmanager")
                        .authority("ROLE_USER")
                        .authority("ROLE_USERS_MANAGER")
                        .build()
        );
    }

    static Stream<UserInfo> invalidUserInfos() {
        return Stream.of(
                UserInfo.builder()
                        .username("test_user")
                        .password("invalid password")
                        .build(),
                UserInfo.builder()
                        .username("test_user")
                        .password("")
                        .build(),
                UserInfo.builder()
                        .username("invalid username")
                        .password("test_user")
                        .build(),
                UserInfo.builder()
                        .username("")
                        .password("")
                        .build()
        );
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("validUserInfos")
    void shouldCreateOrGetExistingOAuthTokenForBrowserClient(UserInfo userInfo) {
        final var getOrCreateTokenSpec = RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when();

        final var tokenInfo = (Map<String, Object>) getOrCreateTokenSpec
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token_type", equalTo("Bearer"))
                .body("scope", equalTo(OAuthScope.UI.getValue()))
                .body("expires_in", greaterThan(0))
                .body("access_token", is(not(isEmptyOrNullString())))
                .body("refresh_token", is(not(isEmptyOrNullString())))
                .extract().body().as(Map.class);

        isAccessTokenValid((String) tokenInfo.get("access_token"));
        assertThat(tokenInfo.get("access_token")).isNotEqualTo(tokenInfo.get("refresh_token"));
        final var accessToken = checkJwtToken(
                (String) tokenInfo.get("access_token"),
                JwtTokenType.ACCESS,
                BROWSER_CLIENT_ID,
                OAuthScope.UI.getValue(),
                userInfo
        );
        final var refreshToken = checkJwtToken(
                (String) tokenInfo.get("refresh_token"),
                JwtTokenType.REFRESH,
                BROWSER_CLIENT_ID,
                OAuthScope.UI.getValue(),
                userInfo
        );
        assertThat(accessToken.getPayload().getJti()).isNotEqualTo(refreshToken.getPayload().getJti());

        final var secondTokenInfo = (Map<String, Object>) getOrCreateTokenSpec
                .post("/oauth/token")
                .then()
                .extract().body().as(Map.class);
        isEqual(
                tokenInfo,
                secondTokenInfo,
                Set.of("access_token", "refresh_token", "expires_in"),
                Set.of(JwtPayload.EXP_KEY)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUserInfos")
    void shouldReturnBadRequestOnCreateOAuthTokenForBrowserClient(UserInfo userInfo) {
        RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("invalid_grant"))
                .body("error_description", is(not(isEmptyOrNullString())));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldCheckToken() {
        final var userInfo = validUserInfos().findAny().get();

        final var accessToken = (String) RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Map.class).get("access_token");

        RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("token", accessToken)
                .when()
                .post("/oauth/check_token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("active", equalTo(true))
                .body("exp", greaterThan(0))
                .body("user_name", equalTo(userInfo.getUsername()))
                .body("authorities", contains(userInfo.getAuthorities().toArray()))
                .body("client_id", equalTo(BROWSER_CLIENT_ID))
                .body("scope", contains(OAuthScope.UI.getValue()));
    }

    @Test
    void shouldRefreshOAuthToken() {
        final var userInfo = validUserInfos().findAny().get();

        final var srcTokenInfo = RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Map.class);
        final var srcAccessToken = (String) srcTokenInfo.get("access_token");
        final var srcRefreshToken = (String) srcTokenInfo.get("refresh_token");

        final var refreshRequestTokenInfo = RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.REFRESH_TOKEN)
                .formParam("scope", OAuthScope.UI.getValue())
                .formParam("refresh_token", srcRefreshToken)
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Map.class);
        final var actualAccessToken = (String) refreshRequestTokenInfo.get("access_token");
        final var actualRefreshToken = (String) refreshRequestTokenInfo.get("refresh_token");

        assertThat(actualAccessToken).isNotEqualTo(srcAccessToken);
        assertThat(actualRefreshToken).isNotEqualTo(srcRefreshToken);
        isAccessTokenValid(actualAccessToken);
        isAccessTokenInvalid(srcAccessToken);

        final var nextTokenInfo = RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Map.class);

        assertThat(nextTokenInfo.get("access_token")).isEqualTo(actualAccessToken);
        assertThat(nextTokenInfo.get("refresh_token")).isEqualTo(actualRefreshToken);
    }

    @Test
    void shouldRevokeOAuthToken() {
        final var userInfo = validUserInfos().findAny().get();

        final var accessToken = (String) RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Map.class).get("access_token");

        isAccessTokenValid(accessToken);

        RestAssured
                .given()
                .auth().oauth2(accessToken)
                .when()
                .delete("/oauth/ext/token")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        isAccessTokenInvalid(accessToken);
    }

    @Test
    void shouldGetCurrentPrincipal() {
        final var userInfo = validUserInfos().findAny().get();

        final var accessToken = (String) RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("grant_type", OAuthGrantTypes.PASSWORD)
                .formParam("username", userInfo.getUsername())
                .formParam("password", userInfo.getPassword())
                .formParam("scope", OAuthScope.UI.getValue())
                .when()
                .post("/oauth/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Map.class).get("access_token");

        final var resp = RestAssured
                .given()
                .auth().oauth2(accessToken)
                .when()
                .get("/principal/current")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("authorities.authority", contains(userInfo.getAuthorities().toArray()))
                .body("details.tokenValue", equalTo(accessToken))
                .body("details.tokenType", equalTo("Bearer"))
                .body("authenticated", equalTo(true))
                .body("principal.id", is(not(isEmptyOrNullString())))
                .body("principal.username", equalTo(userInfo.getUsername()))
                .body("principal.email", is(not(isEmptyOrNullString())))
                .body("principal.enabled", equalTo(true))
                .body("principal.roles", is(not(empty())))
                .body("principal.authorities.authority", contains(userInfo.getAuthorities().toArray()))
                .body("principal.accountNonExpired", equalTo(true))
                .body("principal.credentialsNonExpired", equalTo(true))
                .body("principal.accountNonLocked", equalTo(true))
                .body("name", equalTo(userInfo.getUsername()))
        ;

    }

    private void isAccessTokenValid(String accessToken) {
        RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("token", accessToken)
                .when()
                .post("/oauth/check_token")
                .then()
                .statusCode(HttpStatus.OK.value())

        ;
    }

    private void isAccessTokenInvalid(String accessToken) {
        RestAssured
                .given()
                .auth().basic(BROWSER_CLIENT_ID, BROWSER_CLIENT_PASSWORD)
                .formParam("token", accessToken)
                .when()
                .post("/oauth/check_token")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("invalid_token"))
        ;
    }

    private void isEqual(JwtToken token1, JwtToken token2, Set<String> excludeJwtPayloadKeys) {
        if (token1 == null) {
            assertThat(token2).isNull();
        }
        else {
            assertThat(token1.getHeader()).isEqualTo(token2.getHeader());
            assertThat(token1.getPayload().toMap()).hasSameSizeAs(token2.getPayload().toMap());
            for (final var key : token1.getPayload().toMap().keySet()) {
                if (excludeJwtPayloadKeys.contains(key)) {
                    continue;
                }
                assertThat(token1.getPayload().toMap().get(key))
                        .overridingErrorMessage(
                                "expected that %s->%s is equal to %s->%s",
                                key,
                                token1.getPayload().toMap().get(key),
                                key,
                                token2.getPayload().toMap().get(key)
                        )
                        .isEqualTo(token2.getPayload().toMap().get(key));
            }
        }
    }

    private void isEqual(
            Map<String, Object> oauthToken1,
            Map<String, Object> oauthToken2,
            Set<String> excludeOAuthTokenKeys,
            Set<String> excludeJwtPayloadKeys) {
        if (oauthToken1 == null) {
            assertThat(oauthToken2).isNull();
        }
        else {
            assertThat(oauthToken1).hasSameSizeAs(oauthToken2);
            for (final var key : oauthToken1.keySet()) {
                if (excludeOAuthTokenKeys.contains(key)) {
                    continue;
                }
                assertThat(oauthToken1.get(key))
                        .overridingErrorMessage(
                                "expected that %s->%s is equal to %s->%s",
                                key,
                                oauthToken1.get(key),
                                key,
                                oauthToken2.get(key)
                        )
                        .isEqualTo(oauthToken2.get(key));
            }
            isEqual(
                    tokenParser.parse((String) oauthToken1.get("access_token")),
                    tokenParser.parse((String) oauthToken2.get("access_token")),
                    excludeJwtPayloadKeys
            );
            isEqual(
                    tokenParser.parse((String) oauthToken1.get("refresh_token")),
                    tokenParser.parse((String) oauthToken2.get("refresh_token")),
                    excludeJwtPayloadKeys
            );
        }
    }

    private JwtToken checkJwtToken(
            String tokenStr,
            JwtTokenType expectedTokenType,
            String expectedClientId,
            String expectedScope,
            UserInfo expectedUserInfo) {
        JwtToken token = tokenParser.parse(tokenStr);

        assertThat(token.getHeader()).isNotNull();
        assertThat(token.getHeader().getTyp()).hasValue(JwtHeader.TYP_JWT_VALUE);
        assertThat(token.getHeader().getAlg()).isNotEmpty();

        assertThat(token.getPayload()).isNotNull();
        assertThat(token.getPayload().getJti()).isNotEmpty();
        assertThat(token.getPayload().getExp()).isNotEmpty();
        assertThat(token.getPayload().getTokenType()).hasValue(expectedTokenType);
        assertThat(token.getPayload().getClientId()).hasValue(expectedClientId);
        assertThat(token.getPayload().getUserId()).isNotEmpty();
        assertThat(token.getPayload().getUsername()).hasValue(expectedUserInfo.getUsername());
        assertThat(token.getPayload().getScope()).contains(expectedScope);
        assertThat(token.getPayload().getAuthorities())
                .hasSameSizeAs(expectedUserInfo.getAuthorities())
                .containsAll(expectedUserInfo.getAuthorities());

        return token;
    }

    @Value
    @Builder
    static class UserInfo {
        private final String username;
        private final String password;
        @Singular
        private final Set<String> authorities;
    }
}
