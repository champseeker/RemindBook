package sg.edu.rp.c390.portfolio4redo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.Duration;

public class AddActivity extends AppCompatActivity {

    EditText etAddTitle, etAddContent;
    RadioGroup rgAddImp;
    RadioButton rbSelected;
    Button btnAdd, btnCancelAdd;

    DatabaseReference mDatabase;

    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);
        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance().getReference("notes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot notes : dataSnapshot.getChildren()) {
                    id = Integer.parseInt(notes.getKey());
                }
                id = id + 1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        etAddTitle = findViewById(R.id.etNewTitle);
        etAddContent = findViewById(R.id.etNewDesc);
        rgAddImp = findViewById(R.id.rgImp);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancelAdd = findViewById(R.id.btnCancelAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cTitle = etAddTitle.getText().toString();
                String cDesc = etAddContent.getText().toString();

                if(cTitle.equals("") || cDesc.equals("")){

                    Toast.makeText(AddActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();

                }else{

                    addNewNote();
                    setResult(RESULT_OK);
                    finish();

                }
            }
        });

        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    protected String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void addNewNote() {

        String title = etAddTitle.getText().toString();
        String desc = etAddContent.getText().toString();
        String date = getDate();

        int btnAdd = rgAddImp.getCheckedRadioButtonId();
        rbSelected = findViewById(btnAdd);
        String imp = rbSelected.getText().toString();

        Note newNotea = new Note(title, desc, date, imp);

        mDatabase.child(String.valueOf(id)).setValue(newNotea);

    }

}