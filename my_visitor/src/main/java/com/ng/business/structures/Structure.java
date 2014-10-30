package com.ng.business.structures;

import com.ng.business.Item;
import com.ng.businessvisitor.ItemVisitor;

public abstract class Structure implements Item {

	public Structure() {
	}

	public void accept(ItemVisitor visitor) {
		this.accept(visitor);
	}
}
