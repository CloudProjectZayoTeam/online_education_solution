package cloud_project.repository;

import cloud_project.entity.PendingProfileUpdate;
import cloud_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface PendingProfileUpdateRepository extends JpaRepository<PendingProfileUpdate, Long> {
    @Query("SELECT p FROM PendingProfileUpdate p WHERE p.user = :user ORDER BY p.id DESC LIMIT 1")
    Optional<PendingProfileUpdate> findLatestByUser(User user);
}
