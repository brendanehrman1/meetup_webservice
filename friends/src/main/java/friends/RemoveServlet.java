package friends;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/remove"})
public class RemoveServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userName = request.getParameter("userName");
		String friendName = request.getParameter("friendName");
		String status = null;

		try {
			status = DataStore.getInstance().removeFriend(userName, friendName);
		} catch (Exception var7) {
			response.getOutputStream().println(var7.toString());
		}

		String json = "{\n";
		json = json + "\"status\": " + status + "\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}