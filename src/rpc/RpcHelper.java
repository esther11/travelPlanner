package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;


public class RpcHelper {
		
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException{
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			PrintWriter output = response.getWriter();
			output.print(array);
			output.close();
	}
	
	public static void writeJsonObject(HttpServletResponse response, JSONObject object) throws IOException{
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			PrintWriter output = response.getWriter();
			output.print(object);
			output.close();
	}
	
	public static JSONObject readJsonObject(HttpServletRequest request) throws IOException{
			StringBuilder sb = new StringBuilder();
			try {
					BufferedReader reader = request.getReader();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}	
					return new JSONObject(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new JSONObject();
	}
}
