package project.kjhjdh.ibid.user.domain;

import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import project.kjhjdh.ibid.common.exception.BusinessException;
import project.kjhjdh.ibid.common.exception.ErrorCode;

/**
 * 이메일 값 객체. 생성 시점에 형식을 검증하므로 유효하지 않은 이메일로는 인스턴스가 만들어지지 않는다.
 */
@Embeddable
public record Email(
        @Column(name = "email", nullable = false, unique = true)
        String value
) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }
}
