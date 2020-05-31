package sg.edu.rp.c390.portfolio4redo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    EditText etTitle, etDesc;
    Button btnUpdate, btnCancel;
    RadioGroup rgSetted;
    RadioButton rbImpEdit, rbNotImpEdit, rbSelected;

    Note dataEdit;
    int position;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        rgSetted = findViewById(R.id.rgImpEdit);
        rbImpEdit = findViewById(R.id.rbImpEdit);
        rbNotImpEdit = findViewById(R.id.rbNotImpEdit);

        Intent i = getIntent();
        dataEdit = (Note) i.getSerializableExtra("dataEdit");
        position = i.getIntExtra("positionEdit", 0);

        etTitle.setText(dataEdit.getName());
        etDesc.setText(dataEdit.getDescription());

        if(dataEdit.getImportance().equals("Important")){

            rbImpEdit.setChecked(true);

        }else if(dataEdit.getImportance().equals("Not Important")){

            rbNotImpEdit.setChecked(true);

        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cTitle = etTitle.getText().toString();
                String cDesc = etDesc.getText().toString();

                if (cTitle.equals("") || cDesc.equals("")){

                    Toast.makeText(EditActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();

                }else{

                    updateValue();
                    finish();

                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void updateValue(){

        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();
        String date = dataEdit.getDate();

        int btnEdit = rgSetted.getCheckedRadioButtonId();
        rbSelected = findViewById(btnEdit);
        String imp = rbSelected.getText().toString();

        Note updateNote = new Note(title, desc, date, imp);
        mDatabase = FirebaseDatabase.getInstance().getReference("notes");

        mDatabase.child(dataEdit.getIdKey()).setValue(updateNote);

    }
}