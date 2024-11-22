package apps.project.servlet;

import apps.framework.object.CmMap;
import apps.project.controller.scraping.ScripingController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class ScrapingHometaxServlet extends HttpServlet {

	private static final long serialVersionUID = 2643543543543543543L;

	ScripingController scripingController = new ScripingController();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// GET
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// POST
		JSONObject result = new JSONObject();
		CmMap reqVo = new CmMap();

		try {
			scripingController.hometaxResult(req, reqVo);
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("utf-8");
		
			result.put("r_code", reqVo.getString("r_code"));
			result.put("r_msg", reqVo.getString("r_msg"));
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try {
				result.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (JSONException e1) {
			}
		} finally {
			resp.getWriter().print(result.toString());
		}
	}

}
