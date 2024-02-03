package View;

import javax.swing.JFrame;

public class App extends JFrame {
	public static final int WIDTH = 600, HEIGHT = 700;
//	public static MainPanel mainPanel = new MainPanel();

	public App() {
		add(new Map());
		pack();
		setTitle("Jet Fighter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new App();
	}
}