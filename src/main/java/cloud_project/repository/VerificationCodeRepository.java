package cloud_project.repository;

import cloud_project.entity.User;
import cloud_project.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationCode vc WHERE vc.expirationDate < CURRENT_TIMESTAMP")
    void deleteExpiredVerificationCodes();

    void deleteAllByUser(User user);
}
