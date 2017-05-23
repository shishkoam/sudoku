package com.yo.shishkoam.sudokusolver;

/**
 * Created by User on 16.02.2017
 */
public class FieldManager {
    private static FieldManager ourInstance = new FieldManager();

    public static FieldManager getInstance() {
        return ourInstance;
    }

    private Field currentField;
    private FieldManager() {
    }

    public Field getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }
}
