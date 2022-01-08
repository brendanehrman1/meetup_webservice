package users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataStore {
	String url = "jdbc:mysql://planowestapp1.c7qj6snmi45c.us-east-2.rds.amazonaws.com:3306/planowestapp1";
	String username = "admin";
	String pass = "a4phgU3pvqVWzwr4X2R6";
	private static DataStore instance = new DataStore();

	public static DataStore getInstance() {
		return instance;
	}

	public User getUser(String displayName, String userName, String password)
			throws NumberFormatException, SQLException, ClassNotFoundException {
		String sql = "select * from users where username=\"" + userName + "\" and password=\"" + password + "\"";
		if (displayName != null) {
			sql = "select * from users where displayName=\"" + displayName + "\"";
		}

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		String display = null;
		String status = "correct";
		if (!rs.next()) {
			status = "notExist";
		} else {
			display = rs.getString("displayName");
			userName = rs.getString("username");
			password = rs.getString("password");
		}

		User b = new User(display, userName, password, status);
		return b;
	}

	public String removeUser(String name) throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from users where displayName=\"" + name + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (!rs.next()) {
			status = "notExist";
		} else {
			sql = "delete from friends where userName=\"" + name + "\" or friendName=\"" + name + "\"";
			st.executeUpdate(sql);
			sql = "delete from times where userName=\"" + name + "\"";
			st.executeUpdate(sql);
			sql = "delete from users where displayName=\"" + name + "\"";
			st.executeUpdate(sql);
		}

		return status;
	}

	public String changePassword(String name, String newPass) throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from users where displayName=\"" + name + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (!rs.next()) {
			status = "notExist";
		} else {
			String username = rs.getString("username");
			sql = "select * from users where displayName!=\"" + name + "\" and username=\"" + username
					+ "\" and password=\"" + newPass + "\"";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				status = "otherUser";
			} else {
				sql = "update users set password=\"" + newPass + "\" where displayName=\"" + name + "\"";
				st.executeUpdate(sql);
			}
		}

		return status;
	}

	public String addUser(String displayName, String userName, String password)
			throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from users where displayName=\"" + displayName + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			status = "displayName";
		} else {
			sql = "select * from users where username=\"" + userName + "\" and password=\"" + password + "\"";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				status = "userPass";
			} else {
				sql = "insert into users values (\"" + displayName + "\", \"" + userName + "\", \"" + password + "\")";
				st.executeUpdate(sql);
			}
		}

		return status;
	}
}