package friendTimes;

public class FriendTimeEntry implements Comparable<FriendTimeEntry> {
	private int date;
	private int hour;
	private int minute;
	private int duration;
	private String description;
	private String name;
	private String friendName;

	public FriendTimeEntry(String name, String friendName, int date, int hour, int minute, int duration,
			String description) {
		this.friendName = friendName;
		this.name = name;
		this.date = date;
		this.hour = hour;
		this.minute = minute;
		this.duration = duration;
		this.description = description;
	}

	public String getFriendName() {
		return this.friendName;
	}

	public String getName() {
		return this.name;
	}

	public int getDate() {
		return this.date;
	}

	public int getHour() {
		return this.hour;
	}

	public int getMinute() {
		return this.minute;
	}

	public int getDuration() {
		return this.duration;
	}

	public String getDescription() {
		return this.description;
	}

	public int compareTo(FriendTimeEntry t) {
		return this.getFriendName().compareTo(t.getFriendName());
	}
}