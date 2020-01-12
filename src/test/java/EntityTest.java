import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EntityTest")
public class EntityTest{

    @Id
    private long id;

    @Column
    private String description;


    public EntityTest(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public EntityTest(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}