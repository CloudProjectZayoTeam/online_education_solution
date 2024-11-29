package cloud_project.services;

import cloud_project.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SessionCleanerService {

    @Autowired
    private SessionRepository sessionRepository;

    // Méthode planifiée qui s'exécute toutes les heures
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void cleanExpiredSessions() {
        // Supprimer les sessions expirées de la base de données
        sessionRepository.deleteExpiredSessions();
    }
}