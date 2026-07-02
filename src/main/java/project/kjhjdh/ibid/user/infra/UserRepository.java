package project.kjhjdh.ibid.user.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.kjhjdh.ibid.user.domain.Email;
import project.kjhjdh.ibid.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}