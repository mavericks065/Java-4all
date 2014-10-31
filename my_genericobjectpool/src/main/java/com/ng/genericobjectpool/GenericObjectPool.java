package com.ng.genericobjectpool;

import java.util.ArrayList;
import java.util.List;

import com.ng.genericobjectpool.exceptions.OutOfPoolSizeException;

/**
 * 
 * Nicolas Guignard - Generic Object Pool Java programming test
 * 
 */
public abstract class GenericObjectPool<T> {

	public static final String GLOBAL_LOGGER = "GenericObjectPool Logger";

	/** Our pool of objects */
	private List<Poolable> pool;

	enum State {
		FREE, INUSE
	}
	
	
	class Poolable {

		private T object;
		private State state;
		
		Poolable(T object, State state) {
			this.object = object;
			this.state = state;
		}
		
		T getObject() {
			return object;
		}
		
		State getState() {
			return state;
		}
		
	}

	/**
	 * The total maximum number of objects that can be in the the pool at any
	 * time.
	 */
	private final int ceiling;

	/**
	 * The total minimum number of free objects left in the pool before
	 * allocating more space in the pool.
	 */
	private final int threshold;

	/**
	 * The number of objects to increase the pool by when the threshold is
	 * reached.
	 */
	private final int growth;

	/**
	 * The number of free objects present in the pool.
	 */
	private int nbrOfFreeObjects = 0;

	/**
	 * If the pool is full to the threshold there will be an increase of the
	 * space, increase will be equal to growth so the ceiling will increase.
	 * 
	 * @param initial
	 *            minimum number of objects residing in the pool
	 * @param threshold
	 *            filling percentage of objects left in the pool before
	 *            allocating more space
	 * @param growth
	 *            number of objects to increase the pool by when the threshold
	 *            is reached
	 * @param ceiling
	 *            maximum number of objects residing in the pool
	 * @param validationInterval
	 *            time in seconds for periodical checking of initial / ceiling
	 *            conditions in a separate thread. When the number of objects is
	 *            less than initial, missing instances will be created. When the
	 *            number of objects is greater than ceiling, too many instances
	 *            will be removed.
	 */
	public GenericObjectPool(final int initial, final int ceiling,
			final int threshold, final int growth) {
		this.ceiling = ceiling;
		this.threshold = threshold;
		this.growth = growth;

		initialize(initial);
	}
	
	private void initialize(final int initial) {
		pool = new ArrayList<>();

		for (int i = 0; i < initial; i++) {
			pool.add(new Poolable(createObject(), State.FREE));
			nbrOfFreeObjects++;
		}
	}

	/**
	 * Gets the next free object from the pool. If the pool doesn't contain any
	 * objects, a new object will be created and given to the caller of this
	 * method back.
	 * 
	 * @return T acquired object
	 */
	public T acquiredObject() {
		if (nbrOfFreeObjects == threshold) {
			growth();
		}
		return getFirstFreeObject();
	}

	private void growth() {
		if (growth + pool.size() <= ceiling) {
			for (int i = 0; i < growth; i++) {
				pool.add(new Poolable(createObject(), State.FREE));
				nbrOfFreeObjects++;
			}
		} else {
			final int size = pool.size();
			for (int i = 0; i < ceiling - size; i++) {
				pool.add(new Poolable(createObject(), State.FREE));
				nbrOfFreeObjects++;
			}
		}
	}

	/**
	 * Get the first FREE object of the LinkedHashMap, transform his state into
	 * INUSE and returns it.
	 * 
	 * @return T first Free object
	 */
	private T getFirstFreeObject() {
		for (Poolable poolable : pool) {
			if (State.FREE.equals(poolable.state)) {
				nbrOfFreeObjects--;
				// set the state from FREE to INUSE
				poolable.state = State.INUSE;
				return poolable.object;
			}
		}
		return null;
	}

	/**
	 * Returns object back to the pool.
	 * 
	 * @param object
	 *            object to be returned
	 * @throws OutOfPoolSizeException
	 */
	public void freeObject(final Poolable poolable) throws OutOfPoolSizeException {
		if (poolable == null) {
			return;
		}
		if (pool.size() >= ceiling) {
			throw new OutOfPoolSizeException();
		} else {
			poolable.state = State.FREE;
			nbrOfFreeObjects++;
		}
	}

	/**
	 * Creates a new object.
	 * 
	 * @return T new object
	 */
	protected abstract T createObject();
	
	public void deleteObject(Poolable poolableObject) throws Exception {
		if (poolableObject == null || !pool.contains(poolableObject)) {
			throw new Exception("poolable object is null or does not exist");
		}
		pool.remove(poolableObject);
	}

	public List<Poolable> getPool() {
		return pool;
	}

	public int getCeiling() {
		return ceiling;
	}

	public int getThreshold() {
		return threshold;
	}

	public int getGrowth() {
		return growth;
	}

	public int getNbrOfFreeObjects() {
		return nbrOfFreeObjects;
	}
	
}
