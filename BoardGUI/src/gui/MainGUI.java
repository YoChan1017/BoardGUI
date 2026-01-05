package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGUI extends JFrame implements ActionListener{
	
	public MainGUI() {
		setTitle("MAIN");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		setSize(800, 600);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(panel);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public static void main(String[] args) {
		(new MainGUI()).setVisible(true);
	}
}
