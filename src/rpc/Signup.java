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
 * Servlet implementation class Signup
 */
@WebServlet("/signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Signup() {
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}


	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** testing case 1
		JSONObject obj = new JSONObject();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			obj.put("result", "SUCCESS").put("email", userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonObject(response, obj);
		**/

		/** testing case 2
		 SONObject obj = new JSONObject();
		try {
			response.setStatus(401);
		    obj.put("result", "Email Already Exists");
		} catch (Exception e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonObject(response, obj);
		*/

		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			String name = input.getString("name");
			String password = input.getString("password");

			JSONObject obj = new JSONObject();
			if (connection.verifyUserId(userId)) {
				connection.addUser(userId, password, name);
				obj.put("result", "SUCCESS").put("email", userId).put("name", name);
			} else {
				response.setStatus(401);
				obj.put("result", "Email Already Exists");
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
