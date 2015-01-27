package fr.ng.swingOrNot.calc;

import javax.swing.SwingUtilities;

/**
 * Little classic project for refreshing my thoughts on the basics of SWING FRAMEWORK
 */
public class Calc {

	public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CalcWindow fenetre = new CalcWindow();
				fenetre.setVisible(true);
			}
		});

	}
}
