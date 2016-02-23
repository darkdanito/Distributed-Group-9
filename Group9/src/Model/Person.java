package Model;

public class Person {
	private String name;
	protected boolean status;
	
	public Person(String name)
	{
		this.name = name;
		this.status = true;
	}
	
	/**
	 * Returns the name of the person
	 * @return name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns the status of the person
	 * @return false - Offline | true - Online
	 */
	public boolean getStatus()
	{
		return this.status;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
}
