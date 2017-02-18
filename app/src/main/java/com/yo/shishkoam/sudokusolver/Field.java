package com.yo.shishkoam.sudokusolver;

import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by User on 02.02.2017
 */

public class Field {
    Item[][] fieldItems = new Item[9][9];

    public Field() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                fieldItems[i][j] = new Item(i, j);
            }
        }
    }

    public void setNumber(int row, int column, int value) {
        if (value == 0) {
            fieldItems[row][column] = new Item(row, column);
        } else {
            fieldItems[row][column].setValue(value);
        }
    }

    public void save(SharedPreferences prefs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                builder.append(fieldItems[i][j].getValue());
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("field", builder.toString());
        editor.commit();
    }

    public String[] restore(String fieldString) {
        if (fieldString == null) {
            return new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " ",
                    " ", " ", " ", " ", " ", " ", " ", " ", " "};
        }
        String[] result = new String[81];
        char[] field = fieldString.toCharArray();
        for (int i = 0; i < 9; i++) {
            int currentRow = i * 9;
            for (int j = 0; j < 9; j++) {
                Item item = new Item(i, j);
                String ch = String.valueOf(field[currentRow + j]);
                item.setValue(Integer.valueOf(ch));
                fieldItems[i][j] = item;
                result[currentRow + j] = (!ch.equals("0")) ? ch : "";
            }
        }
        return result;
    }

    public String[] restore() {
        String[] result = new String[81];
        for (int i = 0; i < 9; i++) {
            int currentRow = i * 9;
            for (int j = 0; j < 9; j++) {
                String ch = String.valueOf(fieldItems[i][j].getValue());
                result[currentRow + j] = (!ch.equals("0")) ? ch : "";
            }
        }
        return result;
    }

    public String getData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                builder.append(fieldItems[i][j].getValue());
            }
        }
        return builder.toString();
    }

    public void simpleCheck() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = fieldItems[i][j].getValue();
                if (value == 0) {
                    setUpProposals(i, j);
                }
            }
        }
    }

    private boolean setUpProposals(int i, int j) {
        Item item = fieldItems[i][j];
        for (int k = 0; k < 9; k++) {
            int value = fieldItems[i][k].getValue();
            if (value != 0) {
                item.removeProposals(value);
            }
        }
        if (item.setValue()) {
            return item.setValue();
        }
        for (int k = 0; k < 9; k++) {
            int value = fieldItems[k][j].getValue();
            if (value != 0) {
                item.removeProposals(value);
            }
        }
        if (item.setValue()) {
            return item.setValue();
        }
        int squareI = i / 3 * 3;
        int squareJ = j / 3 * 3;
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                int value = fieldItems[squareI + k][squareJ + l].getValue();
                if (value != 0) {
                    item.removeProposals(value);
                }
            }
        }
        return item.setValue();
    }

    public void proposalUniqueCheck() {
        for (int k = 1; k <= 9; k++) {
            checkProposalUniqueInRow(k);
            checkProposalUniqueInColumn(k);
            checkProposalUniqueInSquares(k);
        }
    }

    private void checkProposalUniqueInRow(int k) {
        ArrayList<Item> haveProposals = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            haveProposals.clear();
            for (int j = 0; j < 9; j++) {
                Item item = fieldItems[i][j];
                if (i == 7 && j == 7) {
                    item = fieldItems[i][j];
                }
                if (item.getValue() == 0 && item.haveProposal(k)) {
                    haveProposals.add(item);
                    if (haveProposals.size() >= 2) {
                        break;
                    }
                }
            }
            if (haveProposals.size() == 1) {
                haveProposals.get(0).setValue(k);
            }
        }
    }

    private void checkProposalUniqueInColumn(int k) {
        ArrayList<Item> haveProposals = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            haveProposals.clear();
            for (int j = 0; j < 9; j++) {
                Item item = fieldItems[j][i];
                if (item.getValue() == 0 && item.haveProposal(k)) {
                    haveProposals.add(item);
                    if (haveProposals.size() >= 2) {
                        break;
                    }
                }
            }
            if (haveProposals.size() == 1) {
                haveProposals.get(0).setValue(k);
            }
        }
    }

    private void checkProposalUniqueInSquares(int k) {
        ArrayList<Item> haveProposals = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            j:
            for (int j = 0; j < 3; j++) {
                haveProposals.clear();
                int curI = i * 3;
                int curJ = j * 3;
                for (int l = 0; l < 3; l++) {
                    for (int m = 0; m < 3; m++) {
                        Item item = fieldItems[curI + l][curJ + m];
                        if (item.getValue() == 0 && item.haveProposal(k)) {
                            haveProposals.add(item);
                            if (haveProposals.size() >= 2) {
                                continue j;
                            }
                        }
                    }
                }
                if (haveProposals.size() == 1) {
                    haveProposals.get(0).setValue(k);
                }
            }
        }
    }
}
