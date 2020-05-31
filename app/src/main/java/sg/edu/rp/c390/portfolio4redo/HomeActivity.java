package sg.edu.rp.c390.portfolio4redo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class HomeActivity extends AppCompatActivity {

    ListView lvNote;
    Button btnAdd;
    ArrayList<Note> alMain = new ArrayList<Note>();
    ArrayAdapter aaMain;
    Note updatedNote;

    DatabaseReference hDatabase;

    int nRequestCode = 123;
    int notificationID = 888;

    @Override
    protected void onStart() {
        super.onStart();

        hDatabase = FirebaseDatabase.getInstance().getReference().child("notes");

        hDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alMain.clear();

                for (DataSnapshot noteSnap: dataSnapshot.getChildren()){

                    String title = (String) noteSnap.child("name").getValue();
                    String desc = (String) noteSnap.child("description").getValue();
                    String date = (String) noteSnap.child("date").getValue();
                    String imp = (String) noteSnap.child("importance").getValue();

                    Note startNote = new Note(title, desc, date, imp);
                    startNote.setIdKey(noteSnap.getKey());
                    alMain.add(startNote);

                }

                aaMain = new NoteArrayAdapter(HomeActivity.this, R.layout.row, alMain);
                lvNote.setAdapter(aaMain);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        hDatabase = FirebaseDatabase.getInstance().getReference().child("notes");

        lvNote = findViewById(R.id.lvNote);
        btnAdd = findViewById(R.id.btnAdd);

        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long identity) {

                Note data = alMain.get(position);
                Intent i = new Intent(HomeActivity.this,
                        InfoActivity.class);
                i.putExtra("data", data);

                startActivityForResult(i, 1);

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this, AddActivity.class);
                startActivityForResult(i, 1);

            }
        });

        hDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String title = (String) dataSnapshot.child("name").getValue();
                String desc = (String) dataSnapshot.child("description").getValue();
                String date = (String) dataSnapshot.child("date").getValue();
                String imp = (String) dataSnapshot.child("importance").getValue();

                updatedNote = new Note(title, desc, date, imp);
                updatedNote.setIdKey(dataSnapshot.getKey());

                NotificationManager notiManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_HIGH);

                    channel.setDescription("This is for default notification");
                    notiManager.createNotificationChannel(channel);
                }

                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity
                        ( HomeActivity.this, nRequestCode, intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.BigTextStyle bigText = new
                        NotificationCompat.BigTextStyle();
                bigText.setBigContentTitle(updatedNote.getName() + " was updated");
                bigText.bigText(updatedNote.getDescription());
                bigText.setSummaryText(updatedNote.getName() + " was updated!");

                NotificationCompat.Builder builder = new
                        NotificationCompat.Builder(HomeActivity.this, "default");
                builder.setContentTitle(updatedNote.getName() + " was updated");
                builder.setContentText("Expand to see full detail");

                String updateImp = updatedNote.getImportance();

                if(updateImp.equals("Important")){
                    builder.setSmallIcon(android.R.drawable.btn_star_big_on);
                }else{
                    builder.setSmallIcon(android.R.drawable.btn_star_big_off);
                }

                builder.setContentIntent(pIntent);
                builder.setStyle(bigText);
                builder.setAutoCancel(true);

                Notification n = builder.build();
                notiManager.notify(notificationID, n);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1){

            aaMain.notifyDataSetChanged();

        }
    }

}