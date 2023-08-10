package com.obss.pokedex.domain.email.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDto {
    private final String to;
    private final String subject;
    private final String text;
}
