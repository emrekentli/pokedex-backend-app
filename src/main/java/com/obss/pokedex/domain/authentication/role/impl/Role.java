package com.obss.pokedex.domain.authentication.role.impl;
import com.obss.pokedex.domain.authentication.role.api.RoleDto;
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
@Table(name = Role.TABLE)
public class Role extends AbstractEntity {
    public static final String TABLE = "role";
    public static final String COL_NAME = "name";
    public static final String COL_DISPLAY_NAME = "display_name";


    @Column(name = COL_NAME)
    private String name;

    @Column(name = COL_DISPLAY_NAME)
    private String displayName;

    public RoleDto toDto() {
        return RoleDto.builder()
                .id(this.getId())
                .name(this.getName())
                .displayName(this.getDisplayName())
                .build();
    }
}
