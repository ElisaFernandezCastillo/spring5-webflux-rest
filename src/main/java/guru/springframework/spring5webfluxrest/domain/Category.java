package guru.springframework.spring5webfluxrest.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Data //getters, setters, equals, hash code
/*@Builder
@NoArgsConstructor
@AllArgsConstructor*/
@Document //MongoDB JPA
public class Category {

    @Id
    private String id;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
