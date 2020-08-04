package com.miro.test.widget.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WidgetDto {
	private String uuid;
	private Integer x;
	private Integer y;
	private Integer z;
	private LocalDate lastModificationDate;
}
