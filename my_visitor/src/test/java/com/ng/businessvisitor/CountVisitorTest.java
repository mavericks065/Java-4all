package com.ng.businessvisitor;

import org.junit.Assert;
import org.junit.Test;

import com.ng.business.Item;
import com.ng.business.furnitures.Chair;
import com.ng.business.furnitures.Table;
import com.ng.business.structures.Door;
import com.ng.business.structures.Room;
import com.ng.business.structures.Window;

public class CountVisitorTest {
	
	@Test
	public void test_should_count_chair_visited () {
		// GIVEN
		final Chair chair = new Chair();
		final CountVisitor countVisitor = new CountVisitor();

		// WHEN
		chair.accept(countVisitor);
		
		// THEN
		Assert.assertEquals(1, countVisitor.getCounter());
	}

	@Test
	public void  test_should_count_table_visited () {
		// GIVEN
		final Table table = new Table();
		final CountVisitor countVisitor = new CountVisitor();

		// WHEN
		table.accept(countVisitor);
		
		// THEN
		Assert.assertEquals(1, countVisitor.getCounter());
	}
	
	@Test
	public void test_should_count_door_and_window_visited () {
		// GIVEN
		final Door door = new Door();
		final Window window = new Window();
		final CountVisitor countVisitor = new CountVisitor();
		
		// WHEN
		door.accept(countVisitor);
		window.accept(countVisitor);
		
		// THEN
		Assert.assertEquals(2, countVisitor.getCounter());
	}
	
	@Test
	public void test_should_count_door_and_window_visited2 () {
		// GIVEN
		final Item[] roomItems = {new Chair()};
		final Room room = new Room(roomItems);
		final CountVisitor countVisitor = new CountVisitor();
		
		// WHEN
		room.accept(countVisitor);
		
		// THEN
		Assert.assertEquals(2, countVisitor.getCounter());
	}
	
	@Test
	public void test_should_print_reset_counter () {
		// GIVEN
		final Table table = new Table();
		final CountVisitor countVisitor = new CountVisitor();

		// WHEN
		table.accept(countVisitor);
		countVisitor.resetCounter();
		
		// THEN
		Assert.assertEquals(0, countVisitor.getCounter());
	}

}
