package com.ng.businessvisitor;

import com.ng.business.Item;

/**
 * The aim of this visitor is to print "visited"
 * everytime an item is visited. 
 * 
 * @author nicolas guignard
 *
 */
public class PrintVisitor implements ItemVisitor{
	
	@Override
	public void countItems(Item item) {
	}

	@Override
	public void printItems(Item item) {
		System.out.println(item.toString() + " visited");
	}

}
