package com.ng.businessvisitor;

import com.ng.business.Item;

/**
 * Interface visitor that will be on 
 * top of visitors' hierarchy
 * 
 * @author nicolas guignard
 *
 */
public interface ItemVisitor {

	void countItems(Item item);

	void printItems(Item item);
}
