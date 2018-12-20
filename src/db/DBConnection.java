package db;

import java.util.List;
import java.util.Set;

import entity.Place;

public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * Insert the favorite places for a user.
	 * 
	 * @param userId
	 * @param placeIds
	 */
	public void setFavoritePlaces(String userId, List<String> placeIds);

	/**
	 * Delete the favorite places for a user.
	 * 
	 * @param userId
	 * @param placeIds
	 */
	public void unsetFavoritePlaces(String userId, List<String> placeIds);
	
	/**
	 * Update the favorite places for a user.
	 * 
	 * @param userId
	 * @param placeIds
	 */
	public void updateFavoritePlaces(String userId, List<String> placeIds);

	/**
	 * Get the favorite place ids for a user.
	 * 
	 * @param userId
	 * @return placeIds
	 */
	public List<String> getFavoritePlaceIds(String userId);

	/**
	 * Get the favorite places for a user.
	 * 
	 * @param userId
	 * @return set of places
	 */
	public List<Place> getFavoritePlaces(String userId);

	/**
	 * Gets photos based on place id
	 * 
	 * @param placeId
	 * @return set of categories
	 */
	public Set<String> getPhotos(String placeId);
	
	/**
	 * Gets types based on place id
	 * 
	 * @param placeId
	 * @return set of types
	 */

	public Set<String> getTypes(String placeId);
	
	/**
	 * Update the order of this user-place primary key. (Important function in Order Update)
	 * If it exist, just update the order; if it doesn't exist, create the new key and add an order
	 * @param userId
	 * @param placeId
	 * @param order
	 * @return void
	 */

	public void updateOrder(String userId, String placeId, int order);
	
	/**
	 * (debug function) check if there's any duplicate order after order update 
	 * @param userId
	 * @param List of placeId that has been updated
	 * @param Update list of placeIds that has been updated
	 * @return String, the problem placeId, which never showed up in updated list but got
	 * 			the same order as one of the placeId in update list. If not found such 
	 *          problem id, return null
	 */
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String checkOrder(String userId, List<String> placeIds);
	
	/**
	 * Search items near a geolocation and a term (optional).
	 * 
	 * @param userId
	 * @param placeName
	 * 			(Nullable)
	 * @param placeType
	 *          (Nullable)
	 * @return list of items
	 */
	public List<Place> searchPlaces(String placeName, String placeType);

	/**
	 * Save item into db.
	 * 
	 * @param item
	 */
	public void savePlace(Place place);

	/**
	 * Get full name of a user. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getUsername(String userId);

	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean verifyLogin(String userId, String password);

}
