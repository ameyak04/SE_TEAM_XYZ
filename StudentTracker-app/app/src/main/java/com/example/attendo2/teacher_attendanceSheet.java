package com.example.attendo2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class teacher_attendanceSheet extends AppCompatActivity {
    ListView listView;
    String teacher_id,class_selected;


    EditText date;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Studentlist = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date;
    android.widget.Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance_sheet);

        listView = (ListView) findViewById(R.id.list);
        date = (EditText) findViewById(R.id.date);
       // mToolbar=(android.widget.Toolbar)findViewById(R.id.ftoolbar);
      //  setSupportActionBar(mToolbar);
       // getSupportActionBar().setTitle("Previous Record");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle1 = getIntent().getExtras();
        class_selected = bundle1.getString("class_selected");
        teacher_id = bundle1.getString("tid");

    }

    private void setSupportActionBar(Toolbar mToolbar) {
    }

    public void viewlist(View v) {

        Userlist.clear();
        dbStudent = ref.child("Student");
        dbStudent.orderByChild("classes").equalTo(class_selected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(dsp.child("sid").getValue().toString());
                }
                display_list(Userlist);
              //  Toast.makeText(teacher_attendanceSheet.this, Userlist.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void display_list(final ArrayList Userlist) {

       // Toast.makeText(teacher_attendanceSheet.this, Userlist.toString(), Toast.LENGTH_SHORT).show();

        Studentlist.clear();
        required_date = date.getText().toString();
        dbAttendance = ref.child("attendance");
        Studentlist.add("      SID       "+"Status" + "   period");
        for (Object sid : Userlist) {
            dbAttendance.child(required_date).child(class_selected).child(teacher_id).child(sid.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                        String p1 = dsp.getValue().toString();
                       // Toast.makeText(teacher_attendanceSheet.this, p1.toString(), Toast.LENGTH_SHORT).show();
                        if((p1.equals("A"))||(p1.equals("P"))){
                            Studentlist.add(dataSnapshot.getKey().toString() + "            " + p1 +"        "+dsp.getKey());
                        }
                    }
                    list(Studentlist);

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                }

            });


        }

    }
    public void list(ArrayList studentlist){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);
        listView.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
