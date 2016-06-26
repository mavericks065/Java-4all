package com.ng.business.furnitures;

import com.ng.business.Item;
import com.ng.businessvisitor.ItemVisitor;

public abstract class Furniture implements Item {

	public void accept(ItemVisitor visitor) {
		this.accept(visitor);
	}
}
