package com.yo.shishkoam.sudokusolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by User on 02.02.2017
 */

public class Item {
    private int row = 0;
    private int column = 0;
    private boolean isFinal = false;
    private int value = 0;
    private Set<Integer> proposals = new HashSet<>();

    public Item(int row, int column) {
        this.row = row;
        this.column = column;
        for (int i = 0; i < 9; i++) {
            proposals.add(i + 1);
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Set<Integer> getProposals() {
        return proposals;
    }

    public void setProposals(Set proposals) {
        this.proposals = proposals;
    }

    public void removeProposals(int proposal) {
        if (proposals.contains(proposal)) {
            proposals.remove(proposal);
        }
    }

    public boolean haveProposal(int proposal) {
        return proposals.contains(proposal);
    }

    public boolean setValue() {
        if (proposals.size() == 1) {
            for (Integer integer: proposals){
                value = integer;
            }
            proposals.clear();
            return true;
        }
        return false;
    }
}
