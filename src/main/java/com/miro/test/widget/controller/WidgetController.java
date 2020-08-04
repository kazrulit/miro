package com.miro.test.widget.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miro.test.widget.dto.WidgetDto;
import com.miro.test.widget.service.WidgetService;

@RestController
@RequestMapping("/api/v1/widget")
@RequiredArgsConstructor
public class WidgetController {
	private final WidgetService widgetService;

	@PostMapping
	public ResponseEntity<WidgetDto> createWidget(@RequestBody WidgetDto widgetDto) {
		WidgetDto widget = widgetService.createWidget(widgetDto);

		return ResponseEntity.ok(widget);
	}

	@PutMapping("/{widgetId}")
	public ResponseEntity<WidgetDto> updateWidget(@PathVariable("widgetId") String widgetId, @RequestBody WidgetDto widgetDto) {
		WidgetDto widget = widgetService.updateWidget(widgetId, widgetDto);

		return ResponseEntity.ok(widget);
	}

	@GetMapping("/{widgetId}")
	public ResponseEntity<WidgetDto> getWidget(@PathVariable("widgetId") String widgetId) {
		WidgetDto widget = widgetService.getById(widgetId);

		return ResponseEntity.ok(widget);
	}

	@GetMapping
	public ResponseEntity<List<WidgetDto>> getAll() {
		List<WidgetDto> widgetDtos = widgetService.getAll();

		return ResponseEntity.ok(widgetDtos);
	}


	@DeleteMapping("/{widgetId}")
	public void updateWidget(@PathVariable("widgetId") String widgetId) {
		widgetService.deleteWidget(widgetId);
	}
}
