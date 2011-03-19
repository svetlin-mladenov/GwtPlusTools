package com.github.svetlin_mladenov.gwteventcreator.wizards;

import org.eclipse.core.runtime.IPath;

final class EventStructsNames {
	
	private final String eventClassName;
	private final String hasInterfaceName;
	private final String handlerInterfaceName;
	private final boolean generateHasInterfave;
	private final boolean generateSepаrateGetTypeMethod;
	private final boolean lazyTypeCreation;
	private final String packageName;
	private final String simpleEventName;
	
	private EventStructsNames(String packageName, String simpleEventName, String eventClassName, String hasInterfaceName, String handlerInterfaceName, boolean generateHasInterfave, boolean generateSepаrateGetMethod, boolean lazyTypeCreation) {
		this.packageName = packageName;
		this.simpleEventName = simpleEventName;
		this.eventClassName = eventClassName;
		this.hasInterfaceName = hasInterfaceName;
		this.handlerInterfaceName = handlerInterfaceName;
		this.generateHasInterfave = generateHasInterfave;
		this.generateSepаrateGetTypeMethod = generateSepаrateGetMethod;
		this.lazyTypeCreation = lazyTypeCreation;
	}
	
	public static final EventStructsNames create(String packageName, String eventName, boolean generateHasInterfave, boolean generateSepаrateGetMethod, boolean lazyTypeCreation) {
		return new EventStructsNames(packageName, eventName, eventName + "Event", "Has" + eventName + "Handlers", eventName + "Handler", generateHasInterfave, generateSepаrateGetMethod, lazyTypeCreation);
	}

	public static String extractPackageName(IPath path) {
		StringBuffer sb = new StringBuffer();
		
		String []segments = path.segments();
		if (segments.length == 0) {
			return "";
		}
		
		sb.append(segments[segments.length-1]);
		for (int i = segments.length-2; i>=0; i--) {
			String segment = segments[i];
			
			sb.insert(0, '.');
			sb.insert(0, segment);
			
			if(isPackageDelimiter(segment)) {
				break;
			}
		}
		
		return sb.toString();
	}

	private static boolean isPackageDelimiter(String candidate) {
		return "com".equals(candidate) ||"org".equals(candidate) || "net".equals(candidate) || "src".equals(candidate);
	}

	public String getEventClassName() {
		return eventClassName;
	}

	public String getHasInterfaceName() {
		return hasInterfaceName;
	}

	public String getHandlerInterfaceName() {
		return handlerInterfaceName;
	}

	public boolean generateHasInterfave() {
		return generateHasInterfave;
	}

	public boolean generateSepаrateGetTypeMethod() {
		return generateSepаrateGetTypeMethod;
	}

	public boolean isLazyTypeCreation() {
		return lazyTypeCreation;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public String getSimpleEventName() {
		return simpleEventName;
	}

}
