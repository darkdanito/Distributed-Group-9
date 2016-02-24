package GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Controller.CommandController;
import Model.Person;

public class CustomDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel;
	JLabel label;
	JButton inviteBtn;
	Container cp ;
	CommandController commandCtrl;
	String groupName;
	
	public CustomDialog(JFrame frame, CommandController commandCtrl, JList<Person> grpFriendList, String groupName)
	{
		super ( frame, "Invite friends", true ) ; 
		this.commandCtrl = commandCtrl;
		this.groupName = groupName;
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0,0,0,0);
		
		JList<Person> inviteFriendListBox = grpFriendList;
		inviteFriendListBox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		inviteFriendListBox.setCellRenderer(new DisabledItemListCellRenderer());
		inviteFriendListBox.addListSelectionListener(new ConditionableListSelectionListener());
		inviteFriendListBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inviteFriendListBox.setBounds(15, 44, 178, 243);
		//panel.add(inviteFriendListBox);

		label = new JLabel("Friend List :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(15, 16, 115, 20);
		//panel.add(label);
		
		inviteBtn = new JButton("Invite");
		inviteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Vector<String> selectedFriend = new Vector<String>();
				
				int[] selectedIndices = inviteFriendListBox.getSelectedIndices();
				for(int i = 0; i< selectedIndices.length; i++)
				{
					selectedFriend.add(commandCtrl.getUserAccount().getFriendList().get(i).getName());
				}
				commandCtrl.inviteFriendsToGroup(selectedFriend.toArray(new String[selectedFriend.size()]), groupName);
				dispose();
			}
		});
		inviteBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inviteBtn.setBounds(15, 303, 178, 29);
		//panel.add(inviteBtn);
		
		cp = getContentPane();
		cp.setLayout(null);
		cp.add(label);
		cp.add(inviteFriendListBox);
		cp.add(inviteBtn);
		setSize(215, 390);
		setLocationRelativeTo(null);
		setResizable(false);
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
