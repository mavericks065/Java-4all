package com.ng.genericobjectpool;

import org.junit.Assert;
import org.junit.Test;

import com.ng.genericobjectpool.exceptions.OutOfPoolSizeException;

public class GenericObjectPoolTest {

	@Test
	public void test_should_initialize_pool() {
		// GIVEN
		final int initial = 500;
		final int ceiling = 1000;
		final int threshold = 100;
		final int growth = 200;

		// WHEN
		GenericObjectPool<Object> pool1 = new GenericObjectPool<Object>(
				initial, ceiling, threshold, growth) {

			@Override
			protected Object createObject() {
				return new Object();
			}

		};

		// THEN
		Assert.assertEquals(initial, pool1.getPool().size());
		Assert.assertEquals(ceiling, pool1.getCeiling());
		Assert.assertEquals(threshold, pool1.getThreshold());
		Assert.assertEquals(growth, pool1.getGrowth());
	}

	@Test
	public void test_should_acquired_object_and_change_his_state() {
		// GIVEN
		final int initial = 500;
		final int ceiling = 1000;
		final int threshold = 100;
		final int growth = 200;

		// WHEN
		GenericObjectPool<Object> genericPool = new GenericObjectPool<Object>(
				initial, ceiling, threshold, growth) {

			@Override
			protected Object createObject() {
				return new Object();
			}

		};
		Object acquiredObject = genericPool.acquiredObject();

		// THEN
		// nbrOfFreeObjects should be equal to initial - 1 (the acquired object)
		Assert.assertEquals(499, genericPool.getNbrOfFreeObjects());
		for (GenericObjectPool<Object>.Poolable poolable : genericPool.getPool()) {
			if (acquiredObject.equals(poolable.getObject())) {
				Assert.assertEquals(GenericObjectPool.State.INUSE,
						poolable.getState());
			}
		}
	}

	@Test
	public void test_should_acquired_object_and_make_pool_bigger_under_ceiling_size() {
		// GIVEN
		final int initial = 500;
		final int ceiling = 1000;
		final int threshold = 500;
		final int growth = 200;

		// WHEN
		GenericObjectPool<Object> pool = new GenericObjectPool<Object>(initial,
				ceiling, threshold, growth) {

			@Override
			protected Object createObject() {
				return new Object();
			}

		};
		pool.acquiredObject();

		// THEN
		// nbrOfFreeObjects should be equal to initial - 1 (the acquired object)
		// + growth under the limit of the ceiling.
		Assert.assertEquals(699, pool.getNbrOfFreeObjects());
		Assert.assertEquals(initial + growth, pool.getPool().size());
	}

	@Test
	public void test_should_acquired_object_and_make_pool_bigger_under_ceiling_size0() {
		// GIVEN
		final int initial = 5;
		final int ceiling = 10;
		final int threshold = 5;
		final int growth = 2;

		// WHEN
		GenericObjectPool<Object> genericPool = new GenericObjectPool<Object>(
				initial, ceiling, threshold, growth) {

			@Override
			protected Object createObject() {
				return new Object();
			}

		};

		Object acquiredObject = genericPool.acquiredObject();

		// THEN
		for (GenericObjectPool<Object>.Poolable poolable : genericPool.getPool()) {
			if (acquiredObject.equals(poolable.getObject())) {
				Assert.assertEquals(GenericObjectPool.State.INUSE,
						poolable.getState());
			}
		}
	}

	@Test
	public void test_should_acquired_object_and_make_pool_bigger_at_ceiling_size() {
		// GIVEN
		final int initial = 500;
		final int ceiling = 700;
		final int threshold = 500;
		final int growth = 250;

		// WHEN
		GenericObjectPool<Object> genericPool = new GenericObjectPool<Object>(
				initial, ceiling, threshold, growth) {

			@Override
			protected Object createObject() {
				return new Object();
			}

		};
		Object object = genericPool.acquiredObject();

		// THEN

		for (GenericObjectPool<Object>.Poolable poolable : genericPool.getPool()) {
			if (object.equals(poolable.getObject())) {
				Assert.assertEquals(GenericObjectPool.State.INUSE,
						poolable.getState());
			}
		}
		Assert.assertEquals(ceiling, genericPool.getPool().size());
	}

	@Test
	public void test_should_free_object_and_re_change_his_state()
			throws OutOfPoolSizeException {
		// GIVEN
		final int initial = 500;
		final int ceiling = 1000;
		final int threshold = 100;
		final int growth = 200;

		// WHEN
		GenericObjectPool<Object> genericPool = new GenericObjectPool<Object>(
				initial, ceiling, threshold, growth) {

			@Override
			protected Object createObject() {
				return new Object();
			}

		};
		Object object = genericPool.acquiredObject();

		// THEN
		for (GenericObjectPool<Object>.Poolable poolable : genericPool.getPool()) {
			if (object.equals(poolable.getObject())) {
				genericPool.freeObject(poolable);
				Assert.assertEquals(GenericObjectPool.State.FREE,
						poolable.getState());
			}
		}
	}

}
