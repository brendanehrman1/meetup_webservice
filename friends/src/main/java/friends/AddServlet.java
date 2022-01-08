package friends;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/add"})
public class AddServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userName = request.getParameter("userName");
		String friendName = request.getParameter("friendName");
		String nickname = request.getParameter("nickname");
		String status = null;

		try {
			status = DataStore.getInstance().addFriend(userName, friendName, nickname);
		} catch (Exception var8) {
			response.getOutputStream().println(var8.toString());
		}

		String json = "{\n";
		json = json + "\"status\": " + status + "\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}