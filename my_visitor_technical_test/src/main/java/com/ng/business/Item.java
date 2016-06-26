package com.ng.business;

import com.ng.businessvisitor.ItemVisitor;

public interface Item {

	void accept(ItemVisitor visitor);

}
