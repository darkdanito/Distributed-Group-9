package Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Model.*;
import GUI.ChattingAppGUI;

public class CommandController {
	private MulticastSocket mainMulticastSocket = null;
	private InetAddress mainMulticastGroup = null;
	private MulticastSocket pmMulticastSocket = null;
	private InetAddress pmMulticastGroup = null;
	private MulticastSocket grpMulticastSocket = null;
	private InetAddress grpMulticastGroup = null;
	
	private Thread mainThread = null;
	private Thread pmThread = null;
	private Thread grpChatThread = null;
	
	/**
	 * [0] - Check_Username | 
	 * [1] - Status_Changed | 
	 * [2] - Add_Friend | 
	 * [3] - Private_Chat | 
	 * [4] - IP_Used | 
	 * [5] - IP_Released | 
	 * [6] - Check_GroupName | 
	 * [7] - Create_NewGroup | 
	 * [8] - Remove_Friend
	 */
	public final String[] commandList = {"Check_Username", "Status_Changed", "Add_Friend", 
			"Private_Chat", "IP_Used", "IP_Released", "Check_GroupName", "Create_NewGroup", "Remove_Friend"};
	
	private final String[] messageTypes = {"Request", "Reply"};
	
	/**
	 * [0] - Not_Available | 
	 * [1] - Offline | 
	 * [2] - Online | 
	 * [3] - Accepted | 
	 * [4] - Rejected
	 */
	public final String[] responseList = {"NotAvailable", "Offline", "Online", "Accepted", "Rejected"};
	
	/**
	 * [0] - Color.Joined | 
	 * [1] - Color.Rejected | 
	 * [2] - Color.Left | 
	 * [3] - Color.UserName
	 */
	private final String[] colorCode = {"Color.Joined", "Color.Rejected", "Color.Left", "Color.UserName"};
	private Vector<String> usedIPAddressList = null;
	private ChattingAppGUI mainFrame = null;
	private UserAccount userAccount = null;
	
	public CommandController(ChattingAppGUI frame)
	{
		this.mainFrame = frame;
		userAccount = new UserAccount(ManagementFactory.getRuntimeMXBean().getName());
		usedIPAddressList = new Vector<String>();
		RunMainThread();
	}
	
	public UserAccount getUserAccount()
	{
		return this.userAccount;
	}
	
	public void registerUser(String username)
	{
		userAccount = new UserAccount(username);
	}
	
	/**
	 * @param command
	 * Get your command from commandList in CommandController
	 * @param content
	 * This parameter will be used to process the command
	 * E.g if check username availability, pass the username string to this parameter
	 * @param target 
	 * if target to all, pass "all" to this parameter
	 */
	public void sendRequest(String command, String content, String target)
	{
		String msg = messageTypes[0] + " " + target + " " + userAccount.getName() + 
				" " + command + " " + content;
		sendMsgToMainMCAddress(msg);
	}
	
