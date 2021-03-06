package svenerik.com.frietappandroid.models;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import svenerik.com.frietappandroid.GroupActivity;
import svenerik.com.frietappandroid.GroupFragment;

/**
 * Created by Erik on 22-4-2015.
 */
public class Group extends AsyncTask<String, String, ResObject> {
    public String _id;
    public String creator;
    public String name;
    public String[] orders;
    public String[] users;
    public String user;
    private GroupFragment groupFragment;

    public Group(String _id, String creator, String name, String[] orders, String[] users, String user){
        this._id = _id;
        this.creator = creator;
        this.name = name;
        this.orders = orders;
        this.users = users;
        this.user = user;
    }

    public void setGroupFragment(GroupFragment groupFragment){
        this.groupFragment = groupFragment;
    }

    public void getSessions(){
        Group newGroup = new Group(this._id, this.creator, this.name, this.orders, this.users, this.user);
        newGroup.setGroupFragment(this.groupFragment);
        newGroup.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResObject doInBackground(String... args) {
        JSONParser jParser = JSONParser.getInstance();
        // Getting JSON from URL
        SharedPreferences settings = this.groupFragment.getActivity().getSharedPreferences("frietapp", 0);
        boolean sortByActive = settings.getBoolean("sortByActive", true);
        String url = "";
        if(sortByActive){
            url = "https://desolate-bayou-9128.herokuapp.com/groups/" + this._id + "/orders?orderBy=active";
        } else {
            url = "https://desolate-bayou-9128.herokuapp.com/groups/" + this._id + "/orders";
        }
        ResObject res = jParser.getJSONFromUrl(url, this.user, true);
        res.setGroup_id(this._id);
        return res;
    }
    @Override
    protected void onPostExecute(ResObject res) {
        // Getting JSON Array
        if(res.hasJSON()){
            if(res.json == null){
                groupFragment.success(res.jsonArr.toString(), res.getGroup_id());
            } else {
                groupFragment.success(res.json.toString(), res.getGroup_id());
            }
        }  else {
            groupFragment.fail();
        }
    }
}
