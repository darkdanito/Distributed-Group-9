package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathNotFoundException;

import util.Constants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.swing.JTextField;
import javax.swing.JButton;

/************************************************************************************************
 * Developer: Anton 																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This class is to allow user to customize the hadoop local machine file system 	*
 * 				path.																*
 ************************************************************************************************/
public class SetPathGUI extends JFrame {
	private JPanel contentPane;
	private JTextField txtPath;
	private SetPathGUI thisFrame;

	/**
	 * Create the frame.
	 */
	public SetPathGUI() {
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 204);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPath = new JLabel("Path:");
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPath.setBounds(28, 38, 69, 20);
		contentPane.add(lblPath);
		
		txtPath = new JTextField();
		txtPath.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPath.setBounds(112, 37, 281, 26);
		contentPane.add(txtPath);
		txtPath.setColumns(10);
		
		JLabel lblExamplePathUserphamvanvunggrouphadoop = new JLabel("example path: user/phamvanvung/group9_hadoop");
		lblExamplePathUserphamvanvunggrouphadoop.setBounds(28, 70, 365, 20);
		contentPane.add(lblExamplePathUserphamvanvunggrouphadoop);
		
		JButton btnSavePath = new JButton("Save Path");
		btnSavePath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try
				{
					String path = txtPath.getText();
					
					if(path.charAt(path.length()-1) == '/')
						path = path.substring(0, path.length()-1);
					if(path.charAt(0) == '/')
						path = path.substring(1, path.length()-1);
					
					Configuration configuration = new Configuration();
					FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), configuration);
					
					Path filePath = new Path("/" + path + "/input");
					if(!fs.exists(filePath))
						throw new PathNotFoundException(fs.getUri() + "/" + filePath.toString());
					
					filePath = new Path("/" + path + "/SentiWordNet.txt");
					if(!fs.exists(filePath))
						throw new FileNotFoundException(fs.getUri() + "/" + filePath.toString());
					
					filePath = new Path("/" + path + "/ISO-3166-alpha3.tsv");
					if(!fs.exists(filePath))
						throw new FileNotFoundException(fs.getUri() + "/" + filePath.toString());
					
					Constants.hadoopPath = path;
					JOptionPane.showMessageDialog(thisFrame, path + " is succesfully saved!", "File path", JOptionPane.INFORMATION_MESSAGE);
					thisFrame.dispose();
				}
				catch(PathNotFoundException ex)
				{
					String errorMsg = "Path " + ex.getPath().toString() + " cannot be found!";
					JOptionPane.showMessageDialog(thisFrame, errorMsg, "Path not found", JOptionPane.ERROR_MESSAGE);
				}
				catch(FileNotFoundException ex)
				{
					String errorMsg = "File " + ex.getMessage() + " cannot be found!\n" + 
							"Please read the ReadMe.txt for further instructions.";
					JOptionPane.showMessageDialog(thisFrame, errorMsg, "Path not found", JOptionPane.ERROR_MESSAGE);
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(thisFrame, "Please check your hadoop localhost connection\n" + ex.getMessage(), "Path not found", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSavePath.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSavePath.setBounds(137, 106, 147, 29);
		contentPane.add(btnSavePath);
	}

	public void setThisFrame(SetPathGUI thisFrame) {
		this.thisFrame = thisFrame;
	}
}

