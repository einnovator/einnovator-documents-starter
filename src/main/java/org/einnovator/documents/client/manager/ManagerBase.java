package org.einnovator.documents.client.manager;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.SecurityUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.util.StringUtils;

public class ManagerBase {

	public static final String NOTIFICATION_TYPE = "Notification";

	private final Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	protected <T> T getCacheValueForPrincipal(Class<T> type, Cache cache, Object... keys) {
		if (cache==null) {
			return null;
		}
		Principal principal = SecurityUtil.getPrincipal();
		if (principal==null) {
			return null;
		}
		String key = makeKeyForPrincipal(keys);
		ValueWrapper value = cache.get(key);
		if (value!=null) {
			T val = (T)value.get();
			logger.debug("getCacheValueForPrincipal: " + key + " " + val);
			return val;
		}
		return null;
	}

	protected <T> T putCacheValueForPrincipal(T value, Cache cache, Object... keys) {
		if (cache==null) {
			return value;
		}
		Principal principal = SecurityUtil.getPrincipal();
		if (principal==null) {
			return value;
		}
		String key = makeKeyForPrincipal(keys);
		cache.put(key, value);
		logger.debug("putCacheValueForPrincipal: " + key + " " + value);
		return value;
	}


	@SuppressWarnings("unchecked")
	protected <T> T getCacheValue(Class<T> type, Cache cache, Object... keys) {
		if (cache==null) {
			return null;
		}
		String key = makeKey(keys);
		if (key!=null) {
			ValueWrapper value = cache.get(key);
			if (value!=null) {
				T val = (T)value.get();
				logger.debug("getCacheValue: " + key + " " + val);
				return val;			}
		}
		return null;
	}

	protected <T> T putCacheValue(T value, Cache cache, Object... keys) {
		if (cache==null) {
			return value;
		}
		String key = makeKey(keys);
		if (key!=null) {
			cache.put(key, value);
			logger.debug("putCacheValueForPrincipal: " + key + " " + value);
		}
		return value;
	}

	protected String makeKeyForPrincipal(Object... keys) {
		Principal principal = SecurityUtil.getPrincipal();
		if (principal==null) {
			return null;
		}
		String key = makeKey(keys);
		return StringUtils.hasText(key) ? principal.getName() + ":" + key : principal.getName();
	}
		

	protected String makeKey(Object... keys) {
		StringBuilder sb = new StringBuilder();
		for (Object key: keys) {
			if (key!=null) {
				if (sb.length()>0) {
					sb.append(":");						
				}
				if (key instanceof String) {
					sb.append(key);
				} else {
					sb.append(key.hashCode());
				}				
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	protected <T> T getNotificationSource(Object data, Class<T> type) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}
		Map<String, Object> map = MappingUtils.convert(data, Map.class);
		Object source = map.get("source");
		if (source!=null) {
			Map<String, Object> map2 = source instanceof Map
					? (Map<String,Object>) source :  MappingUtils.convert(source, Map.class);
			Object type_ = map2.get("type");
			if (type_!=null && type.getSimpleName().equalsIgnoreCase(type_.toString())) {
				Object details = map2.get("details");
				if (details!=null) {
					return  MappingUtils.convert(details, type);					
				}
			}
		}
		return null;		
	}

	@SuppressWarnings("unchecked")
	protected String getNotificationAction(Object data) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}		
		Map<String, Object> map = MappingUtils.convert(data, Map.class);
		Object action = map.get("action");
		if (action!=null) {
			Map<String, Object> map2 = action instanceof Map ? (Map<String,Object>) action :  MappingUtils.convert(action, Map.class);
			Object type = map2.get("type");
			return type!=null ? type.toString() : null;
		}
		return null;		
	}

	@SuppressWarnings("unchecked")
	protected String getNotificationPrincipal(Object data) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}		
		Map<String, Object> map = MappingUtils.convert(data, Map.class);
		Object principal = map.get("principal");
		if (principal!=null) {
			Map<String, Object> map2 = principal instanceof Map ? (Map<String,Object>) principal :  MappingUtils.convert(principal, Map.class);
			Object name = map2.get("id");
			return name!=null ? name.toString() : null;
		}
		return null;		
	}

	@SuppressWarnings("unchecked")
	protected List<String> getNotificationTargets(Object data) {
		if (data==null || !data.getClass().getSimpleName().equals(NOTIFICATION_TYPE)) {
			return null;
		}
		Map<String, Object> map = MappingUtils.convert(data, Map.class);
		Object source = map.get("source");
		if (source!=null) {
			Map<String, Object> map2 = source instanceof Map ? (Map<String,Object>) source :  MappingUtils.convert(source, Map.class);
			Object targets = map2.get("targets");
			if (targets!=null ) {
				List<String> names = new ArrayList<>();
				List<?> targetList = MappingUtils.convert(targets, List.class);	
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
		}
		return null;		
	}

}
