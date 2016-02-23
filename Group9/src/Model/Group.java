package Model;

public class Group{
	private String name;
	private String ipAddress;
	private String createdBy;

	public Group(String name, String ipAddress, String createdBy)
	{
		this.name = name;
		this.ipAddress = ipAddress;
		this.createdBy = createdBy;
	}
	
	public String getName() {
		return this.name;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
}
