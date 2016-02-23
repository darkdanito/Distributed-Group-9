import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Model.*;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JTextPane;

public class ChattingAppGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// JPanels
	private JPanel contentPane;
	private JPanel accountPanel;
	private JPanel friendsPanel;
	private JPanel groupsPanel;
	private JPanel privateMsgPanel;
	private JPanel groupsSubPanel1;
	private JPanel groupsSubPanel2;
	
	// JTabbedPane variables for the UI
	private JTabbedPane tabbedPane;
	private JTabbedPane groupTabbedPane;
	
	// JTextFields
	private JTextField userNameJT;
	private JTextField friendNameJT;
	private JTextField groupNameJT;
	
	// JButtons
	private JButton registerBtn;
	private JButton statusBtn;
	private JButton addFriendBtn;
	private JButton deleteFriendBtn;
	private JButton pmFriendBtn;
	private JButton groupCreateBtn;
	private JButton grpJoinBtn;
	
	// JLists
	private JList<Person> friendListBox;
	private JList<Person> grpFriendListBox;
	private JList<Group> groupListBox;
	
	// Other JUnit
	private JTextPane chatMsgTextPane;
	private JLabel lblUsername;
	private JLabel grpListJL;
	private JLabel groupNameJL;
	private JLabel lblFriend;
	private JLabel grpFriendListJL;
	private JSeparator separator;
	private JSeparator separator_1;
	private JLabel groupListJL;
	private JLabel friendListJL;
	
	// Others variables
	private UserAccount userAccount = new UserAccount("UNREGISTERED UserAccount OBJECT");
	private JTextField joinedGroupNameJT;
	private JButton leaveGroupBtn;
	private JSeparator separator_2;
	private JLabel grpMsgJL;
	private JTextField grpMsgJT;
	private JButton grpSendMsgBtn;
	private JLabel pmChatWithJL;
	private JTextField chattingWithJT;
	private JTextPane pmTextPane;
	private JLabel pmMsgLabel;
	private JTextField pmMsgJT;
	private JButton pmSendBtn;
	private JSeparator separator_3;
	private JButton pmQuitBtn;
	
	/**
	 * Launch the application private JSeparator separator_1;
	private JLabel grpListJL;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChattingAppGUI frame = new ChattingAppGUI();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
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
		JFrame frame = this;
		
		setTitle("ICT2107 ChatApp");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 300, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
    	
    	// Start of account panel
		accountPanel = new JPanel();
		accountPanel.setLayout(null);

		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsername.setBounds(15, 16, 99, 33);
		accountPanel.add(lblUsername);
		
		userNameJT = new JTextField();
		userNameJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		userNameJT.setBounds(129, 16, 181, 33);
		accountPanel.add(userNameJT);
		userNameJT.setColumns(10);
		
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] exist = {"Winnie" ,"Khaleef"}; // TO BE DELETED
				String username = userNameJT.getText().toString();
				for(int i = 0; i< exist.length; i++)
				{
					if(exist[i].toLowerCase().equals(username.toLowerCase()))
					{
						JOptionPane.showMessageDialog(frame,
							    "Username \"" + username + "\" is already in use.",
							    "Registration failed",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				userAccount = new UserAccount(username);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(2, true);
				userNameJT.setEnabled(false);
				statusBtn.setVisible(true);
				registerBtn.setVisible(false);
			}
		});
		registerBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		registerBtn.setBounds(109, 61, 115, 40);
		accountPanel.add(registerBtn);
		
		statusBtn = new JButton("Offline");
		statusBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		statusBtn.setBounds(109, 61, 115, 40);
		statusBtn.setVisible(false);
		statusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userAccount.setStatus(userAccount.getStatus() ^ true);
				
				if(userAccount.getStatus())
				{
					statusBtn.setText("Offline");
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(2, true);
				}
				else
				{
					statusBtn.setText("Online");
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, false);
				}
			}
		});
		accountPanel.add(statusBtn);
		// End of account panel
		
		// Start of friend panel
		friendsPanel = new JPanel();
		
		friendListJL = new JLabel("Friend List :");
		friendListJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		friendListJL.setBounds(15, 20, 115, 20);
		friendsPanel.add(friendListJL);
		
		friendsPanel.setLayout(null);
		friendListBox = new JList<Person>(userAccount.getFriendList());
		friendListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		friendListBox.setBounds(15, 56, 180, 249);
		friendsPanel.add(friendListBox);
		
		addFriendBtn = new JButton("Add");
		addFriendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String friendName = friendNameJT.getText().toString();
				String[] notexist = {"YunYong" ,"jamie"}; // TO BE DELETED
				for(int i = 0; i< notexist.length; i++)
				{
					if(notexist[i].toLowerCase().equals(friendName.toLowerCase()))
					{
						JOptionPane.showMessageDialog(frame,
							    "Friend \"" + friendName + "\" does not exists",
							    "Adding friend failed",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				userAccount.addFriend(friendNameJT.getText().toString());
				friendNameJT.setText(null);
				friendListBox.setListData(userAccount.getFriendList());
				grpFriendListBox.setListData(userAccount.getFriendList());
			}
		});
		addFriendBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		addFriendBtn.setBounds(210, 56, 246, 29);
		friendsPanel.add(addFriendBtn);
		
		friendNameJT = new JTextField();
		friendNameJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		friendNameJT.setBounds(277, 16, 179, 29);
		friendsPanel.add(friendNameJT);
		friendNameJT.setColumns(10);
		
		deleteFriendBtn = new JButton("Delete");
		deleteFriendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selection = friendListBox.getSelectedIndex();
				if( selection >= 0 )
				{
					
					// Add this item to the list and refresh
					userAccount.removeFriend(selection);
					friendListBox.setListData( userAccount.getFriendList() );

					// As a nice touch, select the next item
					if( selection >= userAccount.getFriendList().size() )
						selection = userAccount.getFriendList().size() - 1;
					friendListBox.setSelectedIndex( selection );
				}
			}
		});
		deleteFriendBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		deleteFriendBtn.setBounds(210, 277, 246, 29);
		friendsPanel.add(deleteFriendBtn);
		
		pmFriendBtn = new JButton("Private Message");
		pmFriendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = friendListBox.getSelectedIndex();
				if( selection >= 0 )
				{
					String friend = userAccount.getFriendList().get(selection).getName();
					tabbedPane.setSelectedIndex(3);
					tabbedPane.setEnabledAt(3, true);
					pmFriendBtn.setEnabled(false);
					joinedGroupNameJT.setText(friend);
					
					pmTextPane.setText("");
					chattingWithJT.setText(friend);
					StyledDocument doc = pmTextPane.getStyledDocument();
			        Style style = pmTextPane.addStyle("I'm a Style", null);
			        StyleConstants.setForeground(style, Color.orange);

			        try { doc.insertString(doc.getLength(), "Private message to " + friend + "\n",style); }
			        catch (BadLocationException ex){}
				}
			}
		});
		pmFriendBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmFriendBtn.setBounds(210, 101, 246, 29);
		friendsPanel.add(pmFriendBtn);
		
		lblFriend = new JLabel("Friend");
		lblFriend.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFriend.setBounds(210, 16, 69, 29);
		friendsPanel.add(lblFriend);
		
		separator = new JSeparator();
		separator.setBounds(210, 90, 246, 2);
		friendsPanel.add(separator);
		
		// End of friend panel
		
		// Start of group panel
		groupsPanel = new JPanel();
		groupsPanel.setLayout(null);
		groupTabbedPane = new JTabbedPane();
		groupTabbedPane.setFont( new Font( "GroupTabTitle", Font.BOLD, 16 ) );
		groupsPanel.add(groupTabbedPane);
		
		//Start of SubPanel 1
		groupsSubPanel1 = new JPanel();
		groupsSubPanel1.setLayout(null);
		
		grpFriendListBox = new JList<Person>(userAccount.getFriendList());
		grpFriendListBox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		grpFriendListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpFriendListBox.setBounds(15, 45, 180, 350);
		groupsSubPanel1.add(grpFriendListBox);
		
		grpFriendListJL = new JLabel("Friend List :");
		grpFriendListJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpFriendListJL.setBounds(15, 16, 115, 20);
		groupsSubPanel1.add(grpFriendListJL);
		
		groupNameJL = new JLabel("Group");
		groupNameJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		groupNameJL.setBounds(219, 16, 69, 29);
		groupsSubPanel1.add(groupNameJL);
		
		groupNameJT = new JTextField();
		groupNameJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		groupNameJT.setBounds(288, 16, 264, 29);
		groupsSubPanel1.add(groupNameJT);
		groupNameJT.setColumns(10);
		
		groupListBox = new JList<Group>(userAccount.getGroupList());
		groupListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		groupListBox.setBounds(210, 143, 342, 201);
		groupsSubPanel1.add(groupListBox);
		
		groupCreateBtn = new JButton("Create");
		groupCreateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] groupexist = {"ICT2902" ,"ICT2108"}; // TO BE DELETED
				String groupName = groupNameJT.getText().toString();
				Vector<Group> groupList = userAccount.getGroupList();
				for(int i = 0; i<groupList.size(); i++)
				{
					if(groupList.get(i).getName().equals(groupName))
					{
						JOptionPane.showMessageDialog(frame,
							    "Group \"" + groupName + "\" already exists.",
							    "Creating group failed",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				for(int i = 0; i<groupexist.length; i++)
				{
					if(groupexist[i].equals(groupName))
					{
						JOptionPane.showMessageDialog(frame,
							    "Group \"" + groupName + "\" already exists.",
							    "Creating group failed",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				if(groupName.contains(" "))
				{
					JOptionPane.showMessageDialog(frame,
						    "Group name cannot contain space.",
						    "Creating group failed",
						    JOptionPane.ERROR_MESSAGE);
				}
				userAccount.addGroup(groupName, "235.1.2.3", userAccount.getName());
				groupNameJT.setText(null);
				groupListBox.setListData(userAccount.getGroupList());
			}
		});
		groupCreateBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		groupCreateBtn.setBounds(229, 58, 323, 29);
		groupsSubPanel1.add(groupCreateBtn);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(219, 103, 333, 2);
		groupsSubPanel1.add(separator_1);
		
		grpListJL = new JLabel("Group List :");
		grpListJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpListJL.setBounds(210, 115, 115, 20);
		groupsSubPanel1.add(grpListJL);
		
		groupListJL = new JLabel("Group List :");
		groupListJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		groupListJL.setBounds(210, 115, 115, 20);
		groupsSubPanel1.add(groupListJL);
		
		grpJoinBtn = new JButton("Join");
		grpJoinBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = groupListBox.getSelectedIndex();
				if( selection >= 0 )
				{
					String joinedGroup = userAccount.getGroupList().get(selection).getName();
					groupTabbedPane.setSelectedIndex(1);
					groupTabbedPane.setEnabledAt(0, false);
					groupTabbedPane.setEnabledAt(1, true);
					joinedGroupNameJT.setText(joinedGroup);
					
					chatMsgTextPane.setText("");
					StyledDocument doc = chatMsgTextPane.getStyledDocument();
			        Style style = chatMsgTextPane.addStyle("I'm a Style", null);
			        StyleConstants.setForeground(style, Color.blue);

			        try { doc.insertString(doc.getLength(), "Welcome to " + joinedGroup + "\n",style); }
			        catch (BadLocationException ex){}
				}
			}
		});
		grpJoinBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpJoinBtn.setBounds(210, 350, 342, 45);
		groupsSubPanel1.add(grpJoinBtn);
		
		// End of SubPanel 1
		
		// Start of SubPanel 2
		groupsSubPanel2 = new JPanel();
		groupsSubPanel2.setLayout(null);
		
		joinedGroupNameJT = new JTextField();
		joinedGroupNameJT.setEditable(false);
		joinedGroupNameJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		joinedGroupNameJT.setBounds(15, 16, 215, 29);
		groupsSubPanel2.add(joinedGroupNameJT);
		joinedGroupNameJT.setColumns(10);
		
		leaveGroupBtn = new JButton("Leave");
		leaveGroupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupTabbedPane.setSelectedIndex(0);
				groupTabbedPane.setEnabledAt(0, true);
				groupTabbedPane.setEnabledAt(1, false);
				joinedGroupNameJT.setText("");
				chatMsgTextPane.setText("");
			}
		});
		leaveGroupBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		leaveGroupBtn.setBounds(233, 16, 146, 29);
		groupsSubPanel2.add(leaveGroupBtn);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(15, 49, 537, 2);
		groupsSubPanel2.add(separator_2);
		
		chatMsgTextPane = new JTextPane();
		chatMsgTextPane.setBounds(15, 54, 537, 301);
		chatMsgTextPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chatMsgTextPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		groupsSubPanel2.add(chatMsgTextPane);
		
		grpMsgJL = new JLabel("Message");
		grpMsgJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpMsgJL.setBounds(15, 363, 91, 29);
		groupsSubPanel2.add(grpMsgJL);
		
		grpMsgJT = new JTextField();
		grpMsgJT.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					grpSendMsgBtn.doClick();
		        }
			}
		});
		grpMsgJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpMsgJT.setBounds(102, 364, 355, 26);
		groupsSubPanel2.add(grpMsgJT);
		grpMsgJT.setColumns(10);
		
		grpSendMsgBtn = new JButton("Send");
		grpSendMsgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "You : " + grpMsgJT.getText().toString() + "\n";
				StyledDocument doc = chatMsgTextPane.getStyledDocument();
		        Style style = chatMsgTextPane.addStyle("I'm a Style", null);
		        StyleConstants.setForeground(style, Color.black);
		        StyleConstants.setBold(style, true);
		        StyleConstants.setItalic(style, true);

		        try { doc.insertString(doc.getLength(), msg ,style); }
		        catch (BadLocationException ex){}
		        grpMsgJT.setText("");
			}
		});
		grpSendMsgBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		grpSendMsgBtn.setBounds(461, 363, 91, 29);
		groupsSubPanel2.add(grpSendMsgBtn);
		
		// End of SubPanel 2
		groupTabbedPane.add("Management", groupsSubPanel1);
		groupTabbedPane.add("Chat", groupsSubPanel2);
		groupTabbedPane.setEnabledAt(1, false);
		
		// End of group panel
		
		// Start of Private message panel
		privateMsgPanel = new JPanel();
		privateMsgPanel.setLayout(null);
		
		pmChatWithJL = new JLabel("Chatting with");
		pmChatWithJL.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmChatWithJL.setBounds(15, 16, 127, 29);
		privateMsgPanel.add(pmChatWithJL);
		
		chattingWithJT = new JTextField();
		chattingWithJT.setEditable(false);
		chattingWithJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chattingWithJT.setBounds(146, 16, 300, 29);
		privateMsgPanel.add(chattingWithJT);
		chattingWithJT.setColumns(10);
		
		pmTextPane = new JTextPane();
		pmTextPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmTextPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pmTextPane.setBounds(15, 71, 537, 310);
		privateMsgPanel.add(pmTextPane);
		
		pmMsgLabel = new JLabel("Message");
		pmMsgLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmMsgLabel.setBounds(15, 389, 91, 29);
		privateMsgPanel.add(pmMsgLabel);
		
		pmMsgJT = new JTextField();
		pmMsgJT.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					pmSendBtn.doClick();
		        }
			}
		});
		pmMsgJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmMsgJT.setColumns(10);
		pmMsgJT.setBounds(102, 390, 355, 26);
		privateMsgPanel.add(pmMsgJT);
		
		pmSendBtn = new JButton("Send");
		pmSendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "You : " + pmMsgJT.getText().toString() + "\n";
				StyledDocument doc = pmTextPane.getStyledDocument();
		        Style style = pmTextPane.addStyle("I'm a Style", null);
		        StyleConstants.setForeground(style, Color.black);
		        StyleConstants.setBold(style, true);
		        StyleConstants.setItalic(style, true);

		        try { doc.insertString(doc.getLength(), msg ,style); }
		        catch (BadLocationException ex){}
		        pmMsgJT.setText("");
			}
		});
		pmSendBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pmSendBtn.setBounds(461, 389, 91, 29);
		privateMsgPanel.add(pmSendBtn);
		
		separator_3 = new JSeparator();
		separator_3.setBounds(15, 56, 541, 2);
		privateMsgPanel.add(separator_3);
		
		pmQuitBtn = new JButton("Quit");
		pmQuitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chattingWithJT.setText(null);
				pmMsgJT.setText(null);
				pmTextPane.setText(null);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(3, false);
				pmFriendBtn.setEnabled(true);
			}
		});
		pmQuitBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pmQuitBtn.setBounds(461, 16, 91, 29);
		privateMsgPanel.add(pmQuitBtn);
		
		// End of Private message panel
		
		// Start of tabbedpane
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont( new Font( "Tahoma", Font.BOLD, 18 ) );
		contentPane.add(tabbedPane);
//		tabbedPane.setBounds(0, 0, 330, 160);
//		setBounds(100, 100, 336, 200);
//    	tabbedPane.setBounds(0, 0, 474, 360);
//    	setBounds(100, 100, 480, 400);
    	tabbedPane.setBounds(0, 0, 576, 470);
    	groupTabbedPane.setBounds(0, 0, 572, 430);
    	setBounds(100, 100, 580, 510);		
    	
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    switch(pane.getSelectedIndex())
                    {
	                    case 0:
	                    	// width - 4, height - 40
	                		tabbedPane.setBounds(0, 0, 330, 160);
	                		setBounds(100, 100, 336, 200);
	                    	break;
	                    case 1:
	                    	tabbedPane.setBounds(0, 0, 474, 360);
	                    	setBounds(100, 100, 480, 400);
	                    	break;
	                    case 2:
	                    	tabbedPane.setBounds(0, 0, 576, 470);
	                    	groupTabbedPane.setBounds(0, 0, 572, 430);
	                    	setBounds(100, 100, 580, 510);			
	                    	break;
	                    case 3:
	                    	tabbedPane.setBounds(0, 0, 576, 470);
	                    	groupTabbedPane.setBounds(0, 0, 572, 430);
	                    	setBounds(100, 100, 580, 510);			
	                    	break;
                    }
                }
				frame.setLocationRelativeTo(null);
			}
		});		

		tabbedPane.add("Account", accountPanel);
		tabbedPane.add("Friends", friendsPanel);
		tabbedPane.add("Groups", groupsPanel);
		tabbedPane.add("PM", privateMsgPanel);
		
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);
		// End of tabbedpane
	}
}
