package ru.tbcarus.jrfinal.model.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.tbcarus.jrfinal.model.TickerData;
import ru.tbcarus.jrfinal.model.dto.TickerDataDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TickerDataMapper {

    TickerDataDto toDto(TickerData tickerData);
}