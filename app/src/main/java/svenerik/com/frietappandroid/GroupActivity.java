package svenerik.com.frietappandroid;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import svenerik.com.frietappandroid.models.Group;


public class GroupActivity extends ActionBarActivity implements GroupFragment.OnItemSelectedListener {

    private Group[] groups;
    private String[] orders;
    private String[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String groupsString = getIntent().getStringExtra("groups");
        JSONArray groupsJSON;
        try {
            groupsJSON = new JSONObject(groupsString).getJSONArray("groups");
            groups = new Group[groupsJSON.length()];
            for (int i = 0; i < groupsJSON.length(); i++) {
                JSONObject mJsonObject = groupsJSON.getJSONObject(i);
                orders = new String[mJsonObject.getJSONArray("orders").length()];
                for(int x = 0; x < mJsonObject.getJSONArray("orders").length(); x++){
                     orders[x] = mJsonObject.getJSONArray("orders").getString(x);
                }
                users = new String[mJsonObject.getJSONArray("users").length()];
                for(int y = 0; y < mJsonObject.getJSONArray("users").length(); y++){
                    users[y] = mJsonObject.getJSONArray("users").getString(y);
                }
                String _id = mJsonObject.getString("_id");
                String creator = mJsonObject.getString("creator");
                String name = mJsonObject.getString("name");
                Group newGroup = new Group(_id, creator, name, orders, users);
                groups[i] = newGroup;
            }
            for(int iets = 0; iets < groups.length; iets++){
                Log.i("Result: ", groups[iets].creator);
                Log.i("Result: ", groups[iets].name);
                Log.i("Result: ", groups[iets]._id);
                Log.i("", "");
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        createTableView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    public void createTableView(){
        GroupFragment groupFrag = (GroupFragment) getSupportFragmentManager().findFragmentById(R.id.groupFragment);
        groupFrag.createTableView(groups);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onItemSelected(String titel) {
        OrderFragment fragment = (OrderFragment) getFragmentManager().findFragmentById(R.id.orderFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(titel);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
            intent.putExtra(OrderActivity.EXTRA_TITEL, titel);
            startActivity(intent);
        }
    }
}
