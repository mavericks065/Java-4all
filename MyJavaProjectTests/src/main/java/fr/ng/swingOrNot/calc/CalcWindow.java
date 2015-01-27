package fr.ng.swingOrNot.calc;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import swingOrNot.calc.actions.AboutAction;
import swingOrNot.calc.actions.CalculAction;
import swingOrNot.calc.actions.LeavingAction;

public class CalcWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField field1;
	private JTextField field2;
	private JLabel label;
	private JComboBox list;

	public CalcWindow() {
		super();

		this.build();
	}

	private void build() {

		JMenuBar menuBar = new JMenuBar();

		JMenu menu1 = new JMenu("Calculatrice");

		JMenuItem calculer = new JMenuItem(new CalculAction(this, "Calculer"));
		menu1.add(calculer);

		JMenuItem quitter = new JMenuItem(new LeavingAction("Quitter"));
		menu1.add(quitter);

		menuBar.add(menu1);

		JMenu menu2 = new JMenu("?");

		JMenuItem aPropos = new JMenuItem(new AboutAction(this, "A propos"));
		menu2.add(aPropos);

		menuBar.add(menu2);

		this.setJMenuBar(menuBar);

		this.setTitle("Calculatrice");
		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(this.buildContentPane());
	}

	private JPanel buildContentPane() {

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.white);

		this.field1 = new JTextField();
		this.field1.setColumns(10);

		panel.add(this.field1);

		this.list = new JComboBox(new OperatorsModel());
		panel.add(this.list);

		this.field2 = new JTextField();
		this.field2.setColumns(10);

		panel.add(this.field2);

		JButton bouton = new JButton(new CalculAction(this, "Calculer"));

		panel.add(bouton);

		this.label = new JLabel("Résultat : Pas encore calculé");

		panel.add(this.label);

		return panel;
	}

	public OperatorsModel getModelOperateurs() {
		return (OperatorsModel) this.list.getModel();
	}

	public JTextField getField1() {
		return this.field1;
	}

	public JTextField getField2() {
		return this.field2;
	}

	public JLabel getLabel() {
		return this.label;
	}
}