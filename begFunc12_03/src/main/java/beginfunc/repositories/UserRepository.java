package beginfunc.repositories;

import beginfunc.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u FROM User u " +
            "WHERE u.id NOT LIKE :id")
    List<User> findAllUsersExceptLoggedIn(@Param(value = "id") String loggedInId);
}
