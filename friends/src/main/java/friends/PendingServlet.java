package friends;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/mod"})
public class PendingServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userName = request.getParameter("userName");
		String friendName = request.getParameter("friendName");
		int newPending = -1;
		String nickname = null;
		if (request.getParameter("pending") != null) {
			newPending = Integer.parseInt(request.getParameter("pending"));
		} else {
			nickname = request.getParameter("nickname");
		}

		String status = null;

		try {
			if (nickname == null) {
				status = DataStore.getInstance().setPending(userName, friendName, newPending);
			} else {
				status = DataStore.getInstance().setNickname(userName, friendName, nickname);
			}
		} catch (Exception var9) {
			response.getOutputStream().println(var9.toString());
		}

		String json = "{\n";
		json = json + "\"status\": " + status + "\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}