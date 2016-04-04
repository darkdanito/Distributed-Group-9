package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import controller.TaskFactory;
import model.ITask;
import model.task1.Task1;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JButton[] buttons;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
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
	public GUI() {
		setResizable(false);
		setTitle("Hadoop - ICT2107");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 609);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		buttons = new JButton[9];
		
		JButton btnTask_1 = new JButton("Task 1");
		btnTask_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doTask(1);
			}
		});
		btnTask_1.setBounds(15, 435, 115, 29);
		contentPane.add(btnTask_1);
		buttons[0] = btnTask_1;
		
		JButton btnTask_2 = new JButton("Task 2");
		btnTask_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(2);
			}
		});
		btnTask_2.setBounds(145, 435, 115, 29);
		contentPane.add(btnTask_2);
		buttons[1] = btnTask_2;
		
		JButton btnTask_3 = new JButton("Task 3");
		btnTask_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(3);
			}
		});
		btnTask_3.setBounds(275, 435, 115, 29);
		contentPane.add(btnTask_3);
		buttons[2] = btnTask_3;
		
		JButton btnTask_4 = new JButton("Task 4");
		btnTask_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(4);
			}
		});
		btnTask_4.setBounds(405, 435, 115, 29);
		contentPane.add(btnTask_4);
		buttons[3] = btnTask_4;
		
		JButton btnTask_5 = new JButton("Task 5");
		btnTask_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(5);
			}
		});
		btnTask_5.setBounds(15, 483, 115, 29);
		contentPane.add(btnTask_5);
		buttons[4] = btnTask_5;
		
		JButton btnTask_6 = new JButton("Task 6");
		btnTask_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(6);
			}
		});
		btnTask_6.setBounds(145, 483, 115, 29);
		contentPane.add(btnTask_6);
		buttons[5] = btnTask_6;
		
		JButton btnTask_7 = new JButton("Task 7");
		btnTask_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(7);
			}
		});
		btnTask_7.setBounds(275, 483, 115, 29);
		contentPane.add(btnTask_7);
		buttons[6] = btnTask_7;
		
		JButton btnTask_8 = new JButton("Task 8");
		btnTask_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(8);
			}
		});
		btnTask_8.setBounds(405, 483, 115, 29);
		contentPane.add(btnTask_8);
		buttons[7] = btnTask_8;
		
		JButton btnTask_9 = new JButton("Task 9");
		btnTask_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTask(9);
			}
		});
		btnTask_9.setBounds(213, 528, 115, 29);
		contentPane.add(btnTask_9);
		buttons[8] = btnTask_9;
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		JScrollPane pmJSP = new JScrollPane(textPane);
		pmJSP.setViewportView(textPane);
		pmJSP.setBounds(15, 16, 506, 400);
		contentPane.add(pmJSP);
	}
	
	private void setEnableAllButtons(Boolean value)
	{
		for(int i = 0; i < buttons.length ; i++)
		{
			buttons[i].setEnabled(value);
		}
	}
	
	private void doTask(int taskId)
	{
		setEnableAllButtons(false);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					ITask task = TaskFactory.getTask(taskId);
					task.start();
					
					while(!task.isDone());
					
					Configuration configuration = new Configuration();
					FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), configuration);
					Path filePath = new Path("/user/phamvanvung/group9_hadoop/output/task" + taskId + "/part-r-00000");
					FSDataInputStream fsDataInputStream = fs.open(filePath);
					BufferedReader br = new BufferedReader(new InputStreamReader(fsDataInputStream));
					StringBuilder sb = new StringBuilder();
				    String line = br.readLine();

				    while (line != null) {
				    	StyledDocument doc = textPane.getStyledDocument();
				        Style style = textPane.addStyle("I'm a Style", null);
				
				        try { doc.insertString(doc.getLength(), line +"\n", style); }
				        catch (BadLocationException ex){}
				        line = br.readLine();
				    }
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				catch (URISyntaxException ex)
				{
					ex.printStackTrace();
				} 
				setEnableAllButtons(true);
			}
		});
		thread.start();
	}
}
