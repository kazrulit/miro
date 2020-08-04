package com.miro.test.widget.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class WidgetEntity {
	private String uuid;
	private AtomicInteger x = new AtomicInteger();
	private AtomicInteger y = new AtomicInteger();
	private AtomicInteger z = new AtomicInteger();
	private LocalDate lastModificationDate;
}
