package fr.ng.swingOrNot.calc.actions;

import fr.ng.swingOrNot.calc.CalcWindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


public class AboutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private final CalcWindow calcWindow;

	public AboutAction(final CalcWindow calcWindow, final String text) {
		super(text);

		this.calcWindow = calcWindow;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JOptionPane.showMessageDialog(this.calcWindow,
				"Ce programme a été développé par Nicolas");
	}
}
