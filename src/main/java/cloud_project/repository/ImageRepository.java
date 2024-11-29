package cloud_project.repository;


import cloud_project.entity.Image;
import cloud_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUser(User user);
}
