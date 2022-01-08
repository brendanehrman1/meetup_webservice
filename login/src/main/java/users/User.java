package users;

public class User {
	private String displayName;
	private String username;
	private String password;
	private String status;

	public User(String display, String user, String pass, String status) {
		this.displayName = display;
		this.username = user;
		this.password = pass;
		this.status = status;
	}

	public String getName() {
		return this.displayName;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getStatus() {
		return this.status;
	}
}