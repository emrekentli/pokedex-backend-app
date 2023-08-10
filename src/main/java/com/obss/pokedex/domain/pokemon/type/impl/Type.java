import com.obss.pokedex.library.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Type.TABLE)
public class Type extends AbstractEntity {
    public static final String TABLE = "type";
    public static final String COL_NAME= "name";

    @Column(name = COL_NAME)
    private String name;

}
