package com.ng.business.structures;

import com.ng.businessvisitor.ItemVisitor;

public class Window extends Structure {

	public Window() {
	}

	@Override
	public void accept(ItemVisitor visitor) {
		visitor.printItems(this);
		visitor.countItems(this);
	}

	@Override
	public String toString() {
		return "window";
	}
}
