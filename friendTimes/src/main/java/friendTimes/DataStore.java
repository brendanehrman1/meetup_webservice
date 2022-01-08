package friendTimes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;

public class DataStore {
	String url = "jdbc:mysql://planowestapp1.c7qj6snmi45c.us-east-2.rds.amazonaws.com:3306/planowestapp1";
	String username = "admin";
	String pass = "a4phgU3pvqVWzwr4X2R6";
	private static DataStore instance = new DataStore();

	public static DataStore getInstance() {
		return instance;
	}

	public TreeSet<FriendTimeEntry> getFriendTimes(String userName, int date, int hour, int minute, int duration,
			String description) throws NumberFormatException, SQLException, ClassNotFoundException {
		TreeSet<FriendTimeEntry> times = new TreeSet();
		int time = hour * 60 + minute;
		String sql = "select * from friendTimes where userName=\"" + userName + "\" and date=" + date + " and hour="
				+ hour + " and minute=" + minute + " and duration=" + duration + " and description=\"" + description
				+ "\"";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			int potDate = rs.getInt("date");
			int potHr = rs.getInt("hour");
			int potMin = rs.getInt("minute");
			int potDuration = rs.getInt("duration");
			int stTime = potHr * 60 + potMin;
			stTime -= (date - potDate) * 1440;
			int endTime = stTime + potDuration;
			if (endTime >= time) {
				FriendTimeEntry t = new FriendTimeEntry(rs.getString("userName"), rs.getString("friendName"), potDate,
						potHr, potMin, potDuration, rs.getString("description"));
				times.add(t);
			}
		}

		return times;
	}

	public String addFriendTime(String userName, String friendName, int date, int hour, int minute, int duration,
			String description) throws ClassNotFoundException, SQLException {
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
				sql = "select * from friendTimes where userName=\"" + userName + "\" and friendName=\"" + friendName
						+ "\" and date=" + date + " and hour=" + hour + " and minute=" + minute + " and duration="
						+ duration + " and description=\"" + description + "\"";
				rs = st.executeQuery(sql);
				if (rs.next()) {
					status = "alreadyExists";
				} else {
					sql = "insert into friendTimes values (\"" + userName + "\", \"" + friendName + "\", " + date + ", "
							+ hour + ", " + minute + ", " + duration + ", \"" + description + "\")";
					st.executeUpdate(sql);
				}
			}
		}

		return status;
	}

	public String removeFriendTime(String userName, String friendName, int date, int hour, int minute, int duration,
			String description) throws ClassNotFoundException, SQLException {
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
				sql = "select * from times where userName=\"" + userName + "\" and date=" + date + " and hour=" + hour
						+ " and minute=" + minute + " and duration=" + duration + " and description=\"" + description
						+ "\"";
				rs = st.executeQuery(sql);
				if (!rs.next()) {
					status = "notExist";
				} else {
					sql = "delete from friendTimes where userName=\"" + userName + "\" and friendName=\"" + friendName
							+ "\" and date=" + date + " and hour=" + hour + " and minute=" + minute + " and duration="
							+ duration + " and description=\"" + description + "\"";
					st.executeUpdate(sql);
				}
			}
		}

		return status;
	}
}