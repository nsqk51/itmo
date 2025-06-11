package client.Data;


public class CurrentClient  {
	

	private String userName;
	private String userPassword;
	
	public CurrentClient(String userName, String userPassword) {
		setUserName(userName);
		setUserPassword(userPassword);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
}