package site.devmentor.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsUserByUserId(String userId);

  boolean existsUserByEmail(String email);

  Optional<User> findUserByEmail(String email);
}
