package com.example.chess.pieces;

public class Piece {
    private String type;
    private boolean color;
    private int x;
    private int y;
    private boolean alive;

    public Piece(String type, boolean color, int x, int y){
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public boolean getColor(){
        return color;
    }
    public void setColor(boolean color){
        this.color = color;
    }
    public boolean getAlive(){
        return alive;
    }
    public void setAlive(boolean alive){
        this.alive = alive;
    }
    public int[] getPosition(){
        int[] pos = new int[2];
        pos[0] = x;
        pos[1] = y;
        return pos;
    }
    public void setPosition(int[] position){
        x = position[0];
        y = position[1];
    }
}
