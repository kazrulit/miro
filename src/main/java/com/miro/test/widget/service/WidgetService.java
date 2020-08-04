package com.miro.test.widget.service;

import java.util.List;

import com.miro.test.widget.dto.WidgetDto;

public interface WidgetService {
	/**
	 * Get list of all widgets
	 * @return
	 */
	List<WidgetDto> getAll();

	/**
	 * Get widget by Id
	 * @param widgetId
	 * @return
	 */
	WidgetDto getById(String widgetId);

	/**
	 * Create widget
	 * @param widgetDto
	 * @return
	 */
	WidgetDto createWidget(WidgetDto widgetDto);

	/**
	 * Update widget
	 * @param widgetId
	 * @param widgetDto
	 * @return
	 */
	WidgetDto updateWidget(String widgetId, WidgetDto widgetDto);

	/**
	 * Delete widget
	 * @param widgetId
	 */
	void deleteWidget(String widgetId);
}
