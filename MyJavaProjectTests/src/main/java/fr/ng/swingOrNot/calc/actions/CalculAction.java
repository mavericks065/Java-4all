package fr.ng.swingOrNot.calc.actions;

import fr.ng.swingOrNot.calc.CalcWindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


public class CalculAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private final CalcWindow calcWindow;

	public CalculAction(final CalcWindow calcWindow, final String text) {
		super(text);

		this.calcWindow = calcWindow;
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {

		final String number1String = this.calcWindow.getField1().getText();
		final double number1 = Double.parseDouble(number1String);

		final String number2String = this.calcWindow.getField2().getText();
		final double number2 = Double.parseDouble(number2String);

		final String operator = this.calcWindow.getModelOperateurs()
				.getSelectedOperator();

		double resultat = 0;

		if ("+".equals(operator)) {
			resultat = number1 + number2;
		} else if ("-".equals(operator)) {
			resultat = number1 - number2;
		} else if ("*".equals(operator)) {
			resultat = number1 * number2;
		} else if ("/".equals(operator)) {
			if (number2 == 0) {
				JOptionPane.showMessageDialog(null, "Vous ne pouvez pas diviser par 0");
			}
			resultat = number1 / number2;
		}

		this.calcWindow.getLabel().setText("Résultat = " + resultat);
	}
}