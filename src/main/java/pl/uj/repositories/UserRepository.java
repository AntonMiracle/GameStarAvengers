package pl.uj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.uj.model.User;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username=:username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.role=:role")
    Set<User> findAllWithRole(@Param("role") User.Role role);
}
