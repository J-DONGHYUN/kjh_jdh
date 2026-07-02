package project.kjhjdh.ibid.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kjhjdh.ibid.common.exception.BusinessException;
import project.kjhjdh.ibid.common.exception.ErrorCode;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final int USERNAME_MIN_LENGTH = 4;
    private static final int USERNAME_MAX_LENGTH = 8;
    private static final int PASSWORD_MIN_LENGTH = 4;
    private static final int PASSWORD_MAX_LENGTH = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    private User(String email, String encodedPassword, String username) {
        validateUsername(username);
        this.email = new Email(email);
        this.password = encodedPassword;
        this.username = username;
    }

    /**
     * 이미 인코딩된 비밀번호로 사용자를 생성한다.
     * 요청 DTO의 Bean Validation과 별개로, 도메인 불변식을 여기서 다시 보장한다.
     * 이메일 형식은 {@link Email} 값 객체가, 유저이름 길이는 이 생성자가 검증한다.
     * 원문 비밀번호 길이는 인코딩 전에 {@link #validateRawPassword(String)}로 검증한다.
     */
    public static User create(String email, String encodedPassword, String username) {
        return new User(email, encodedPassword, username);
    }

    public static void validateRawPassword(String rawPassword) {
        if (rawPassword == null
                || rawPassword.length() < PASSWORD_MIN_LENGTH
                || rawPassword.length() > PASSWORD_MAX_LENGTH) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD_LENGTH);
        }
    }

    private void validateUsername(String username) {
        if (username == null
                || username.length() < USERNAME_MIN_LENGTH
                || username.length() > USERNAME_MAX_LENGTH) {
            throw new BusinessException(ErrorCode.INVALID_USERNAME_LENGTH);
        }
    }
}