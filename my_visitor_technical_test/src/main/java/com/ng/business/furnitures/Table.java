package com.ng.business.furnitures;

import com.ng.businessvisitor.ItemVisitor;

public class Table extends Furniture {

	public Table() {
	}

	@Override
	public void accept(ItemVisitor visitor) {
		visitor.printItems(this);
		visitor.countItems(this);
	}

	@Override
	public String toString() {
		return "table";
	}
}
