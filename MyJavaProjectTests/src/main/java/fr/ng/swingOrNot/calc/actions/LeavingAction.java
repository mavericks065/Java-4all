package fr.ng.swingOrNot.calc.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class LeavingAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public LeavingAction(final String text) {
		super(text);
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {
		System.exit(0);
	}
}
