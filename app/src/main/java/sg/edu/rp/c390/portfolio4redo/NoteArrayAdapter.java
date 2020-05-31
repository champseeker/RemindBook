package sg.edu.rp.c390.portfolio4redo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteArrayAdapter extends ArrayAdapter<Note> {
    Context context;
    ArrayList<Note> notes;
    int resource;
    TextView tvTitle, tvDate;
    ImageView ivImp;

    public NoteArrayAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        this.notes = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(resource, parent, false);

        tvTitle = rowView.findViewById(R.id.tvNoteName);
        tvDate = rowView.findViewById(R.id.tvDateAdded);
        ivImp = rowView.findViewById(R.id.ivImp);

        Note note = notes.get(position);

        tvTitle.setText(note.getName());
        tvDate.setText(String.valueOf(note.getDate()));

        if (note.getImportance().equals("Important")){

            ivImp.setImageResource(android.R.drawable.btn_star_big_on);

        }else if (note.getImportance().equals("Not Important")){

            ivImp.setImageResource(android.R.drawable.btn_star_big_off);

        }

        return rowView;
    }

}
