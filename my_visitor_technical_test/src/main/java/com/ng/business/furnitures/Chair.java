package com.ng.business.furnitures;

import com.ng.businessvisitor.ItemVisitor;

public class Chair extends Furniture {

	public Chair() {
	}

	@Override
	public void accept(ItemVisitor visitor) {
		visitor.printItems(this);
		visitor.countItems(this);
	}

	@Override
	public String toString() {
		return "chair";
	}
}
