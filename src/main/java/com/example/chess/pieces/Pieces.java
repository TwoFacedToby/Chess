package com.example.chess.pieces;

import java.util.ArrayList;

public class Pieces {

    private static ArrayList<Piece> pieces;

    public static ArrayList<Piece> getPieces(){
        if(pieces == null) initPieces();
        return pieces;
    }

    private static void initPieces(){
        pieces = new ArrayList<>();
        //White Pieces
        pieces.add(new Piece("Rook", false, 0, 7)); //White Rook Left
        pieces.add(new Piece("Knight", false, 1, 7)); //White Knight Left
        pieces.add(new Piece("Bishop", false, 2, 7)); //White Bishop Left
        pieces.add(new Piece("Queen", false, 3, 7)); //White Queen
        pieces.add(new Piece("King", false, 4, 7)); //White King
        pieces.add(new Piece("Bishop", false, 5, 7)); //White Bishop Right
        pieces.add(new Piece("Knight", false, 6, 7)); //White Knight Right
        pieces.add(new Piece("Rook", false, 7, 7)); //White Rook Right
        pieces.add(new Piece("Pawn", false, 0, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 1, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 2, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 3, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 4, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 5, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 6, 6)); //White Pawn
        pieces.add(new Piece("Pawn", false, 7, 6)); //White Pawn
        //Black Pieces
        pieces.add(new Piece("Rook", true, 0, 0)); //Black Rook Left
        pieces.add(new Piece("Knight", true, 1, 0)); //Black Knight Left
        pieces.add(new Piece("Bishop", true, 2, 0)); //Black Bishop Left
        pieces.add(new Piece("Queen", true, 3, 0)); //Black Queen
        pieces.add(new Piece("King", true, 4, 0)); //Black King
        pieces.add(new Piece("Bishop", true, 5, 0)); //Black Bishop Right
        pieces.add(new Piece("Knight", true, 6, 0)); //Black Knight Right
        pieces.add(new Piece("Rook", true, 7, 0)); //Black Rook Right
        pieces.add(new Piece("Pawn", true, 0, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 1, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 2, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 3, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 4, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 5, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 6, 1)); //Black Pawn
        pieces.add(new Piece("Pawn", true, 7, 1)); //Black Pawn
    }
}
