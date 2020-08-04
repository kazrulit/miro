package com.miro.test.widget.repository;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.miro.test.widget.entity.WidgetEntity;

@Slf4j
@Repository
public class InMemoryWidgetRepositoryImpl implements WidgetRepository {
	private final Map<String, WidgetEntity> widgetStorage = new ConcurrentHashMap<>();
	private final NavigableMap<Integer, String> zIndexWidgetIdMap = new ConcurrentSkipListMap<>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public List<WidgetEntity> getAllOrderedByZIndex() {
		lock.readLock().lock();
		try {
			return zIndexWidgetIdMap.values().stream()
					.map(widgetStorage::get)
					.collect(Collectors.toList());
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public WidgetEntity getById(String widgetId) {
		lock.readLock().lock();
		try {
			return widgetStorage.get(widgetId);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public WidgetEntity create(WidgetEntity widgetEntity) {
		lock.writeLock().lock();
		try {
			widgetEntity.setLastModificationDate(LocalDate.now());

			int zIndex = widgetEntity.getZ().get();

			String widgetId = widgetEntity.getUuid();

			incrementZIndexOfWidgets(zIndex);

			zIndexWidgetIdMap.put(zIndex, widgetId);
			widgetStorage.put(widgetEntity.getUuid(), widgetEntity);

			return widgetEntity;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * In case if widgets z-index is being changed, we need to move up other widgets z-indexes
	 *
	 * @param widgetId
	 * @param zIndex
	 * @return
	 */
	@Override
	public WidgetEntity updateZIndexForWidget(String widgetId, Integer zIndex) {
		WidgetEntity existingWidgetEntity = getById(widgetId);

		lock.writeLock().lock();
		try {
			int existingZIndex = existingWidgetEntity.getZ().get();
			if (zIndex != existingZIndex) {
				zIndexWidgetIdMap.remove(existingZIndex);
				incrementZIndexOfWidgets(zIndex);
				zIndexWidgetIdMap.put(zIndex, widgetId);

				existingWidgetEntity.getZ().set(zIndex);
			}
			return existingWidgetEntity;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void delete(String widgetId) {
		lock.writeLock().lock();
		try {
			WidgetEntity removedWidget = widgetStorage.remove(widgetId);
			zIndexWidgetIdMap.remove(removedWidget.getZ().get());
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Integer getForeGroundZIndex() {
		lock.readLock().lock();
		try {
			return !zIndexWidgetIdMap.isEmpty() ? zIndexWidgetIdMap.firstKey() - 1 : null;
		} finally {
			lock.readLock().unlock();
		}
	}

	private void incrementZIndexOfWidgets(int zIndex) {
		if (zIndexWidgetIdMap.containsKey(zIndex)) {
			log.info("Incrementing elements from: {}", zIndex);

			int previousInteger = zIndex - 1;

			for (Map.Entry<Integer, String> entry : zIndexWidgetIdMap.tailMap(zIndex, true).entrySet()) {
				Integer existingZIndex = entry.getKey();
				String existingWidgetId = entry.getValue();
				if (existingZIndex == previousInteger + 1) {
					int incrementedZIndex = widgetStorage.get(existingWidgetId).getZ().incrementAndGet();
					zIndexWidgetIdMap.put(incrementedZIndex, existingWidgetId);
				} else {
					//Stop incrementing since there is a gap with the current and previous widget
					break;
				}
			}
		}
	}
}
