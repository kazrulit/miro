package com.miro.test.widget.repository;

import java.util.List;

import com.miro.test.widget.entity.WidgetEntity;


public interface WidgetRepository {
	List<WidgetEntity> getAllOrderedByZIndex();

	WidgetEntity getById(String widgetId);

	WidgetEntity create(WidgetEntity widgetEntity);

	WidgetEntity updateZIndexForWidget(String widgetId, Integer zIndex);

	void delete(String widgetId);

	Integer getForeGroundZIndex();
}
