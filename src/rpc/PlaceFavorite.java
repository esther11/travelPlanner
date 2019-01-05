package rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;

/**
 * Servlet implementation class PlaceFavorite
 */
@WebServlet("/favorite")
public class PlaceFavorite extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlaceFavorite() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// doGet: Get favorite list from db, return it to FrontEnd
	// request: JSONObject with userId included
	// response: JSONArray with List<placeId> included
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String userId = request.getParameter("user_id");
		JSONArray array = new JSONArray();

		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			List<Place> places;
			places = conn.getFavoritePlaces(userId);
			for (Place place : places) {
				JSONObject obj = place.toJSONObject();
				obj.append("favorite", true);
				array.put(obj);
			}

			RpcHelper.writeJsonArray(response, array);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// doPost: update all Orders of places that this User wants to go in DB, return
	// success message
	// request: JSONObject including k-v pair: userId, and JSONArray: list of
	// favorite places
	// response: success message
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			String city = input.getString("city");
			JSONArray array = input.getJSONArray("favorite");
			List<String> placeIds = new ArrayList<>();
			for (int i = 0; i < array.length(); ++i) {
				placeIds.add(array.getString(i));
			}
			connection.updateFavoritePlaces(userId, placeIds, city);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS for Updating"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	// doPut: add more places into favorite list, return success message
	// request: JSONObject including k-v pair: userId, and JSONArray: new favorite
	// places
	// response: success message
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			String city = input.getString("city");
			JSONArray array = input.getJSONArray("favorite");
			List<String> placeIds = new ArrayList<>();
			for (int i = 0; i < array.length(); ++i) {
				placeIds.add(array.getString(i));
			}
			connection.setFavoritePlaces(userId, placeIds, city);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS for Adding into favorite list"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	// doDelete: remove specific places from favorite in db, return success message
	// request: JSONObject including k-v pair: userId, and JSONArray: old places
	// that needed to be deleted
	// response: success message
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			JSONArray array = input.getJSONArray("favorite");
			List<String> placeIds = new ArrayList<>();
			for (int i = 0; i < array.length(); ++i) {
				placeIds.add(array.getString(i));
			}
			connection.unsetFavoritePlaces(userId, placeIds);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS for Deleting"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
