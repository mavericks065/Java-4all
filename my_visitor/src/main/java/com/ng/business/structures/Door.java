package com.ng.business.structures;

import com.ng.businessvisitor.ItemVisitor;

public class Door extends Structure {

	public Door() {
	}

	@Override
	public void accept(ItemVisitor visitor) {
		visitor.printItems(this);
		visitor.countItems(this);
	}

	@Override
	public String toString() {
		return "door";
	}
}
