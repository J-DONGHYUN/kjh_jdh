package project.kjhjdh.ibid.user.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.kjhjdh.ibid.fixture.UserFixture;
import project.kjhjdh.ibid.support.RepositoryTestSupport;
import project.kjhjdh.ibid.user.domain.Email;
import project.kjhjdh.ibid.user.domain.User;

class UserRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일 값 객체로 사용자를 조회한다")
    @Test
    void findByEmail() {
        // given
        userRepository.save(UserFixture.user().email("find@example.com").username("finder").build());

        // when
        Optional<User> found = userRepository.findByEmail(new Email("find@example.com"));

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("finder");
    }

    @DisplayName("가입된 이메일이면 존재 여부가 참이다")
    @Test
    void existsByEmail() {
        // given
        userRepository.save(UserFixture.user().email("exists@example.com").build());

        // when
        boolean exists = userRepository.existsByEmail(new Email("exists@example.com"));

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("가입되지 않은 이메일이면 존재 여부가 거짓이다")
    @Test
    void existsByEmail_notFound() {
        // when
        boolean exists = userRepository.existsByEmail(new Email("none@example.com"));

        // then
        assertThat(exists).isFalse();
    }
}
