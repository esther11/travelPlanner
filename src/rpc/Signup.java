package rpc;

import java.io.IOException;

import org.json.JSONObject;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar.String;

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
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String email = input.getString("email");
			String name = input.getString("name");
			String password = input.getString("password");
			
			JSONObject obj = new JSONObject();
			JSONObject user = new JSONObject();
			if (connection.verifyUserId(email)) {
				// add to DB
				user.put("user_id", email).put("username", name).put("password", password);
				connection.addUser(user);
				obj.put("result", "SUCCESS").put("user_id", email).put("name", name);
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
