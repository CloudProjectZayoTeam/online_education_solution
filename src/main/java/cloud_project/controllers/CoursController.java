package cloud_project.controllers;

import cloud_project.dtos.CoursDTO;
import cloud_project.entity.Cours;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
public class CoursController {

    private final CoursService coursService;

    @PostMapping
    public ResponseEntity<Cours> creerCours(
            @Valid @RequestBody CoursDTO coursDTO,
            @AuthenticationPrincipal Prof prof) {
        return new ResponseEntity<>(coursService.creerCours(coursDTO, prof), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Cours>> rechercherCours(
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(coursService.rechercherCours(keyword, pageable));
    }

    @GetMapping("/mes-cours")
    public ResponseEntity<Page<Cours>> getMesCours(
            @AuthenticationPrincipal Prof prof,
            Pageable pageable) {
        return ResponseEntity.ok(coursService.getCoursByProf(prof, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cours> modifierCours(
            @PathVariable Long id,
            @Valid @RequestBody CoursDTO coursDTO,
            @AuthenticationPrincipal Prof prof) {
        return ResponseEntity.ok(coursService.modifierCours(id, coursDTO, prof));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCours(
            @PathVariable Long id,
            @AuthenticationPrincipal Prof prof) {
        coursService.supprimerCours(id, prof);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCours(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.getCours(id));
    }
}