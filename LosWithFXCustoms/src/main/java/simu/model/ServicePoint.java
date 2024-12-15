package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;

/**
 * Represents a service point in the simulation where customers are served,
 * handling queuing, service timing, and event scheduling.
 */
public class ServicePoint {
	protected LinkedList<Customer> queue = new LinkedList<>(); // Data Structure used
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
	private String name;
    protected boolean reserved = false;


	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, String name) {
		this.eventList = eventList;
		this.generator = generator;
		this.eventTypeScheduled = type;
		this.name = name;
	}

	public Customer peekQueue(){
		return queue.peek();
    }

	public void addQueue(Customer a) {	// The first customer of the queue is always in service
		queue.add(a);
	}

	public Customer removeQueue() {		// Remove serviced customer
		reserved = false;
		return queue.poll();
	}

	public void beginService() {		// Begins a new service, customer is on the queue during the service
		Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queue.peek().getId());
		
		reserved = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
	}

	public void endService() {		// Begins a new service, customer is on the queue during the service
		Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queue.peek().getId());

		reserved = true;
		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()));
	}
	/**
	 * @hidden
	 */
	public boolean isReserved(){
		return reserved;
	}
	/**
	 * @hidden
	 */
	public boolean isOnQueue(){
		return queue.size() != 0;
	}
	/**
	 * @hidden
	 */
	public EventType getEventType(){
		return eventTypeScheduled;
	}
	/**
	 * @hidden
	 */
	public LinkedList<Customer> getQueue(){
		return queue;
	}
	/**
	 * @hidden
	 */
	public String getName() {
		return name;
	}
}
