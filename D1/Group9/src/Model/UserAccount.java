package Model;

import java.util.Vector;

public class UserAccount extends Person{

	private Vector<Person> friendList;
	private Vector<Group> groupsList;
	
	public UserAccount(String name) {
		super(name);
		friendList = new Vector<Person>();
		groupsList = new Vector<Group>();
	}
	
	public String getName()
	{
		return super.getName();
	}
	
	public boolean getStatus()
	{
		return super.status;
	}

	public void setStatus(boolean status)
	{
		super.status = status;
	}
	
	public Vector<Person> getFriendList()
	{
		return new Vector<Person>(this.friendList);
	}
	
	public Vector<Group> getGroupList()
	{
		return new Vector<Group>(this.groupsList);
	}
	
	public void addFriend(String friendName)
	{
		friendList.add(new Person(friendName));
	}
	
	public void removeFriend(int index)
	{
		friendList.removeElementAt( index );
	}
	
	public void addGroup(String groupName, String ipAddress, String createdBy)
	{
		groupsList.add(new Group(groupName, ipAddress, createdBy));
	}
}
