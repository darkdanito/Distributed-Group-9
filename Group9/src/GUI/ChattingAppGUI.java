package GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Controller.CommandController;
import Model.*;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
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
	public JTabbedPane tabbedPane;
	public JTabbedPane groupTabbedPane;
	
	// JTextFields
	public JTextField userNameJT;
	public JTextField friendNameJT;
	public JTextField groupNameJT;
	public JTextField joinedGroupNameJT;
	public JTextField chattingWithJT;
	public JTextField grpMsgJT;
	public JTextField pmMsgJT;
	
	// JButtons
	public JButton registerBtn;
	public JButton statusBtn;
	public JButton addFriendBtn;
	public JButton pmFriendBtn;
	public JButton groupCreateBtn;
	public JButton grpJoinBtn;
	public JButton leaveGroupBtn;
	public JButton pmSendBtn;
	public JButton pmQuitBtn;
	public JButton grpSendMsgBtn;
	public JButton inviteFriendBtn;
	
	// JLists
	public JList<Person> friendListBox;
	public JList<Person> grpFriendListBox;
	public JList<Group> groupListBox;
	
	// Other JUnit
	public JTextPane chatMsgTextPane;
	public JTextPane pmTextPane;
	public JScrollPane pmJSP;
	public JScrollPane chatMsgJSP;
	public JScrollPane friendListBoxMsgJSP;
	public JScrollPane grpFriendListBoxMsgJSP;
	public JScrollPane grpListBoxMsgJSP;
	private JLabel lblUsername;
	private JLabel grpListJL;
	private JLabel groupNameJL;
	private JLabel lblFriend;
	private JLabel grpFriendListJL;
	private JLabel groupListJL;
	private JLabel friendListJL;
	private JLabel grpMsgJL;
	private JLabel pmChatWithJL;
	private JLabel pmMsgLabel;
	private JLabel lblWelcomeTo;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JSeparator separator_3;
	
	// Others variables
	private UserAccount userAccount = new UserAccount("UNREGISTERED UserAccount OBJECT");
	private CommandController commandCtrl;
	public boolean userNameExist = false;
	public boolean groupExist = false;
	
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
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	commandCtrl.leaveGroup();
				commandCtrl.leavePrivateChat();
				commandCtrl.sendRequest(commandCtrl.commandList[8], commandCtrl.getUserAccount().getName(), "all");
		        System.exit(0);
		    }
		});
		UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 18));
		UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 18));
		commandCtrl = new CommandController(this);
		
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

		lblWelcomeTo = new JLabel("Welcome to 2107 Chat App");
		lblWelcomeTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeTo.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblWelcomeTo.setBounds(42, 0, 295, 49);
		accountPanel.add(lblWelcomeTo);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsername.setBounds(42, 53, 99, 33);
		accountPanel.add(lblUsername);
		
		userNameJT = new JTextField();
		userNameJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		userNameJT.setBounds(156, 53, 181, 33);
		accountPanel.add(userNameJT);
		userNameJT.setColumns(10);
		
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String userName = userNameJT.getText().toString();
				
				if(userName.equals("")){
					JOptionPane.showMessageDialog(frame, "Username cannot be empty.",
							"Registration failed!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(userName.contains(" "))
				{
					JOptionPane.showMessageDialog(frame, "Username cannot contain space(s).",
							"Registration failed!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				userNameExist = false;
				userNameJT.setEnabled(false);
				registerBtn.setEnabled(false);
				commandCtrl.sendRequest(commandCtrl.commandList[0], userName, "all");
				
				Timer timer = new Timer(1000, new ActionListener() {
					  @Override
					  public void actionPerformed(ActionEvent arg0) {
					    // Code to be executed
						  if(!userNameExist)
						  {
							  commandCtrl.registerUser(userName);
							  tabbedPane.setEnabledAt(1, true);
							  tabbedPane.setEnabledAt(2, true);
							  userNameJT.setEnabled(false);
							  statusBtn.setVisible(true);
							  registerBtn.setVisible(false);
							  frame.setTitle("(Online) " + userName);
						  }
					  }
					});
				timer.setRepeats(false); // Only execute once
				timer.start(); // Go go go!
			}
		});
		registerBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		registerBtn.setBounds(136, 98, 115, 40);
		accountPanel.add(registerBtn);
		
		statusBtn = new JButton("Offline");
		statusBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		statusBtn.setBounds(136, 98, 115, 40);
		statusBtn.setVisible(false);
		statusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandCtrl.getUserAccount().setStatus(commandCtrl.getUserAccount().getStatus() ^ true);
				if(commandCtrl.getUserAccount().getStatus())
				{
					statusBtn.setText("Offline");
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(2, true);
					commandCtrl.sendRequest(commandCtrl.commandList[1], commandCtrl.responseList[2], "all");
					frame.setTitle("(Online) " + commandCtrl.getUserAccount().getName());
				}
				else
				{
					statusBtn.setText("Online");
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(2, false);
					commandCtrl.leaveGroup();
					commandCtrl.leavePrivateChat();
					commandCtrl.sendRequest(commandCtrl.commandList[1], commandCtrl.responseList[1], "all");
					tabbedPane.setSelectedIndex(0);
					frame.setTitle("(Offline) " + commandCtrl.getUserAccount().getName());
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
		friendListBoxMsgJSP = new JScrollPane(friendListBox);
		friendListBox.setCellRenderer(new DisabledItemListCellRenderer());
		friendListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		friendListBoxMsgJSP.setBounds(15, 56, 180, 249);
		friendsPanel.add(friendListBoxMsgJSP);
		
		addFriendBtn = new JButton("Add");
		addFriendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String friendName = friendNameJT.getText().toString();
				
				if(friendName.equals("")){
					JOptionPane.showMessageDialog(frame, "Friend field cannot be empty.",
							"Registration failed!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(friendName.contains(" "))
				{
					JOptionPane.showMessageDialog(frame, "Friend field cannot contain space(s).",
							"Registration failed!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(friendName.equals(commandCtrl.getUserAccount().getName()))
				{
					JOptionPane.showMessageDialog(frame, "You cannot add yourself as friend!",
						    "Adding friend failed", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else if(commandCtrl.isFriendInList(friendName))
				{
					JOptionPane.showMessageDialog(frame, "Friend is already in friend list!",
						    "Adding friend failed", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(frame, "Your friend request sent.",
					    "Friend Request", JOptionPane.INFORMATION_MESSAGE);
				commandCtrl.sendRequest(commandCtrl.commandList[2], "NIL", friendName);
				friendNameJT.setText(null);
			}
		});
		addFriendBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		addFriendBtn.setBounds(211, 96, 246, 29);
		friendsPanel.add(addFriendBtn);
		
		friendNameJT = new JTextField();
		friendNameJT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		friendNameJT.setBounds(278, 56, 179, 29);
		friendsPanel.add(friendNameJT);
		friendNameJT.setColumns(10);
		
		pmFriendBtn = new JButton("Private Message");
		pmFriendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = friendListBox.getSelectedIndex();
				if( selection >= 0 )
				{
					String friend = commandCtrl.getUserAccount().getFriendList().get(selection).getName();
					commandCtrl.createPrivateChat(friend);
				}
			}
		});
		pmFriendBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmFriendBtn.setBounds(211, 141, 246, 29);
		friendsPanel.add(pmFriendBtn);
		
		lblFriend = new JLabel("Friend");
		lblFriend.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFriend.setBounds(211, 56, 69, 29);
		friendsPanel.add(lblFriend);
		
		separator = new JSeparator();
		separator.setBounds(211, 133, 246, 2);
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
		grpFriendListBoxMsgJSP = new JScrollPane(grpFriendListBox);
		grpFriendListBox.setCellRenderer(new DisabledItemListCellRenderer());
		grpFriendListBox.addListSelectionListener(new ConditionableListSelectionListener());
		grpFriendListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpFriendListBoxMsgJSP.setBounds(15, 45, 180, 350);
		groupsSubPanel1.add(grpFriendListBoxMsgJSP);
		
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
		grpListBoxMsgJSP = new JScrollPane(groupListBox);
		groupListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		grpListBoxMsgJSP.setBounds(210, 143, 342, 201);
		groupsSubPanel1.add(grpListBoxMsgJSP);
		
		groupCreateBtn = new JButton("Create");
		groupCreateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String groupName = groupNameJT.getText().toString();
				
				if(groupName.equals("")){
					JOptionPane.showMessageDialog(frame, "Group name cannot be empty.",
							"Creating group failed!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(groupName.contains(" "))
				{
					JOptionPane.showMessageDialog(frame, "Group name cannot contain space(s).",
							"Creating group failed!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				groupCreateBtn.setEnabled(false);
				groupNameJT.setEnabled(false);
				
				groupExist = false;
				Vector<String> selectedFriend = new Vector<String>();
				
				int[] selectedIndices = grpFriendListBox.getSelectedIndices();
				
				if(selectedIndices.length <= 0)
				{
					Object[] options = {"Create", "Cancel"};
					int n = JOptionPane.showOptionDialog(
							frame,
							"You did not invite any friends.\nContinue creating group?",
							"Group Invitation",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							null,     //do not use a custom Icon
							options,  //the titles of buttons
							options[0]); //default button title
					if(n == JOptionPane.NO_OPTION)
					{
						groupCreateBtn.setEnabled(true);
						groupNameJT.setEnabled(true);
						return;
					}
				}
				for(int i = 0; i< selectedIndices.length; i++)
				{
					selectedFriend.add(commandCtrl.getUserAccount().getFriendList().get(i).getName());
				}
				commandCtrl.createGroupChat(groupName, selectedFriend.toArray(new String[selectedFriend.size()]));
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
					grpJoinBtn.setEnabled(false);
					commandCtrl.joinGroup(selection);
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
				commandCtrl.leaveGroup();
			}
		});
		leaveGroupBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		leaveGroupBtn.setBounds(238, 16, 146, 29);
		groupsSubPanel2.add(leaveGroupBtn);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(15, 49, 537, 2);
		groupsSubPanel2.add(separator_2);
		
		chatMsgTextPane = new JTextPane();
		chatMsgJSP = new JScrollPane(chatMsgTextPane);
		chatMsgJSP.setBounds(15, 54, 537, 301);
		chatMsgTextPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chatMsgTextPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		groupsSubPanel2.add(chatMsgJSP);
		
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
				String msg = grpMsgJT.getText().toString();
				if(msg.equals("")) return;
				commandCtrl.sendGrpChatMessage(msg);
			}
		});
		grpSendMsgBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		grpSendMsgBtn.setBounds(461, 363, 91, 29);
		groupsSubPanel2.add(grpSendMsgBtn);
		
		// End of SubPanel 2
		groupTabbedPane.add("Management", groupsSubPanel1);
		groupTabbedPane.add("Chat", groupsSubPanel2);
		
		inviteFriendBtn = new JButton("Invite Friends");
		inviteFriendBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				CustomDialog a= new CustomDialog(frame, commandCtrl, grpFriendListBox, joinedGroupNameJT.getText().toString());
				a.show();
			}
		});
		inviteFriendBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inviteFriendBtn.setBounds(388, 16, 164, 29);
		groupsSubPanel2.add(inviteFriendBtn);
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
		pmJSP = new JScrollPane(pmTextPane);
		pmJSP.setViewportView(pmTextPane);
		pmTextPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pmTextPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pmJSP.setBounds(15, 71, 537, 310);
		
		privateMsgPanel.add(pmJSP);
		
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
				String msg = pmMsgJT.getText().toString();
				if(msg.equals("")) return;
				commandCtrl.sendPMMessage(msg);
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
				commandCtrl.leavePrivateChat();
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
    	

		tabbedPane.setSize(380, 190);
		setSize(386, 230);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    switch(pane.getSelectedIndex())
                    {
	                    case 0:
	                    	// width - 6, height - 40
	                    	tabbedPane.setSize(380, 190);
	                		setSize(386, 230);
	                    	break;
	                    case 1:
	                    	tabbedPane.setSize(474, 360);
	                    	setSize(480, 400);
	                    	break;
	                    case 2:
	                    	tabbedPane.setSize(576, 470);
	                    	groupTabbedPane.setSize(572, 430);
	                    	setSize(580, 510);			
	                    	break;
	                    case 3:
	                    	tabbedPane.setSize(576, 470);
	                    	groupTabbedPane.setSize(572, 430);
	                    	setSize(580, 510);			
	                    	break;
                    }
                }
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
	
	private class DisabledItemListCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        	setText((value == null) ? "" : value.toString());
            if (isSelected) {
               setBackground(list.getSelectionBackground());
               setForeground(list.getSelectionForeground());
            }
            else {
               setBackground(list.getBackground());
               setForeground(list.getForeground());
            }
        
            if (!commandCtrl.getUserAccount().getFriendList().get(index).getStatus()) {
               setBackground(list.getBackground());
               setForeground(UIManager.getColor("Label.disabledForeground"));
            }
        
            setEnabled(commandCtrl.getUserAccount().getFriendList().get(index).getStatus());
            setFont(list.getFont());
        
            return this;
        }
    }
	private class ConditionableListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) 
		{
			@SuppressWarnings("unchecked")
			JList<Person> list = (JList<Person>) e.getSource();
			for (int i=e.getFirstIndex(); i<=e.getLastIndex(); i++) {
				if (list.getSelectionModel().isSelectedIndex(i)) {
					if (!commandCtrl.getUserAccount().getFriendList().get(i).getStatus()) {
						list.removeSelectionInterval(i, i);
					}
				}
			}
		}
	}
}
