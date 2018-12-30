package rpc;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchPlace extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchPlace() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// doGet: search place list from Google Maps API, return it to FrontEnd
	// request: http request parameters
	// response: JSONArray with List<Place> included
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// allow access only if session exists
		// comment this feature for now in order to test search Servlet
		/*
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		*/
		// optional from session, if session exists we could get user_id from session instead of request body
		// String userId = session.getAttribute("user_id").toString(); 
		String userId = request.getParameter("user_id");
		String placeName = request.getParameter("placeName");
		String placeType = request.getParameter("placeType");
		
        DBConnection connection = DBConnectionFactory.getConnection();
        try {
        		List<Place> places = connection.searchPlaces(placeName, placeType);
        		List<String> favoritedPlaceIds = connection.getFavoritePlaceIds(userId);
        		
        		JSONArray array = new JSONArray();
        		for (Place place : places) {
        			JSONObject obj = place.toJSONObject();
        			// comment below, "favorite" is boolean in PlaceFavorite.java
    				// obj.put("favorite", favoritedPlaceIds.contains(place.getPlaceId()));
    				array.put(obj);
        		}
        		RpcHelper.writeJsonArray(response, array);
        		

        } catch (Exception e) {
        		e.printStackTrace();
        } finally {
        		connection.close();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
