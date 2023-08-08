package com.obss.pokedex.domain.pokemon.stat.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StatService {
    StatDto createStat(StatDto dto);

    StatDto updateStat(String id, StatDto dto);

    void deleteStat(String id);

    Page<StatDto> getAll(Pageable pageable);

    StatDto getById(String id);

    Page<StatDto> filterStats(StatDto dto, Pageable pageable);
}
