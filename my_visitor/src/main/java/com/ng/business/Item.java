package com.ng.business;

import com.ng.businessvisitor.ItemVisitor;

public interface Item {

	public void accept(ItemVisitor visitor);

}
