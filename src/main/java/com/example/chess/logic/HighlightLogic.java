package com.example.chess.logic;

import java.util.ArrayList;

public class HighlightLogic {
    ArrayList<Boolean> isLit;

    public boolean isHighlighted(int i){
        if(isLit == null) init();
        return isLit.get(i);
    }
    private void init(){ //Initiates
        isLit = new ArrayList<Boolean>();
        for(int i = 0; i < 64; i++){
            isLit.add(false);
        }
    }
}
