package com.ng.businessvisitor;

import com.ng.business.Item;

/**
 * The aim of this class is to count every visited item
 * 
 * @author nicolas guignard
 *
 */
public class CountVisitor implements ItemVisitor {
	
	private int counter; 
	
	public CountVisitor (){
		this.counter = 0;
	}
	
	@Override
	public void countItems(Item item) {
		counter++;
	}

	@Override
	public void printItems(Item item) {
	}

	public int getCounter() {
		return counter;
	}

	public void resetCounter() {
		this.counter = 0;
	}

}