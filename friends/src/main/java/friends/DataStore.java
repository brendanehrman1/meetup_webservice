package friends;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataStore {
	String url = "jdbc:mysql://planowestapp1.c7qj6snmi45c.us-east-2.rds.amazonaws.com:3306/planowestapp1";
	String username = "admin";
	String pass = "a4phgU3pvqVWzwr4X2R6";
	private static DataStore instance = new DataStore();

	public static DataStore getInstance() {
		return instance;
	}

	public ArrayList<String> getFriends(String userName, String friendName, int pending)
			throws NumberFormatException, SQLException, ClassNotFoundException {
		ArrayList<String> friends = new ArrayList();
		String sql = "select * from friends where userName=\"" + userName + "\" and pending=" + pending;
		if (friendName != null) {
			sql = "select * from friends where friendName=\"" + friendName + "\" and pending=" + pending;
		}

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();

		for (ResultSet rs = st.executeQuery(sql); rs.next(); friends.add(rs.getString("nickname"))) {
			if (friendName == null) {
				friends.add(rs.getString("friendName"));
			} else {
				friends.add(rs.getString("userName"));
			}
		}

		return friends;
	}

	public String setPending(String userName, String friendName, int newPending)
			throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from friends where userName=\"" + userName + "\" and friendName=\"" + friendName + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (!rs.next()) {
			status = "notExist";
		} else {
			sql = "update friends set pending=" + newPending + " where userName=\"" + userName + "\" and friendName=\""
					+ friendName + "\"";
			st.executeUpdate(sql);
		}

		return status;
	}

	public String setNickname(String userName, String friendName, String nickname)
			throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from friends where userName=\"" + userName + "\" and friendName=\"" + friendName + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (!rs.next()) {
			status = "notExist";
		} else {
			sql = "select * from friends where userName=\"" + userName + "\" and nickname=\"" + nickname + "\"";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				status = "nickname";
			} else {
				sql = "update friends set nickname=\"" + nickname + "\" where userName=\"" + userName
						+ "\" and friendName=\"" + friendName + "\"";
				st.executeUpdate(sql);
			}
		}

		return status;
	}

	public String removeFriend(String userName, String friendName) throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from friends where userName=\"" + userName + "\" and friendName=\"" + friendName + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (!rs.next()) {
			status = "notExist";
		} else {
			sql = "delete from friends where userName=\"" + userName + "\" and friendName=\"" + friendName + "\"";
			st.executeUpdate(sql);
		}

		return status;
	}

	public String addFriend(String userName, String friendName, String nickname)
			throws ClassNotFoundException, SQLException {
		String status = "correct";
		String sql = "select * from users where displayName=\"" + userName + "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (!rs.next()) {
			status = "userNotExist";
		} else {
			sql = "select * from users where displayName=\"" + friendName + "\"";
			rs = st.executeQuery(sql);
			if (!rs.next()) {
				status = "friendNotExist";
			} else {
				sql = "select * from friends where userName=\"" + userName + "\" and friendName=\"" + friendName + "\"";
				rs = st.executeQuery(sql);
				if (rs.next()) {
					status = "alreadyPending";
				} else {
					sql = "select * from friends where userName=\"" + userName + "\" and nickname=\"" + nickname + "\"";
					rs = st.executeQuery(sql);
					if (rs.next()) {
						status = "nicknameUsed";
					} else {
						sql = "select * from friends where userName=\"" + friendName + "\" and friendName=\"" + userName
								+ "\" and pending=0";
						rs = st.executeQuery(sql);
						if (rs.next()) {
							sql = "insert into friends values (\"" + userName + "\", \"" + friendName + "\", \""
									+ nickname + "\", 0)";
						} else {
							sql = "insert into friends values (\"" + userName + "\", \"" + friendName + "\", \""
									+ nickname + "\", 1)";
						}

						st.executeUpdate(sql);
					}
				}
			}
		}

		return status;
	}
}