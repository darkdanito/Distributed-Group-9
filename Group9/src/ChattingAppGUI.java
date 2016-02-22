import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

public class ChattingAppGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChattingAppGUI frame = new ChattingAppGUI();
					frame.setTitle("Group 9 - Chatting Application");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChattingAppGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 757, 543);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 735, 487);
		contentPane.add(tabbedPane);
		JLabel label1 = new JLabel("First panel");
		JLabel label2 = new JLabel("Second panel");
		JPanel panel1 = new JPanel();
		panel1.add(label1);
		JPanel panel2 = new JPanel();
		panel2.add(label2);
		
		tabbedPane.add("First tab", panel1);
		tabbedPane.add("Second tab", panel2);
	}
}
