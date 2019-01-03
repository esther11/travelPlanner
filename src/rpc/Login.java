package rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** testing case 1
		JSONObject obj = new JSONObject();
		try {

			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			String password = input.getString("password");
			obj.put("result", "SUCCESS").put("user_id", userId).put("password", password);


		} catch (Exception e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonObject(response, obj);
        **/

		/**testing case 2
		JSONObject obj = new JSONObject();
		try {
			response.setStatus(401);
		    obj.put("result", "User Doesn't Exist");
		} catch (Exception e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonObject(response, obj);
		**/

		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			String password = input.getString("password");

			JSONObject obj = new JSONObject();
			if (connection.verifyLogin(userId, password)) {
				obj.put("result", "SUCCESS").put("user_id", userId).put("user name", connection.getFullname(userId));
			} else {
				response.setStatus(401);
				obj.put("result", "User Doesn't Exist");
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
}
