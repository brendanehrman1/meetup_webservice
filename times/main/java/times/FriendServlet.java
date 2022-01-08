package times;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/friends"})
public class FriendServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userName = request.getParameter("userName");
		int date = Integer.parseInt(request.getParameter("date"));
		TreeSet list = null;

		try {
			list = DataStore.getInstance().getFriendTimes(userName, date);
		} catch (Exception var9) {
			response.getOutputStream().println(var9.toString());
		}

		Iterator<TimeEntry> iter = list.iterator();
		String json = "{\n";

		for (json = json + "\"times\": [\n"; iter.hasNext(); json = json + "\n") {
			TimeEntry t = (TimeEntry) iter.next();
			json = json + "{\"displayName\": \"" + t.getName() + "\",\n";
			json = json + "\"date\": " + t.getDate() + ",\n";
			json = json + "\"hour\": " + t.getHour() + ",\n";
			json = json + "\"minute\": " + t.getMinute() + ",\n";
			json = json + "\"duration\": " + t.getDuration() + ",\n";
			json = json + "\"description\": \"" + t.getDescription() + "\"}";
			if (iter.hasNext()) {
				json = json + ",";
			}
		}

		json = json + "]\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}