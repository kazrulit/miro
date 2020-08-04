package com.miro.test.widget.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.miro.test.widget.dto.WidgetDto;
import com.miro.test.widget.entity.WidgetEntity;
import com.miro.test.widget.mapper.WidgetEntityDtoMapper;
import com.miro.test.widget.repository.WidgetRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetServiceImpl implements WidgetService {
	// In case if zIndex is not passed for initial widget
	@Value("${default_initial_z_index:0}")
	private final int defaultInitialZIndex = 0;

	private final WidgetRepository widgetRepository;
	private final WidgetEntityDtoMapper widgetEntityDtoMapper;

	public List<WidgetDto> getAll() {
		log.debug("Fetching all widgets ordered by zIndex");
		return widgetRepository.getAllOrderedByZIndex()
				.stream()
				.map(widgetEntityDtoMapper::convert)
				.collect(Collectors.toList());
	}

	public WidgetDto getById(String widgetId) {
		log.debug("Fetching widget: {}", widgetId);
		return widgetEntityDtoMapper.convert(widgetRepository.getById(widgetId));
	}


	public WidgetDto createWidget(WidgetDto widgetDto) {
		WidgetEntity widgetEntity = new WidgetEntity();

		widgetEntity.setUuid(UUID.randomUUID().toString());

		if(widgetDto.getX() != null) {
			widgetEntity.getX().set(widgetDto.getX());
		}

		if(widgetDto.getY() != null) {
			widgetEntity.getY().set(widgetDto.getY());
		}

		if(widgetDto.getZ() != null) {
			widgetEntity.getZ().set(widgetDto.getZ());
		} else {
			int foreGroundZIndex = Optional.ofNullable(widgetRepository.getForeGroundZIndex())
					.orElse(defaultInitialZIndex);
			widgetEntity.getZ().set(foreGroundZIndex);
		}

		WidgetEntity createdWidget = widgetRepository.create(widgetEntity);

		log.info("Created new widget with id: {}", createdWidget.getUuid());
		return widgetEntityDtoMapper.convert(createdWidget);
	}

	public WidgetDto updateWidget(String widgetId, WidgetDto widgetDto) {
		WidgetEntity existingWidgetEntity = widgetRepository.getById(widgetId);
		if (existingWidgetEntity != null) {

			if(widgetDto.getX() != null) {
				existingWidgetEntity.getX().set(widgetDto.getX());
			}

			if(widgetDto.getY() != null) {
				existingWidgetEntity.getY().set(widgetDto.getY());
			}

			Optional.ofNullable(widgetDto.getZ())
					.ifPresent(zIndex -> widgetRepository.updateZIndexForWidget(widgetId, zIndex));

			log.info("Updated widget: {}", widgetId);
			return widgetEntityDtoMapper.convert(existingWidgetEntity);
		}

		throw new WidgetNotFoundException("Widget not found");
	}

	public void deleteWidget(String widgetId) {
		widgetRepository.delete(widgetId);
		log.info("Deleted widget: {}", widgetId);
	}
}
