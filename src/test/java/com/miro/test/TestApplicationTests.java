package com.miro.test;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.test.widget.dto.WidgetDto;
import com.miro.test.widget.service.WidgetService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestApplicationTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WidgetService widgetService;


	@Test
	void shouldCreateNewWidget() throws Exception {
		List<WidgetDto> widgetDtos = widgetService.getAll();
		Assertions.assertTrue(widgetDtos.isEmpty());

		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setX(1);
		widgetDto.setY(2);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDtos = widgetService.getAll();
		Assertions.assertFalse(widgetDtos.isEmpty());

		widgetService.deleteWidget(widgetDtos.get(0).getUuid());
	}

	@Test
	void shouldCreateNewWidgetAndMoveZIndexOfExistingOnes() throws Exception {
		List<WidgetDto> widgetDtos = widgetService.getAll();
		Assertions.assertTrue(widgetDtos.isEmpty());

		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setX(1);
		widgetDto.setY(2);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDto = new WidgetDto();
		widgetDto.setX(2);
		widgetDto.setY(3);
		widgetDto.setZ(5);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDto = new WidgetDto();
		widgetDto.setX(5);
		widgetDto.setY(5);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDtos = widgetService.getAll();

		Assertions.assertFalse(widgetDtos.isEmpty());

		//First widget
		Assertions.assertEquals(5, widgetDtos.get(0).getX());
		Assertions.assertEquals(5, widgetDtos.get(0).getY());
		Assertions.assertEquals(1, widgetDtos.get(0).getZ());


		//Second widget
		Assertions.assertEquals(1, widgetDtos.get(1).getX());
		Assertions.assertEquals(2, widgetDtos.get(1).getY());
		Assertions.assertEquals(2, widgetDtos.get(1).getZ());

		//Third widget should stay as there was a gap
		Assertions.assertEquals(2, widgetDtos.get(2).getX());
		Assertions.assertEquals(3, widgetDtos.get(2).getY());
		Assertions.assertEquals(5, widgetDtos.get(2).getZ());

		widgetService.deleteWidget(widgetDtos.get(0).getUuid());
		widgetService.deleteWidget(widgetDtos.get(1).getUuid());
		widgetService.deleteWidget(widgetDtos.get(2).getUuid());
	}


	@Test
	void shouldUpdateWidget() throws Exception {
		List<WidgetDto> widgetDtos = widgetService.getAll();
		Assertions.assertTrue(widgetDtos.isEmpty());

		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setX(1);
		widgetDto.setY(2);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		String widgetId = widgetService.getAll().get(0).getUuid();

		WidgetDto updateWidget = new WidgetDto();
		updateWidget.setX(5);
		updateWidget.setY(2);
		updateWidget.setZ(3);

		mockMvc.perform(put("/api/v1/widget/{widgetId}", widgetId)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(updateWidget)))
				.andExpect(status().isOk());

		WidgetDto updatedWidget = widgetService.getAll().get(0);
		Assertions.assertEquals(5, updatedWidget.getX());
		Assertions.assertEquals(2, updatedWidget.getY());
		Assertions.assertEquals(3, updatedWidget.getZ());

		widgetService.deleteWidget(widgetId);
	}

	@Test
	void updateShoulddMoveZIndexOfExistingWidget() throws Exception {
		List<WidgetDto> widgetDtos = widgetService.getAll();
		Assertions.assertTrue(widgetDtos.isEmpty());

		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setX(1);
		widgetDto.setY(2);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDto = new WidgetDto();
		widgetDto.setX(2);
		widgetDto.setY(3);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDto = new WidgetDto();
		widgetDto.setX(5);
		widgetDto.setY(5);
		widgetDto.setZ(1);

		mockMvc.perform(put("/api/v1/widget/{widgetId}",  widgetService.getAll().get(1).getUuid())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		widgetDtos = widgetService.getAll();

		Assertions.assertFalse(widgetDtos.isEmpty());

		//First widget
		Assertions.assertEquals(5, widgetDtos.get(0).getX());
		Assertions.assertEquals(5, widgetDtos.get(0).getY());
		Assertions.assertEquals(1, widgetDtos.get(0).getZ());

		//Second widget
		Assertions.assertEquals(2, widgetDtos.get(1).getX());
		Assertions.assertEquals(3, widgetDtos.get(1).getY());
		Assertions.assertEquals(2, widgetDtos.get(1).getZ());



		widgetService.deleteWidget(widgetDtos.get(0).getUuid());
		widgetService.deleteWidget(widgetDtos.get(1).getUuid());
	}

	@Test
	void shouldDeleteWidget() throws Exception {
		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setX(1);
		widgetDto.setY(2);
		widgetDto.setZ(1);

		mockMvc.perform(post("/api/v1/widget")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(widgetDto)))
				.andExpect(status().isOk());

		WidgetDto createdWidget = widgetService.getAll().get(0);

		mockMvc.perform(delete("/api/v1/widget/{widgetId}", createdWidget.getUuid())
				.contentType("application/json"))
				.andExpect(status().isOk());

		List<WidgetDto> widgetDtos = widgetService.getAll();

		Assertions.assertTrue(widgetDtos.isEmpty());
	}

}
