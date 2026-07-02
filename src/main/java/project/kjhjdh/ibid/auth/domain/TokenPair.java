package project.kjhjdh.ibid.auth.domain;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
