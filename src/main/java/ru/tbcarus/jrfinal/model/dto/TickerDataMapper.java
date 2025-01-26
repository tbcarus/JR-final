package ru.tbcarus.jrfinal.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.tbcarus.jrfinal.model.TickerData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TickerDataMapper {

    TickerDataDto toDto(TickerData tickerData);
}