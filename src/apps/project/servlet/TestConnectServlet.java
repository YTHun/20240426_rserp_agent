package apps.project.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class TestConnectServlet extends HttpServlet {

	private static final long serialVersionUID = 2759495955400853386L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject result = new JSONObject();

		try {
			String testParam = req.getParameter("testParam");

			result.put("testParam", testParam);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("utf-8");

		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try {
				result.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (JSONException ex) {
				throw new RuntimeException(ex);
			}

			throw new RuntimeException(e);
		} finally {
			resp.getWriter().print(result.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

}
