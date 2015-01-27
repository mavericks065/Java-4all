package fr.ng.swingOrNot.calc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

public class OperatorsModel extends DefaultComboBoxModel {

	private static final long serialVersionUID = 1L;
	private final List<String> operators;

	public OperatorsModel() {
		super();

		this.operators = new ArrayList<String>();
		this.operators.add("+");
		this.operators.add("-");
		this.operators.add("*");
		this.operators.add("/");
	}

	public String getSelectedOperator() {
		return (String) this.getSelectedItem();
	}

	@Override
	public Object getElementAt(final int index) {
		return this.operators.get(index);
	}

	@Override
	public int getSize() {
		return this.operators.size();
	}

	@Override
	public int getIndexOf(final Object element) {
		return this.operators.indexOf(element);
	}

}