	public boolean isFriendInList(String friendName)
	{
		Vector<Person> list = userAccount.getFriendList();
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getName().equals(friendName))
			{
				return true;
			}
		}
		return false;
	}
	
	public void createPrivateChat(String friendName)
	{
		String ipaddress = getIPAddress();
		String msg = messageTypes[0] + " all " + userAccount.getName() + 
				" " + commandList[4] + " " + ipaddress;
		sendMsgToMainMCAddress(msg); // Broadcast to inform all the ipaddress is in use.
		msg = messageTypes[0] + " " + friendName + " " + userAccount.getName() + 
				" " + commandList[3] + " " + ipaddress;
		sendMsgToMainMCAddress(msg); // Request friend for a private chat
		
		try
		{
			pmMulticastGroup = InetAddress.getByName(ipaddress);
			pmMulticastSocket = new MulticastSocket(6789);
			pmMulticastSocket.joinGroup(pmMulticastGroup);
	
			mainFrame.tabbedPane.setSelectedIndex(3);
			mainFrame.tabbedPane.setEnabledAt(3, true);
			mainFrame.pmFriendBtn.setEnabled(false);
			
			mainFrame.pmTextPane.setText("");
			mainFrame.chattingWithJT.setText(friendName);
			StyledDocument doc = mainFrame.pmTextPane.getStyledDocument();
	        Style style = mainFrame.pmTextPane.addStyle("I'm a Style", null);
	        StyleConstants.setForeground(style, Color.orange);
	
	        try { doc.insertString(doc.getLength(), "Private message to " + friendName + "\n",style); }
	        catch (BadLocationException ex){}
			setPMThread();
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void leavePrivateChat()
	{
		leavePM();
	}
	
	public void sendPMMessage(String message)
	{
		if(pmMulticastGroup != null && pmMulticastSocket != null )
		{
			try
			{
				String msg = colorCode[3] + userAccount.getName() + " : " + message + "\n";
				byte[] buf = msg.getBytes();
				DatagramPacket dgpConnected = 
						new DatagramPacket(buf, buf.length, pmMulticastGroup, 6789);
				pmMulticastSocket.send(dgpConnected);
		        mainFrame.pmMsgJT.setText("");
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}	
	
	public void joinGroup(int groupNameIndex)
	{
		startGroupChat(groupNameIndex);
	}
	
	public void sendGrpChatMessage(String message)
	{
		if(grpMulticastGroup != null && grpMulticastSocket != null )
		{
			try
			{
				String msg = colorCode[3] + userAccount.getName() + " : " + message + "\n";
				byte[] buf = msg.getBytes();
				DatagramPacket dgpConnected = 
						new DatagramPacket(buf, buf.length, grpMulticastGroup, 6789);
				grpMulticastSocket.send(dgpConnected);
		        mainFrame.grpMsgJT.setText("");
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void inviteFriendsToGroup(String[] friendNames, String groupName)
	{
		String ipaddress = "";
		Vector<Group> grpList = userAccount.getGroupList();
		for(int i=0; i<grpList.size(); i++)
		{
			if(grpList.get(i).getName().equals(groupName))
			{
				ipaddress = grpList.get(i).getIpAddress();
			}
		}
		addFriendToGroup(groupName, friendNames, ipaddress);
	}
	
	public void leaveGroup()
	{
		leaveGroupChat();
	}
	
	public void createGroupChat(String groupName, String[] friendNames)
	{
		Vector<Group> groupList = userAccount.getGroupList();
		if(groupName.contains(" "))
		{
			JOptionPane.showMessageDialog(mainFrame,
				    "Group name cannot contain space.",
				    "Creating group failed",
				    JOptionPane.ERROR_MESSAGE);
			mainFrame.groupCreateBtn.setEnabled(true);
			mainFrame.groupNameJT.setEnabled(true);
			return;
		}
		for(int i = 0; i<groupList.size(); i++)
		{
			if(groupList.get(i).getName().equals(groupName))
			{
				JOptionPane.showMessageDialog(mainFrame,
					    "Group \"" + groupName + "\" already exists.",
					    "Creating group failed",
					    JOptionPane.ERROR_MESSAGE);
				mainFrame.groupCreateBtn.setEnabled(true);
				mainFrame.groupNameJT.setEnabled(true);
				return;
			}
		}
		
		// Check group name if exist or not
		String checkGroupName = messageTypes[0] + " all " 
				+ userAccount.getName() + " " + commandList[6] + " " + groupName;
		sendMsgToMainMCAddress(checkGroupName);
		
		Timer timer = new Timer(1000, new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent arg0) {
			    // Code to be executed
				  if(!mainFrame.groupExist)
				  {
					  createGroup(groupName, friendNames);
					  mainFrame.groupCreateBtn.setEnabled(true);
					  mainFrame.groupNameJT.setEnabled(true);
					  mainFrame.groupNameJT.setText("");
				  }
			  }
			});
		timer.setRepeats(false); // Only execute once
		timer.start(); // Go go go!
	}
	
	private void createGroup(String groupName, String[] friendNames)//, String[] friendNames)
	{
		String ipaddress = getIPAddress();
		String msg = messageTypes[0] + " all " + userAccount.getName() + 
				" " + commandList[4] + " " + ipaddress;
		sendMsgToMainMCAddress(msg); // Broadcast to inform all the ipaddress is in use.
		
		userAccount.addGroup(groupName, ipaddress, userAccount.getName());
		mainFrame.groupListBox.setListData(userAccount.getGroupList());
		addFriendToGroup(groupName, friendNames, ipaddress);
	}
	
	private void addFriendToGroup(String groupName, String[] friendNames, String ipaddress)
	{
		for(int i = 0; i < friendNames.length; i++)
		{
			String createGroup = messageTypes[0] + " " + friendNames[i] + " "
					+ userAccount.getName() + " " + commandList[7] + " " + groupName + "_" + ipaddress;
			sendMsgToMainMCAddress(createGroup);
		}
	}
	
	private void RunMainThread()
	{
		try
		{
			mainMulticastGroup = InetAddress.getByName("235.1.1.1");
			mainMulticastSocket = new MulticastSocket(6789);
			
			//Join Main Group
			mainMulticastSocket.joinGroup(mainMulticastGroup);
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		mainThread = new Thread(new Runnable(){
			@Override
			public void run(){
				byte buf1[] = new byte[1000];
				DatagramPacket dgpReceived =
						new DatagramPacket(buf1, buf1.length);
				while(true)
				{
					try{
						mainMulticastSocket.receive(dgpReceived);
						byte receivedData[] = dgpReceived.getData();
						int length = dgpReceived.getLength();
						// Assumed we received string
						String msg = new String(receivedData, 0, length);
						handleMessage(msg.split(" "));
					}catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
		mainThread.start();
	}

	private void sendMsgToMainMCAddress(String message)
	{
		try
		{
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = 
					new DatagramPacket(buf, buf.length, mainMulticastGroup, 6789);
			mainMulticastSocket.send(dgpConnected);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private void handleMessage(String[] message)
	{
		// Command structure "<type> <target> <from> <command> <content>"		
		// if the message is from this application
		if(!userAccount.getStatus()) return; // if user offline
		
		if(message[2].equals(userAccount.getName())) return; 
		
		// if the message is not for all or this application or user account
		if(!message[1].equals(userAccount.getName()) && !message[1].equals("all")) return; 
		
		if(message[3].equals(commandList[0])) // if Check_Username command
		{
			runUserNameExistCommand(message);
		}
		else if(message[3].equals(commandList[1])) // if Status_Changed command
		{
			runStatusChanged(message);
		}
		else if(message[3].equals(commandList[2])) // if Add_Friend command
		{
			if(message[1].equals(userAccount.getName())) // only if target is to me
			{
				runAddFriend(message);
			}
		}
		else if(message[3].equals(commandList[3])) // if Private_Chat command
		{
			if(message[1].equals(userAccount.getName())) // only if target is to me
			{
				runPMRequest(message);
			}
		}
		else if(message[3].equals(commandList[4])) // if IP_Used command
		{
			usedIPAddressList.add(message[4]);
		}
		else if(message[3].equals(commandList[5])) // if IP_Released command
		{
			usedIPAddressList.remove(message[4]);
		}
		else if(message[3].equals(commandList[6])) // if Check_GroupName command
		{
			runCheckGroupName(message);
		}
		else if(message[3].equals(commandList[7])) // if Create_NewGroup command
		{
			if(message[1].equals(userAccount.getName())) // only if target is to me
			{
				runCreateNewGroup(message);
			}
		}
		else if(message[3].equals(commandList[8])) // if Remove_Friend command
		{
			runRemoveFriend(message);
		}
	}
	
	private void runRemoveFriend(String[] message)
	{
		Vector<Person> friendlist = userAccount.getFriendList();
		for(int i=0; i<friendlist.size(); i++)
		{
			if(friendlist.get(i).getName().equals(message[4]))
			{
				userAccount.removeFriend(i);
				mainFrame.friendListBox.setListData(userAccount.getFriendList());
				mainFrame.grpFriendListBox.setListData(userAccount.getFriendList());
				mainFrame.inviteFriendListBox.setListData(userAccount.getFriendList());
				return;
			}
		}
	}
	
	private void runCreateNewGroup(String[] message)
	{
		String[] response = message[4].split("_");
		Vector<Group> grpList = userAccount.getGroupList();
		for(int i=0; i<grpList.size(); i++)
		{
			// if group is already added
			if(grpList.get(i).getName().equals(response[0])) return;
		}
		
		Object[] options = {"Join", "Reject"};
		int n = JOptionPane.showOptionDialog(
				mainFrame,
				message[2] + " has invited you to " + response[0] + " group chat.\n" +
				"Would you like to join?",
				"Group Invitation",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
		if(n == JOptionPane.YES_OPTION)
		{
			userAccount.addGroup(response[0], response[1], message[2]);
			mainFrame.groupListBox.setListData(userAccount.getGroupList());
		}
	}
	
	private void runCheckGroupName(String[] message)
	{
		if(message[0].equals(messageTypes[0])){ // if it is a request
			Vector<Group> list = userAccount.getGroupList();
			for(int i = 0; i < list.size(); i++ )
			{
				if(list.get(i).getName().equals(message[4])) // if found group
				{
					if(list.get(i).getCreatedBy().equals(userAccount.getName())) // group created by this user
					{
						String msg = messageTypes[1] + " " + message[2] + " " 
								+ userAccount.getName() + " " + commandList[6] + " " + responseList[0] + "_" + message[4];
						sendMsgToMainMCAddress(msg);
						return;
					}
				}
			}
		}
		else if(message[0].equals(messageTypes[1])){ // if it is a reply
			if(message[1].equals(userAccount.getName())) // only if target is to me
			{
				String[] response = message[4].split("_");
				if(response[0].equals(responseList[0])) // if group exist
				{
					mainFrame.groupExist = true;
					JOptionPane.showMessageDialog(mainFrame,
						    "Group \"" + response[1] + "\" already exists.",
						    "Creating group failed",
						    JOptionPane.ERROR_MESSAGE);
					mainFrame.groupCreateBtn.setEnabled(true);
					mainFrame.groupNameJT.setEnabled(true);
				}
			}
		}
	}
	
	private void runPMRequest(String[] message)
	{
		int n = JOptionPane.showConfirmDialog(
				mainFrame,
				message[2] + " wants to chat with you. Chat?",
				"PM Request",
				JOptionPane.YES_NO_OPTION);
		
		if(n==JOptionPane.YES_OPTION)
		{
			startPM(message[4], message[2]);
		}
		else if(n == JOptionPane.NO_OPTION)
		{
			try{
				InetAddress pmmcg = InetAddress.getByName(message[4]);
				@SuppressWarnings("resource")
				MulticastSocket pmmcs = new MulticastSocket(6789);
				pmmcs.joinGroup(pmmcg);
				String msg = colorCode[1] + userAccount.getName() + " has rejected your PM request.\n";
				byte[] buf = msg.getBytes();
				DatagramPacket dgpConnected = 
						new DatagramPacket(buf, buf.length, pmmcg, 6789);
				pmmcs.send(dgpConnected);
				pmmcs.leaveGroup(pmmcg);
				pmmcg = null;
				pmmcs = null;
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private void runAddFriend(String[] message)
	{
		if(message[0].equals(messageTypes[0])){ // if it is a request
			Object[] options = {"Accept", "Reject"};
			String reply = messageTypes[1] + " " + message[2] + " " 
					+ userAccount.getName() + " " + commandList[2] + " ";
			int n = JOptionPane.showOptionDialog(
					mainFrame,
					"Would you like to accept " + message[2] + " as friend?",
					"Friend Request",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,     //do not use a custom Icon
					options,  //the titles of buttons
					options[0]); //default button title
			if(n == JOptionPane.YES_OPTION)
			{
				userAccount.addFriend(message[2]);
				reply += responseList[3];
			}
			else if(n == JOptionPane.NO_OPTION)
			{
				reply += responseList[4];
			}
			sendMsgToMainMCAddress(reply);
		}
		else if (message[0].equals(messageTypes[1])) // if it is reply
		{
			if(message[4].equals(responseList[3]))
			{
				userAccount.addFriend(message[2]);
			}
			else if(message[4].equals(responseList[4]))
			{
				JOptionPane.showMessageDialog(mainFrame,
					    message[2] + " has rejected your friend request.",
					    "Request rejected",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		mainFrame.friendListBox.setListData(userAccount.getFriendList());
		mainFrame.grpFriendListBox.setListData(userAccount.getFriendList());
		mainFrame.inviteFriendListBox.setListData(userAccount.getFriendList());
	}
	
	private void runStatusChanged(String[] message)
	{
		Vector<Person> friendList = userAccount.getFriendList();
		for(int i = 0; i < friendList.size(); i++)
		{
			if(friendList.get(i).getName().equals(message[2])) // if this person is in friend list
			{
				if(message[4].equals(responseList[1])) //if friends offline
				{
					friendList.get(i).setStatus(false);
				}
				else if(message[4].equals(responseList[2]))
				{
					friendList.get(i).setStatus(true);
				}
				break;
			}
		}
		mainFrame.friendListBox.setListData(friendList);
		mainFrame.grpFriendListBox.setListData(friendList);
		mainFrame.inviteFriendListBox.setListData(friendList);
	}
	
	private void runUserNameExistCommand(String[] message)
	{	
		if(message[0].equals(messageTypes[0])){ // if it is a request
			if(message[4].toLowerCase().equals(userAccount.getName()))
			{
				String msg = messageTypes[1] + " " + message[2] + " " + userAccount.getName() + 
						" " + commandList[0] + " " + responseList[0];
				sendMsgToMainMCAddress(msg);
			}
		}
		else if (message[0].equals(messageTypes[1])) // if it is reply
		{
			if(message[4].equals(responseList[0])) // if username not available
			{
				mainFrame.userNameExist = true;
				mainFrame.userNameJT.setEnabled(true);
				mainFrame.registerBtn.setEnabled(true);
				JOptionPane.showMessageDialog(mainFrame,
					    "Username \"" + mainFrame.userNameJT.getText().toString() + "\" is already in use.",
					    "Registration failed",
					    JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	private void startPM(String ipAddress, String friendName)
	{
		leavePM();
		try
		{
			pmMulticastGroup = InetAddress.getByName(ipAddress);
			pmMulticastSocket = new MulticastSocket(6789);
			pmMulticastSocket.joinGroup(pmMulticastGroup);
	
			mainFrame.tabbedPane.setSelectedIndex(3);
			mainFrame.tabbedPane.setEnabledAt(3, true);
			mainFrame.pmFriendBtn.setEnabled(false);
			
			mainFrame.pmTextPane.setText("");
			mainFrame.chattingWithJT.setText(friendName);
			StyledDocument doc = mainFrame.pmTextPane.getStyledDocument();
	        Style style = mainFrame.pmTextPane.addStyle("I'm a Style", null);
	        StyleConstants.setForeground(style, Color.orange);
	
	        try { doc.insertString(doc.getLength(), "Private message to " + friendName + "\n",style); }
	        catch (BadLocationException ex){}
	        
			String msg = colorCode[0] + userAccount.getName() + " has joined!\n";
			byte[] buf = msg.getBytes();
			DatagramPacket dgpConnected = 
					new DatagramPacket(buf, buf.length, pmMulticastGroup, 6789);
			pmMulticastSocket.send(dgpConnected);
			setPMThread();
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void setPMThread()
	{
		pmThread = new Thread(new Runnable(){
			@Override
			public void run(){
				byte buf1[] = new byte[1000];
				DatagramPacket dgpReceived =
						new DatagramPacket(buf1, buf1.length);
				while(true)
				{
					try{
						pmMulticastSocket.receive(dgpReceived);
						byte receivedData[] = dgpReceived.getData();
						int length = dgpReceived.getLength();
						// Assumed we received string
						String msg = new String(receivedData, 0, length);
						StyledDocument doc = mainFrame.pmTextPane.getStyledDocument();
				        Style style = mainFrame.pmTextPane.addStyle("I'm a Style", null);
				        if(msg.contains(colorCode[0])) // joined
				        {
				        	msg = msg.replace(colorCode[0], "");
				        	StyleConstants.setForeground(style, Color.green);
				        	StyleConstants.setBold(style, true);
				        	StyleConstants.setItalic(style, true);
				        	if(msg.contains(userAccount.getName()))
				        	{
				        		msg = msg.replace(userAccount.getName(), "You");
				        		msg = msg.replace("has", "have");
				        	}
				        }
				        else if(msg.contains(colorCode[1])) // reject
				        {
				        	msg = msg.replace(colorCode[1], "");
				        	StyleConstants.setForeground(style, Color.red);
				        	StyleConstants.setBold(style, true);
				        	StyleConstants.setItalic(style, true);
				        }
				        else if(msg.contains(colorCode[2])) // left
				        {
				        	msg = msg.replace(colorCode[2], "");
				        	StyleConstants.setForeground(style, Color.magenta);
				        	StyleConstants.setBold(style, true);
				        	StyleConstants.setItalic(style, true);
				        	if(msg.contains(userAccount.getName()))
				        	{
				        		msg = msg.replace(userAccount.getName(), "You");
				        		msg = msg.replace("has", "have");
				        	}
				        	else
				        	{
				        		try { doc.insertString(doc.getLength(), msg, style); }
						        catch (BadLocationException ex){}
				        		mainFrame.pmJSP.getVerticalScrollBar().setValue(mainFrame.pmJSP.getVerticalScrollBar().getMaximum());
				        		clearPMMCSocketAndGroup();
				        	}
				        } 
				        else if(msg.contains(colorCode[3])) // Username
				        {
				        	msg = msg.replace(colorCode[3], "");
				        	StyleConstants.setForeground(style, Color.black);
				        	if(msg.contains(userAccount.getName()))
				        	{
				        		msg = msg.replace(userAccount.getName(), "You");
					        	StyleConstants.setBold(style, true);
					        	StyleConstants.setItalic(style, true);
				        	}
				        	else
				        	{
				        		StyleConstants.setForeground(style, Color.black);
				        	}
				        }
				
				        try { doc.insertString(doc.getLength(), msg, style); }
				        catch (BadLocationException ex){}
				        mainFrame.pmJSP.getVerticalScrollBar().setValue(mainFrame.pmJSP.getVerticalScrollBar().getMaximum());
					}catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
		pmThread.start();
	}

	private void leavePM()
	{
		if(pmMulticastGroup == null && pmMulticastSocket == null )
		{
			mainFrame.chattingWithJT.setText(null);
			mainFrame.pmMsgJT.setText(null);
			mainFrame.pmTextPane.setText(null);
			mainFrame.tabbedPane.setSelectedIndex(1);
			mainFrame.tabbedPane.setEnabledAt(3, false);
			mainFrame.pmFriendBtn.setEnabled(true);
			return;
		}
		try
		{
			String msg = colorCode[2] + userAccount.getName() + " has left.\n";
			byte[] buf = msg.getBytes();
			DatagramPacket dgpConnected = 
					new DatagramPacket(buf, buf.length, pmMulticastGroup, 6789);
			pmMulticastSocket.send(dgpConnected);
			pmMulticastSocket.leaveGroup(pmMulticastGroup);
			usedIPAddressList.remove(pmMulticastGroup.getHostAddress());
			clearPMMCSocketAndGroup();
			mainFrame.chattingWithJT.setText(null);
			mainFrame.pmMsgJT.setText(null);
			mainFrame.pmTextPane.setText(null);
			mainFrame.tabbedPane.setSelectedIndex(1);
			mainFrame.tabbedPane.setEnabledAt(3, false);
			mainFrame.pmFriendBtn.setEnabled(true);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void startGroupChat(int groupNameIndex)
	{
		leaveGroupChat();
		try
		{
			grpMulticastGroup = InetAddress.getByName(userAccount.getGroupList().get(groupNameIndex).getIpAddress());
			grpMulticastSocket = new MulticastSocket(6789);
			grpMulticastSocket.joinGroup(grpMulticastGroup);
	
			mainFrame.groupTabbedPane.setSelectedIndex(1);
			mainFrame.groupTabbedPane.setEnabledAt(0, false);
			mainFrame.groupTabbedPane.setEnabledAt(1, true);
			mainFrame.joinedGroupNameJT.setText(userAccount.getGroupList().get(groupNameIndex).getName());
			
			mainFrame.chatMsgTextPane.setText("");
			StyledDocument doc = mainFrame.chatMsgTextPane.getStyledDocument();
	        Style style = mainFrame.chatMsgTextPane.addStyle("I'm a Style", null);
	        StyleConstants.setForeground(style, Color.orange);
	
	        try { doc.insertString(doc.getLength(), "Welcome to " + userAccount.getGroupList().get(groupNameIndex).getName() 
	        		+ " group chat!\n",style); }
	        catch (BadLocationException ex){}
	        
			String msg = colorCode[0] + userAccount.getName() + " has joined!\n";
			byte[] buf = msg.getBytes();
			DatagramPacket dgpConnected = 
					new DatagramPacket(buf, buf.length, grpMulticastGroup, 6789);
			grpMulticastSocket.send(dgpConnected);
			setGrpChatThread();
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void setGrpChatThread()
	{
		grpChatThread = new Thread(new Runnable(){
			@Override
			public void run(){
				byte buf1[] = new byte[1000];
				DatagramPacket dgpReceived =
						new DatagramPacket(buf1, buf1.length);
				while(true)
				{
					try{
						grpMulticastSocket.receive(dgpReceived);
						byte receivedData[] = dgpReceived.getData();
						int length = dgpReceived.getLength();
						// Assumed we received string
						String msg = new String(receivedData, 0, length);
						StyledDocument doc = mainFrame.chatMsgTextPane.getStyledDocument();
				        Style style = mainFrame.chatMsgTextPane.addStyle("I'm a Style", null);
				        if(msg.contains(colorCode[0])) // joined
				        {
				        	msg = msg.replace(colorCode[0], "");
				        	StyleConstants.setForeground(style, Color.green);
				        	StyleConstants.setBold(style, false);
				        	StyleConstants.setItalic(style, true);
				        	if(msg.contains(userAccount.getName()))
				        	{
				        		msg = msg.replaceFirst(userAccount.getName(), "You");
				        		msg = msg.replace("has", "have");
				        	}
				        }
				        else if(msg.contains(colorCode[2])) // left
				        {
				        	msg = msg.replace(colorCode[2], "");
				        	StyleConstants.setForeground(style, Color.magenta);
				        	StyleConstants.setBold(style, false);
				        	StyleConstants.setItalic(style, true);
				        } 
				        else if(msg.contains(colorCode[3])) // Username
				        {
				        	msg = msg.replace(colorCode[3], "");
				        	StyleConstants.setForeground(style, Color.black);
				        	if(msg.contains(userAccount.getName()))
				        	{
				        		msg = msg.replaceFirst(userAccount.getName(), "You");
					        	StyleConstants.setBold(style, true);
					        	StyleConstants.setItalic(style, true);
				        	}
				        	else
				        	{
				        		StyleConstants.setForeground(style, Color.black);
				        	}
				        }
				
				        try { doc.insertString(doc.getLength(), msg, style); }
				        catch (BadLocationException ex){}
				        mainFrame.chatMsgJSP.getVerticalScrollBar().setValue(mainFrame.chatMsgJSP.getVerticalScrollBar().getMaximum());
					}catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
		grpChatThread.start();
	}
	
	private void leaveGroupChat()
	{
		if(grpMulticastGroup == null && grpMulticastSocket == null )
		{
			mainFrame.groupTabbedPane.setSelectedIndex(0);
			mainFrame.groupTabbedPane.setEnabledAt(0, true);
			mainFrame.groupTabbedPane.setEnabledAt(1, false);
			mainFrame.joinedGroupNameJT.setText("");
			mainFrame.chatMsgTextPane.setText("");
			mainFrame.grpJoinBtn.setEnabled(true);
			return;
		}
		try
		{
			String msg = colorCode[2] + userAccount.getName() + " has left.\n";
			byte[] buf = msg.getBytes();
			DatagramPacket dgpConnected = 
					new DatagramPacket(buf, buf.length, grpMulticastGroup, 6789);
			grpMulticastSocket.send(dgpConnected);
			grpMulticastSocket.leaveGroup(grpMulticastGroup);
			clearGrpMCSocketAndGroup();
			mainFrame.groupTabbedPane.setSelectedIndex(0);
			mainFrame.groupTabbedPane.setEnabledAt(0, true);
			mainFrame.groupTabbedPane.setEnabledAt(1, false);
			mainFrame.joinedGroupNameJT.setText("");
			mainFrame.chatMsgTextPane.setText("");
			mainFrame.grpJoinBtn.setEnabled(true);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void clearPMMCSocketAndGroup(){
		String msg = messageTypes[0] + " all " + userAccount.getName() + 
				" " + commandList[5] + " " + pmMulticastGroup.getHostAddress();
		sendMsgToMainMCAddress(msg);
		pmThread.stop();
		pmThread = null;
		pmMulticastGroup = null;
		pmMulticastSocket = null;
	}
	
	@SuppressWarnings("deprecation")
	private void clearGrpMCSocketAndGroup(){
		grpChatThread.stop();
		grpChatThread = null;
		grpMulticastGroup = null;
		grpMulticastSocket = null;
	}
	
	private boolean isIPValid(String ipAddress)
	{
		String regex = "^(235).(1).([0-9]{1,3}).([0-9]{1,3})$"; // first and two number must be 235 and 1 respectively
		if(ipAddress.matches(regex))
		{
			if(!usedIPAddressList.contains(ipAddress))
				return true;
		}
		return false;
	}

	private String getIPAddress()
	{
		String ipaddress = "";
		do
		{
			Random r = new Random();
			ipaddress = "235.1." + r.nextInt(256) + "." + r.nextInt(256);
		}while(!isIPValid(ipaddress));
		return ipaddress;
	}
}
