package times;

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

	public TreeSet<TimeEntry> getTimes(String userName, int date, int hour, int minute)
			throws NumberFormatException, SQLException, ClassNotFoundException {
		TreeSet<TimeEntry> times = new TreeSet();
		int time = hour * 60 + minute;
		String sql = "select * from times where userName=\"" + userName + "\"";
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
				TimeEntry t = new TimeEntry(rs.getString("userName"), potDate, potHr, potMin, potDuration,
						rs.getString("description"));
				times.add(t);
			}
		}

		return times;
	}

	public TreeSet<TimeEntry> getFriendTimes(String userName, int date)
			throws NumberFormatException, SQLException, ClassNotFoundException {
		TreeSet<TimeEntry> times = new TreeSet();
		String sql = "select * from friends where userName=\"" + userName + "\" and pending=0";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);

		int potHr;
		int potMin;
		int potDuration;
		while (rs.next()) {
			sql = "select * from times where userName=\"" + rs.getString("friendName") + "\" and date>=" + date
					+ " and date<=" + (date + 2);
			st = con.createStatement();
			ResultSet friendRs = st.executeQuery(sql);

			while (friendRs.next()) {
				potHr = friendRs.getInt("date");
				potMin = friendRs.getInt("hour");
				potDuration = friendRs.getInt("minute");
				int friendDuration = friendRs.getInt("duration");
				TimeEntry t = new TimeEntry(friendRs.getString("userName"), potHr, potMin, potDuration, friendDuration,
						friendRs.getString("description"));
				times.add(t);
			}
		}

		sql = "select * from times where userName=\"" + userName + "\" and date>=" + date + " and date<=" + (date + 2);
		rs = st.executeQuery(sql);

		while (rs.next()) {
			int potDate = rs.getInt("date");
			potHr = rs.getInt("hour");
			potMin = rs.getInt("minute");
			potDuration = rs.getInt("duration");
			TimeEntry t = new TimeEntry(rs.getString("userName"), potDate, potHr, potMin, potDuration,
					rs.getString("description"));
			times.add(t);
		}

		return times;
	}

	public String updateTimes(int date, int hour, int minute)
			throws NumberFormatException, SQLException, ClassNotFoundException {
		String sql = "delete from times where date<" + date + " or date=" + date + " and hour<" + hour + " or date="
				+ date + " and hour=" + hour + " and minute<" + minute;
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.pass);
		Statement st = con.createStatement();
		st.executeUpdate(sql);
		return "correct";
	}

	public String addTime(String userName, int date, int hour, int minute, int duration, String description)
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
			sql = "select * from times where userName=\"" + userName + "\" and date=" + date + " and hour=" + hour
					+ " and minute=" + minute + " and duration=" + duration + " and description=\"" + description
					+ "\"";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				status = "alreadyExists";
			} else {
				sql = "insert into times values (\"" + userName + "\", " + date + ", " + hour + ", " + minute + ", "
						+ duration + ", \"" + description + "\")";
				st.executeUpdate(sql);
			}
		}

		return status;
	}

	public String removeTime(String userName, int date, int hour, int minute, int duration, String description)
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
			sql = "select * from times where userName=\"" + userName + "\" and date=" + date + " and hour=" + hour
					+ " and minute=" + minute + " and duration=" + duration + " and description=\"" + description
					+ "\"";
			rs = st.executeQuery(sql);
			if (!rs.next()) {
				status = "notExist";
			} else {
				sql = "delete from times where userName=\"" + userName + "\" and date=" + date + " and hour=" + hour
						+ " and minute=" + minute + " and duration=" + duration + " and description=\"" + description
						+ "\"";
				st.executeUpdate(sql);
			}
		}

		return status;
	}
}