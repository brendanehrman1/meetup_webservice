package times;

public class TimeEntry implements Comparable<TimeEntry> {
	private int date;
	private int hour;
	private int minute;
	private int duration;
	private String description;
	private String name;

	public TimeEntry(String name, int date, int hour, int minute, int duration, String description) {
		this.name = name;
		this.date = date;
		this.hour = hour;
		this.minute = minute;
		this.duration = duration;
		this.description = description;
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

	public int compareTo(TimeEntry t) {
		if (this.date != t.date) {
			return this.date - t.date;
		} else if (this.hour != t.hour) {
			return this.hour - t.hour;
		} else if (this.minute != t.minute) {
			return this.minute - t.minute;
		} else {
			return this.duration != t.duration ? this.duration - t.duration : this.description.compareTo(t.description);
		}
	}
}