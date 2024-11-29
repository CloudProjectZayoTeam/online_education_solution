package cloud_project.services;

import cloud_project.repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeCleanerService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    // Méthode planifiée qui s'exécute toutes les heures pour nettoyer les codes expirés
    @Scheduled(cron = "0 0 * * * ?") // Cette expression Cron signifie "toutes les heures"
    @Transactional
    public void cleanExpiredVerificationCodes() {
        verificationCodeRepository.deleteExpiredVerificationCodes();
    }
}
