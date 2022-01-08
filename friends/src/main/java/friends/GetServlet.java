package friends;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/get"})
public class GetServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userName = request.getParameter("userName");
		String friendName = request.getParameter("friendName");
		int pending = Integer.parseInt(request.getParameter("pending"));
		ArrayList list = null;

		try {
			list = DataStore.getInstance().getFriends(userName, friendName, pending);
		} catch (Exception var9) {
			response.getOutputStream().println(var9.toString());
		}

		String json = "{\n";
		json = json + "\"friends\": [\n";

		for (int i = 0; i < list.size() - 2; i += 2) {
			json = json + "{\"friendName\": \"" + (String) list.get(i) + "\",\n";
			json = json + "\"nickname\": \"" + (String) list.get(i + 1) + "\"},\n";
		}

		if (!list.isEmpty()) {
			json = json + "{\"friendName\": \"" + (String) list.get(list.size() - 2) + "\",\n";
			json = json + "\"nickname\": \"" + (String) list.get(list.size() - 1) + "\"}\n";
		}

		json = json + "]\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}