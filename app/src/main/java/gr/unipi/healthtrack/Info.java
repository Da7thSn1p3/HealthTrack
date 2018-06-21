package gr.unipi.healthtrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Info extends AppCompatActivity {

    ArrayList<String> change_List;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mList = findViewById(R.id.listview);

        change_List = new ArrayList<String>();

        ArrayAdapter list_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, change_List);
        mList.setAdapter(list_adapter);
        change_List.add("---------- Change List ----------");
        change_List.add("Release v1.0");
        change_List.add("First fully working release, fixed doctor/symptom bug.");
        change_List.add("Fixed color behind spinners bug.");
        change_List.add("Added ability to delete single symptoms.");
        change_List.add("Added ability to call patients by clicking their number.");
        change_List.add("---------------------------------");
        change_List.add("Release v1.1");
        change_List.add("Fixed a bug with patient names in Symptom List.");
        change_List.add("---------------------------------");
        change_List.add("Release v1.2");
        change_List.add("Made some visual changes, rounded corners");
        change_List.add("Also changed gravity for welcome name, to be centered");
        change_List.add("---------------------------------");
        change_List.add("Release v1.3");
        change_List.add("Added End Time for Symptoms");
        change_List.add("Added ability to delete Time and End Time of Symptoms");
        change_List.add("Added ability to edit Time and End Time of Symptoms");
        change_List.add("---------------------------------");
    }
}
