package com.ng.business.furnitures;

import com.ng.business.Item;
import com.ng.businessvisitor.ItemVisitor;

public abstract class Furniture implements Item {


	public Furniture() {
	}

	public void accept(ItemVisitor visitor) {
		this.accept(visitor);
	}
}
