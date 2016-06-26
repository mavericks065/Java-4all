package com.ng.business.structures;

import com.ng.business.Item;
import com.ng.businessvisitor.ItemVisitor;

public class Room extends Structure {

	private Item[] items; 
	
	public Room(Item[] items) {
		this.items = items;
	}

	@Override
	public void accept(ItemVisitor visitor) {
		visitor.printItems(this);
		visitor.countItems(this);
		
		if (items != null && items.length != 0) {
			for (Item item : items) {
				visitor.printItems(item);
				visitor.countItems(item);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("room and its items : ");
		for (Item item : getItems()) {
			sb.append(item.toString());
			sb.append(" ");
		}
		return sb.toString();
	}

	public Item[] getItems() {
		return items;
	}
}
