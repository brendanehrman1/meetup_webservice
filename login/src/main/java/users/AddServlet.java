package users;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/add"})
public class AddServlet extends HttpServlet {
    public AddServlet() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String displayName = request.getParameter("displayName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String status = null;

        try {
            status = DataStore.getInstance().addUser(displayName, username, password);
        } catch (Exception e) {
            response.getOutputStream().println(e.toString());
        }

        String json = "{\n";
        json = json + "\"status\": " + status + "\n";
        json = json + "}";
        response.getOutputStream().println(json);
    }
}
