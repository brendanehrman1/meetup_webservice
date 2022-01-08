package users;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/user"})
public class GetServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String displayName = request.getParameter("displayName");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = null;

		try {
			user = DataStore.getInstance().getUser(displayName, username, password);
		} catch (Exception var8) {
			response.getOutputStream().println(var8.toString());
		}

		String json = "{\n";
		json = json + "\"status\": " + user.getStatus() + ",\n";
		json = json + "\"displayName\": \"" + user.getName() + "\",\n";
		json = json + "\"username\": \"" + user.getUsername() + "\",\n";
		json = json + "\"password\": \"" + user.getPassword() + "\"\n";
		json = json + "}";
		response.getOutputStream().println(json);
	}
}