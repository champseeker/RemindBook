package sg.edu.rp.c390.portfolio4redo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    TextView tvTitleShow, tvDescShow;
    Button btnEditShow, btnDeleteShow, btnBackShow;

    Note dataInfo;
    int positionInfo;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info);
        getSupportActionBar().hide();

        tvTitleShow = findViewById(R.id.tvTitleShow);
        tvDescShow = findViewById(R.id.tvDescShow);
        btnEditShow = findViewById(R.id.btnEditShow);
        btnDeleteShow = findViewById(R.id.btnDelete);
        btnBackShow = findViewById(R.id.btnBackShow);

        Intent i = getIntent();
        dataInfo = (Note) i.getSerializableExtra("data");
        positionInfo = i.getIntExtra("position", 0);

        tvTitleShow.setText(dataInfo.getName());
        tvDescShow.setText(dataInfo.getDescription());

        btnEditShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(InfoActivity.this, EditActivity.class);
                i.putExtra("dataEdit", dataInfo);
                i.putExtra("positionEdit", positionInfo);
                startActivity(i);

            }
        });

        btnDeleteShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteValue();
                setResult(RESULT_OK);
                finish();
            }
        });

        btnBackShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("notes");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String title = (String) dataSnapshot.child("name").getValue();
                String desc = (String) dataSnapshot.child("description").getValue();
                String date = (String) dataSnapshot.child("date").getValue();
                String imp = (String) dataSnapshot.child("importance").getValue();

                Note newObj = new Note(title, desc, date, imp);
                newObj.setIdKey(dataSnapshot.getKey());
                dataInfo = newObj;
                tvTitleShow.setText(dataInfo.getName());
                tvDescShow.setText(dataInfo.getDescription());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void deleteValue(){

        mDatabase = FirebaseDatabase.getInstance().getReference("notes");

        mDatabase.child(dataInfo.getIdKey()).removeValue();

    }

}