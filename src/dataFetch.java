/**
 * Created by vvu on 23/11/13.
 */

import com.facebook.model.GraphUser;

import org.json.JSONException;
import org.json.JSONObject;

public class dataFetch {
	private GraphUser user;
	private String id;
	private String lName;
	private String fName;
	private String imgurl;
	private String email;
	private String username;
	private String JSON_data;

	void dataFetch (GraphUser user) {
		this.user = user;
	}

	public String getData () throws JSONException {
		JSONObject obj = new JSONObject();

		this.id = this.user.getId();
		this.lName = this.user.getLastName();
		this.fName = this.user.getFirstName();
		this.imgurl = "http://graph.facebook.com/" + this.id + "/picture?type=large";
		this.email = user.getInnerJSONObject().getString("email");
		this.username = this.user.getUsername();

		obj.put("fb_id", this.id);
		obj.put("fname", this.fName);
		obj.put("lname", this.lName);
		obj.put("imageurl", this.imgurl);
		obj.put("email", this.email);
		obj.put("username", this.username);

		this.JSON_data = obj.toString();

		return this.JSON_data;
	}
}
