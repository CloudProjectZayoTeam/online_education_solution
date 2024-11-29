package cloud_project.config;

public class SecurityConstants {
    public static final String PROF_ROLE = "PROF";
    public static final String ETUDIANT_ROLE = "ETUDIANT";

    public static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/api/public/**"
    };

    public static final String[] PROF_URLS = {
            "/api/prof/**",
            "/api/courses/**"
    };

    public static final String[] ETUDIANT_URLS = {
            "/api/etudiant/**",
            "/api/courses/enroll/**"
    };
}