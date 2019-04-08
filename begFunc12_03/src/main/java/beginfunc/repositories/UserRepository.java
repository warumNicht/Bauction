package beginfunc.repositories;

import beginfunc.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u FROM User u " +
            "WHERE u.id NOT LIKE :id")
    List<User> findAllUsersExceptLoggedIn(@Param(value = "id") String loggedInId);



    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (id, username,full_name,password, email,registration_date) " +
            "VALUES (:id,:username,:fullName,:password, :email,:registrationDate)",nativeQuery = true)
    void insertRootUser(@Param("id") String id,
                        @Param("username") String username,
                        @Param("fullName") String fullName,
                        @Param("password") String password,
                        @Param("email") String email,
                        @Param("registrationDate") Date registrationDate);
}
