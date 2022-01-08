package times;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/update"})
public class UpdateServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int date = Integer.parseInt(request.getParameter("date"));
		int hour = Integer.parseInt(request.getParameter("hour"));
		int minute = Integer.parseInt(request.getParameter("minute"));
		String status = null;

		try {
			status = DataStore.getInstance().updateTimes(date, hour, minute);
		} catch (Exception var8) {
			response.getOutputStream().println(var8.toString());
		}

		String json = "{\n";
		json = json + "\"status\": " + status + "\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}