package project.kjhjdh.ibid.auth.presentation.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증된 사용자 정보({@link project.kjhjdh.ibid.auth.domain.UserInfo})를
 * 컨트롤러 파라미터로 주입받기 위한 애노테이션.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}