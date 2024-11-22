package apps.project.servlet.autopay;

import apps.framework.object.CmMap;
import apps.project.controller.autopay.AutoPayController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class AutoPayServlet extends HttpServlet {

	private static final long serialVersionUID = 2643543543543543543L;

	private AutoPayController autoPayController = new AutoPayController();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// GET
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// POST
		JSONObject result = new JSONObject();

		try {
			CmMap reqVo = new CmMap();

			// TODO : 추후 해당 에이전트에서 요청을 받아 처리하는 정책으로 정해질시 추가해야 할 부분
			// ParameterMap을 CmMap으로 변환
			// req.getParameterMap().forEach((k, v) -> reqVo.put(k.toString(), v[0]));

			autoPayController.getAutoPayDataAfterInsert(reqVo);

			result.put("result", "success");

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

}
