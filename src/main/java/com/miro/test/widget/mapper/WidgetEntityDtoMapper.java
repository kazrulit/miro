package com.miro.test.widget.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.miro.test.widget.dto.WidgetDto;
import com.miro.test.widget.entity.WidgetEntity;

@Component
public class WidgetEntityDtoMapper implements Converter<WidgetEntity, WidgetDto> {
	@Override
	public WidgetDto convert(WidgetEntity widgetEntity) {
		WidgetDto widgetDto = new WidgetDto();

		widgetDto.setUuid(widgetEntity.getUuid());

		widgetDto.setX(widgetEntity.getX().get());
		widgetDto.setY(widgetEntity.getY().get());
		widgetDto.setZ(widgetEntity.getZ().get());

		widgetDto.setLastModificationDate(widgetEntity.getLastModificationDate());
		return widgetDto;
	}
}
