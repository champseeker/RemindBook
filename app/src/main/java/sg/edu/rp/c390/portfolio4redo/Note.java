package sg.edu.rp.c390.portfolio4redo;

import java.io.Serializable;

public class Note implements Serializable {

    private String name;
    private String description;
    private String date;
    private String importance;
    private String idKey;


    public Note(String name, String description, String date, String importance) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.importance = importance;


    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getImportance() {
        return importance;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }
}
