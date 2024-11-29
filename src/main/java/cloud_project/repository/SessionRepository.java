package cloud_project.repository;


import cloud_project.entity.Session;
import cloud_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expirationDate < CURRENT_TIMESTAMP")
    void deleteExpiredSessions();

    Optional<Session> findByToken(String token);
    void deleteAllByUser(User user);

}
