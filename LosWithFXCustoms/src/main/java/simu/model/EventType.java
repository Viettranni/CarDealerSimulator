package simu.model;

import simu.framework.IEventType;

/**
 * Represents event types for simulation stages, including customer arrivals and service point departures.
 */
public enum EventType implements IEventType {
	ARR1, DEP1, DEP2, DEP3, DEP4;
}
