package cloud_project;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "cloud project solution for education in morocco & REST API Documentation",
                description = "cloud project solution for education in morocco & REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "ZayoGroup : Zouhir El Amraoui , Yassine Benhsine , Achraf Jarrou , Othmane Arejdal",
                        email = "zouhir.mpi.isitd@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description =  "cloud project solution for education in morocco",
                url = "http://localhost:8080/swagger-ui.html"
        )
)
public class CloudProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudProjectApplication.class, args);
    }

}
