package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import controller.TaskFactory;
import model.ITask;
import model.task1.Task1;
import util.Constants;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;

/************************************************************************************************
 * Developer: Anton 																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This is simple UI to allow user to interact with the functionalities provided	*
 ************************************************************************************************/
public class GUI extends JFrame {

	private JPanel contentPane;
	private JButton[] buttons;
	private JLabel lblTimeTaken;
	private Map<String, Object> result;
	private String title;
	private JTextPane titleTextPane;
	private JButton btnViewChart;
	private JTable resultTable;
	private Vector<Vector> rowData;
	private Vector<String> columnNames;
	private JScrollPane pmJSP;
	private JButton btnSetPath;
	private static GUI frame;

	// Launch the application.
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				try {
					
					frame = new GUI();
					frame.setVisible(true);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
	}

	// Create the frame.
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
		
		/* Start of initializing, assigning, and setting listener to buttons */
		
		JButton btnTask_1 = new JButton("Task 1");
		btnTask_1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				title = "(TASK 1)\nHow many negative reasons? (e.g., \"late flight\" or \"rude service\")";
				
				doTask(1);
			}
		});
		
		btnTask_1.setBounds(15, 435, 115, 29);
		contentPane.add(btnTask_1);
		buttons[0] = btnTask_1;

		JButton btnTask_2 = new JButton("Task 2");
		btnTask_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 2)\nDiscover the people from which country complain the most for the " + 
				"airline services?";
				doTask(2);
			}
		});
		btnTask_2.setBounds(145, 435, 115, 29);
		contentPane.add(btnTask_2);
		buttons[1] = btnTask_2;
		
		JButton btnTask_3 = new JButton("Task 3");
		btnTask_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 3)\nHow many people give the negative comments on the airline by giving the " + 
				"\"badflight\" or \"CSProblem\" negative reason for each company?";
				doTask(3);
			}
		});
		btnTask_3.setBounds(275, 435, 115, 29);
		contentPane.add(btnTask_3);
		buttons[2] = btnTask_3;

		JButton btnTask_4 = new JButton("Task 4");
		btnTask_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 4)\nWhat are the top 3 airlines getting the most number or positive tweets?";
				doTask(4);
			}
		});
		btnTask_4.setBounds(405, 435, 115, 29);
		contentPane.add(btnTask_4);
		buttons[3] = btnTask_4;
		
		JButton btnTask_5 = new JButton("Task 5");
		btnTask_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 5)\nWhat is the median value of the trusting point in each airline?";
				doTask(5);
			}
		});
		btnTask_5.setBounds(15, 483, 115, 29);
		contentPane.add(btnTask_5);
		buttons[4] = btnTask_5;

		JButton btnTask_6 = new JButton("Task 6");
		btnTask_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 6)\nHow many delayed flights? Tweets can be filtered to only those with " + 
				"certain keywords (i.e., \"delayed\") or hashtags (i.e., \"#SFO\") to  find a " + 
						"set of relevant messages.";
				doTask(6);
			}
		});
		btnTask_6.setBounds(145, 483, 115, 29);
		contentPane.add(btnTask_6);
		buttons[5] = btnTask_6;
		
		JButton btnTask_7 = new JButton("Task 7");
		btnTask_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 7)\nHow many unique IPs in the twitter dataset? " + 
				"Please print the IPs and the number of tweets from them.";	
				doTask(7);			
			}
		});
		btnTask_7.setBounds(275, 483, 115, 29);
		contentPane.add(btnTask_7);
		buttons[6] = btnTask_7;
		
		JButton btnTask_8 = new JButton("Task 8");
		btnTask_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 8)\nHow many angry/happy/etc. tweets for flights? We can perform sentiment " +
						"analysis on the set of tweets to separate tweets by emotion: angry, " + 
						"happy, etc. To perform sentiment analysis, we can use a free online " + 
						"sentiment database, SentiWordNet(http://sentiwordnet.isti.cnr.it/)," + 
						"to find the emotions most likely associated with each tweet";
				doTask(8);
			}
		});
		btnTask_8.setBounds(405, 483, 115, 29);
		contentPane.add(btnTask_8);
		buttons[7] = btnTask_8;

		JButton btnTask_9 = new JButton("Task 9");
		btnTask_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = "(TASK 9)\nListing the top 5 person name with negative sentiment and sort the number of tweet";
				doTask(9);
			}
		});
		btnTask_9.setBounds(15, 528, 115, 29);
		contentPane.add(btnTask_9);
		buttons[8] = btnTask_9;
		
		/* End of initializing, assigning, and setting listener to buttons */
		
		/************************************************************************************************
		 * Description: This button is to show the chart of tasks  										*
		 * 																								*
		 ************************************************************************************************/
		btnViewChart = new JButton("View chart");
		btnViewChart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new PieChart(result, title).View();
			}
		});
		btnViewChart.setEnabled(false);
		btnViewChart.setBounds(167, 528, 115, 29);
		contentPane.add(btnViewChart);
		
		/************************************************************************************************
		 * Description: This button is to set the path to read the hadoop files before using before		*
		 * 				using the program																*
		 ************************************************************************************************/
		btnSetPath = new JButton("Set Path");
		btnSetPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetPathGUI subFrame = null;
				subFrame = new SetPathGUI();
				subFrame.setThisFrame(subFrame);
				subFrame.setVisible(true);
			}
		});
		btnSetPath.setBounds(295, 528, 115, 29);
		contentPane.add(btnSetPath);
		
		/************************************************************************************************
		 * Description: This resultTable is to display the result of selected task  					*
		 * 																								*
		 ************************************************************************************************/
		resultTable = new JTable();
	    resultTable.setFont(new Font("Tahoma", Font.PLAIN, 15));
	    FontMetrics metrics = resultTable.getFontMetrics(new Font("Tahoma", Font.PLAIN, 20));
	    int fontHeight = metrics.getHeight();
	    resultTable.setRowHeight(fontHeight);
		pmJSP = new JScrollPane(resultTable);
		pmJSP.setViewportView(resultTable);
		pmJSP.setBounds(15, 116, 506, 300);
		contentPane.add(pmJSP);
		
		/************************************************************************************************
		 * Description: This lblTimeTaken is to display the time taken that a task takes to complete  	*
		 * 																								*
		 ************************************************************************************************/
		lblTimeTaken = new JLabel("0 ms");
		lblTimeTaken.setBounds(451,537,69,20);
		contentPane.add(lblTimeTaken);
		
		/************************************************************************************************
		 * Description: This titleTextPane is to display the question/description of each task 			*
		 * 																								*
		 ************************************************************************************************/
		titleTextPane = new JTextPane();
		titleTextPane.setEditable(false);
		titleTextPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		JScrollPane pmJSP1 = new JScrollPane(titleTextPane);
		pmJSP1.setViewportView(titleTextPane);
		pmJSP1.setBounds(15, 16, 506, 84);
		contentPane.add(pmJSP1);
	}
	
	/**
	 * To set all buttons in the UI to be disabled or enabled
	 * @param value true or false
	 */
	private void setEnableAllButtons(Boolean value)
	{
		for(int i = 0; i < buttons.length ; i++)
		{
			buttons[i].setEnabled(value);
		}
	}
	
	/**
	 * This method is to create a thread to run the selected task in background.
	 * All buttons will be disabled when running the task.
	 * After task is completely done, table in the UI will be updated with the result,
	 * and all buttons will be enabled
	 * @param taskId the number of the task to be run
	 */
	private void doTask(int taskId)
	{
		setEnableAllButtons(false);
		btnViewChart.setEnabled(false);
		titleTextPane.setText(title);
		lblTimeTaken.setText("Loading");
		
		Thread thread = new Thread(new Runnable() {
			ITask task;
			@Override
			public void run() {
				try {					
			        double total=0;		
					rowData = new Vector<Vector>();
					result = new HashMap<String, Object>();
					Map<String,Object> tempresult = new HashMap<String, Object>();
					task = TaskFactory.getTask(taskId);
					task.start();

					while(!task.isDone());
					
					Configuration configuration = new Configuration();
					FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), configuration);
					Path filePath = new Path(Constants.hadoopPath + "/output/task" + taskId + "/part-r-00000");
					FSDataInputStream fsDataInputStream = fs.open(filePath);
					BufferedReader br = new BufferedReader(new InputStreamReader(fsDataInputStream));
				    String line = br.readLine();

				    // Read every line from the output
				    while (line != null) {
				    	String string = line;
				    	
				    	// Processing the string
				        if(line.split("\t").length > 2){
					        string = string.replace("\t", "_");
					        string = string.replaceFirst("_", "~");
					        string = string.replace("_", "\t");
				        }
				        
				        // Store the results into the row Vector, add this row to the table rowData
				        String rowLines[] = string.split("\t");
				        Vector<String> row = new Vector<String>();
				        row.addElement(rowLines[0]);
				        row.addElement(rowLines[1]);
				        rowData.addElement(row);
				        
				        total += Double.parseDouble(rowLines[1]);
				        // for task3, get the total number of CSProblem and #badflight to show in the chart
				        if(taskId == 3)
				        {				
				        	String key = rowLines[0].split("~")[1];
				        	if(tempresult.containsKey(key))
				        	{
				        		int value = Integer.parseInt(tempresult.get(key).toString());
				        		value += Integer.parseInt(rowLines[1]);
				        		tempresult.replace(key, value);
				        	}
				        	else
				        	{
					        	tempresult.put(key, rowLines[1]);
				        	}
				        }
				        else
				        {
				        	tempresult.put(rowLines[0], rowLines[1]);
				        }

					    line = br.readLine();
				    }
		        
			        // Processing data for displaying into chart for some tasks
				    if(taskId == 1 || taskId == 3 || taskId == 4 || taskId == 8 || taskId == 9 || taskId == 5)
				    {
				        for(String key : tempresult.keySet())
				        {
				        	double value = Double.parseDouble(tempresult.get(key).toString());
				        	double newValue = (value/total) * 100;
				        	result.put(key+ String.format("(%.2f)", newValue) + "%", tempresult.get(key).toString());
				        }
				    }
			        line = br.readLine();
				    
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
					setEnableAllButtons(true);
				}
				catch (URISyntaxException ex)
				{
					ex.printStackTrace();
					setEnableAllButtons(true);
				} 
				catch(Exception ex)
				{
					ex.printStackTrace();
					setEnableAllButtons(true);
				}
				
				updateTable();
				
				lblTimeTaken.setText( task.timeElapsed() + " ms");
				setEnableAllButtons(true);
				if(taskId == 1 || taskId == 3 || taskId == 4 || taskId == 8 || taskId == 9 || taskId == 5)
				{
					btnViewChart.setEnabled(true);
				}
			}
		});
		thread.start();
	}
	
	/**
	 * Update the JTable in the UI
	 */
	private void updateTable()
	{
		contentPane.remove(pmJSP);
		columnNames = new Vector<String>();
        columnNames.addElement("");
        columnNames.addElement("Values");
        resultTable = new JTable(rowData, columnNames);
	    resultTable.setFont(new Font("Tahoma", Font.PLAIN, 15));
	    FontMetrics metrics = resultTable.getFontMetrics(new Font("Tahoma", Font.PLAIN, 20));
	    int fontHeight = metrics.getHeight();
	    resultTable.setRowHeight(fontHeight);
	    resultTable.getColumn("Values").setMaxWidth(150);
		pmJSP = new JScrollPane(resultTable);
		pmJSP.setBounds(15, 116, 506, 300);
		contentPane.add(pmJSP);
	}
}
