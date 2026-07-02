package project.kjhjdh.ibid.auth.presentation.cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private final long refreshExpiration;

    public RefreshTokenCookieHandler(@Value("${jwt.refresh.expiration}") long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpiration / 1000)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie removeRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
