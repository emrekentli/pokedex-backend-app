import Pokemon;
import Stat;
import com.obss.pokedex.library.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
@Table(name = PokemonStat.TABLE)
public class PokemonStat extends AbstractEntity {
    public static final String TABLE = "pokemon_stat";
    public static final String COL_STAT_POINT= "stat_point";
    public static final String COL_STAT_ID= "stat_id";

    @Column(name = COL_STAT_POINT)
    private Integer statPoint;
    @ManyToOne
    private Stat stat;

    @ManyToOne
    private Pokemon pokemon;
}
