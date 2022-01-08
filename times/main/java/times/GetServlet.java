package times;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/get"})
public class GetServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userName = request.getParameter("userName");
		int date = Integer.parseInt(request.getParameter("date"));
		int hour = Integer.parseInt(request.getParameter("hour"));
		int minute = Integer.parseInt(request.getParameter("minute"));
		TreeSet list = null;

		try {
			list = DataStore.getInstance().getTimes(userName, date, hour, minute);
		} catch (Exception var11) {
			response.getOutputStream().println(var11.toString());
		}

		Iterator<TimeEntry> iter = list.iterator();
		String json = "{\n";

		for (json = json + "\"times\": [\n"; iter.hasNext(); json = json + "\n") {
			TimeEntry t = (TimeEntry) iter.next();
			json = json + "{\"date\": " + t.getDate() + ",\n";
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