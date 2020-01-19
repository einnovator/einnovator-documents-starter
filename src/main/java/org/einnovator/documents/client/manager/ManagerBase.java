package org.einnovator.documents.client.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.einnovator.util.MapUtil;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.cache.CachingManagerBase;
import org.springframework.util.StringUtils;

public class ManagerBase extends CachingManagerBase {

	public static final String NOTIFICATION_TYPE = "Notification";


	protected <T> T getNotificationSource(Object data, Class<T> type) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}
		return MappingUtils.convert(MapUtil.resolve("source.details", data), type);
	}

	protected String getNotificationAction(Object data) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}
		return MappingUtils.convert(MapUtil.resolve("action.details", data), String.class);
	}

	protected String getNotificationPrincipal(Object data) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}		
		return MappingUtils.convert(MapUtil.resolve("principal.id", data), String.class);
	}

	@SuppressWarnings("unchecked")
	protected List<String> getNotificationTargets(Object data) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}
		List<?> targetList = MappingUtils.convert(MapUtil.resolve("source.targets", data), List.class);
		if (targetList!=null ) {
			List<String> names = new ArrayList<>();
			for (Object target: targetList) {					
				if (target!=null) {
					Map<String, Object> map3 = target instanceof Map ? (Map<String,Object>) target :  MappingUtils.convert(target, Map.class);
					Object id = map3.get("id");
					if (id!=null && StringUtils.hasText(id.toString())) {
						names.add(id.toString());
					}
				}

			}
			return names;
		}
		return null;		
	}

}
