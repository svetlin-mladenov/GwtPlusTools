package com.github.svetlin_mladenov.gwteventcreator.wizards;

import org.eclipse.core.runtime.IPath;

final class EventStructsNames {
	
	private static final String EVENT_SUFFIX = "Event";
	
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
		eventName = striptEventSuffix(eventName);
		return new EventStructsNames(packageName, eventName, eventName + EVENT_SUFFIX, "Has" + eventName + "Handlers", eventName + "Handler", generateHasInterfave, generateSepаrateGetMethod, lazyTypeCreation);
	}
	
	private static String striptEventSuffix(String eventName) {
		if (eventName.toLowerCase().endsWith(EVENT_SUFFIX.toLowerCase())) {
			eventName = eventName.substring(0, eventName.length() - EVENT_SUFFIX.length());
		}
		return eventName;
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
