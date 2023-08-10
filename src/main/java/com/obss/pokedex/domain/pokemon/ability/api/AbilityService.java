import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbilityService {
    AbilityDto createAbility(AbilityDto dto);

    AbilityDto updateAbility(String id, AbilityDto dto);

    void deleteAbility(String id);

    Page<AbilityDto> getAll(Pageable pageable);

    AbilityDto getById(String id);

    Page<AbilityDto> filterAbilites(AbilityDto dto, Pageable pageable);
}
