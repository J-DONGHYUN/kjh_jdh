package project.kjhjdh.ibid.auth.presentation.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.kjhjdh.ibid.auth.application.TokenProvider;
import project.kjhjdh.ibid.auth.domain.UserInfo;
import project.kjhjdh.ibid.common.exception.ErrorCode;
import project.kjhjdh.ibid.common.exception.GlobalException;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    public static final String USER_INFO_ATTRIBUTE = "userInfo";

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			return true;
		}
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        UserInfo userInfo = authenticate(request);
        request.setAttribute(USER_INFO_ATTRIBUTE, userInfo);

        return true;
    }

    private UserInfo authenticate(HttpServletRequest request) {
        String token = extractToken(request);

		Long userId = tokenProvider.parseAccessToken(token);
		return new UserInfo(userId);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
		throw new GlobalException(ErrorCode.UNAUTHORIZED);
    }
}
