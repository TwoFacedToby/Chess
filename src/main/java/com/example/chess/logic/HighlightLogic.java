package com.example.chess.logic;

import com.example.chess.pieces.Piece;
import com.example.chess.pieces.Pieces;

import java.util.ArrayList;

public class HighlightLogic {
    private Piece enPassant = null;
    private boolean running = true;
    ArrayList<Boolean> isLit;
    ArrayList<Piece> pieces = Pieces.getPieces();
    Piece current = null;
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
    private void resetBoard(){
        for(int i = 0; i < pieces.size(); i++){
            pieces.get(i).setChosen(false);
            pieces.get(i).setMarked(false);
        }
        for(int i = 0; i < isLit.size(); i++){
            isLit.set(i, false);
        }
    }
    public void choosePiece(Piece newPiece){
        if(running) {
            if (current == null) { //mark new piece
                if (newPiece.getType().equals("Empty")) {
                    current = null;
                } else {
                    if (newPiece.getColor() == Pieces.getInstance().isTurn()) {
                        resetBoard();
                        newPiece.setChosen(true);
                        highlightFor(newPiece);
                        current = newPiece;
                    } else current = null;
                }
            } else { //try to move
                if (newPiece.getType().equals("Empty")) {
                    if (isLit.get(newPiece.getPosition()[0] + newPiece.getPosition()[1] * 8)) {
                        //Move to spot
                        moveTo(newPiece);
                    } else resetBoard();
                    //Else un-mark all pieces.
                    current = null;
                } else {
                    if (newPiece.getMarked()) {
                        //Move to spot
                        moveTo(newPiece);
                        //Remove Other Piece
                        newPiece.setAlive(false);
                        if (newPiece.getType().equals("King")) {
                            running = false;
                            LogicController.get().getViewController().winner(!newPiece.getColor());
                        }
                        LogicController.get().getViewController().killPiece(newPiece);
                        //Moves them away from the board, because if I remove it from the list or make it null it will cause problems.
                        int[] pos = new int[2];
                        pos[0] = -1;
                        pos[1] = -1;
                        newPiece.setPosition(pos);
                    } else if (newPiece.getColor() == Pieces.getInstance().isTurn()) {
                        current = null;
                        choosePiece(newPiece);
                    } else {
                        current = null;
                        resetBoard();
                    }
                    //Else un-mark all pieces.
                }
            }
        }
    }
    private void moveTo(Piece newPiece){
        //Move to spot
        int[] moveTo = new int[2];
        moveTo[0] = newPiece.getPosition()[0];
        moveTo[1] = newPiece.getPosition()[1];
        if(enPassant != null){
            System.out.println("EnPassant is not null");
            if(moveTo[0] == enPassant.getPosition()[0] && moveTo[1] == enPassant.getPosition()[1]){
                System.out.println("Equals position");
                if(current.getType().equals("Pawn")){
                    if(current.getPosition()[1] == newPiece.getPosition()[1]){
                        if(current.getColor()){ //Black
                            moveTo[1] += 1;
                        }
                        else{ //White
                            moveTo[1] -=1;
                        }
                    }
                }
            }
        }
        if(current.getType().equals("Pawn")){
            if(current.getColor()) //Black
            {
                if(current.getPosition()[1] == 1 && newPiece.getPosition()[1] == 3){
                    enPassant = current;
                }
                else enPassant = null;
            }
            else //White
            {
                if(current.getPosition()[1] == 6 && newPiece.getPosition()[1] == 4){
                    enPassant = current;
                }
                else enPassant = null;
            }
        }
        else enPassant = null;



        LogicController.get().getViewController().movePiece(current, moveTo);
        current.setPosition(moveTo);
        Pieces.getInstance().setTurn(!Pieces.getInstance().isTurn());
        LogicController.get().getViewController().highlightTurn();
        resetBoard();
    }
    private boolean isAvailable(int[] pos, boolean color){
        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).getPosition()[0] == pos[0]){
                if(pieces.get(i).getPosition()[1] == pos[1]){
                    if(pieces.get(i).getColor() == color){
                        return false;
                    }
                    else {
                        pieces.get(i).setMarked(true);
                        return false;
                    }
                }
            }
        }
        isLit.set(pos[0] + pos[1]*8, true);
        return true;
    }
    private boolean pawnStraightAvailable(int[] pos, boolean color){
        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).getPosition()[0] == pos[0]){
                if(pieces.get(i).getPosition()[1] == pos[1]){
                    if(pieces.get(i).getType().equals("Empty")){
                        isLit.set(pos[0] + pos[1]*8, true);
                        return true;
                    }
                    else if(pieces.get(i).getColor() == color){
                        return false;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        isLit.set(pos[0] + pos[1]*8, true);
        return true;
    }
    private boolean pawnDiagonalEnemyAvailable(int[] pos, boolean color){
        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).getPosition()[0] == pos[0]){
                if(pieces.get(i).getPosition()[1] == pos[1]){
                    if(pieces.get(i).getType().equals("Empty")){
                        return false;
                    }
                    else if(pieces.get(i).getColor() != color){
                        pieces.get(i).setMarked(true);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    private boolean enPassantAvailable(int[] pos, boolean color){
        if(enPassant != null){
            for(int i = 0; i < pieces.size(); i++){
                if(pieces.get(i).getPosition()[0] == pos[0]){
                    if(color){ //Black
                        if(pieces.get(i).getPosition()[1] == pos[1]-1){
                            return false;
                        }
                    }
                    else{ //White
                        if(pieces.get(i).getPosition()[1] == pos[1]+1){
                            return false;
                        }
                    }
                }
            }
            //Diagonal is empty: returns true if enemy is at spot
            for(int i = 0; i < pieces.size(); i++){
                if(enPassant.getPosition()[0] == pos[0] && pos[0] == pieces.get(i).getPosition()[0]){
                    if(enPassant.getPosition()[1] == pos[1] && pos[1] == pieces.get(i).getPosition()[1]){
                        if(pieces.get(i).getType().equals("Empty")){
                            return false;
                        }
                        else if(pieces.get(i).getColor() != color){
                        /*if(color){
                            isLit.set(pos[0] + (pos[1]+1)*8, true);
                        }
                        else{
                            isLit.set(pos[0] + (pos[1]-1)*8, true);
                        }*/

                            pieces.get(i).setMarked(true);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
            return false;
        }
        else return false;
    }

    public void highlightFor(Piece piece){
        switch (piece.getType()){
            case "Pawn":
                highlightForPawn(piece);
                break;
            case "Knight":
                highlightForKnight(piece);
                break;
            case "Rook":
                highlightForRook(piece);
                break;
            case "Bishop":
                highlightForBishop(piece);
                break;
            case "Queen":
                highlightForRook(piece);
                highlightForBishop(piece);
                break;
            case "King":
                highlightForKing(piece);
                break;
        }
    }
    private void highlightForPawn(Piece originalPiece){
        int[] checkForPos;
        checkForPos = originalPiece.getPosition();
        if(originalPiece.getColor()){//Black
            //Down
            if(checkForPos[1] < 7) checkForPos[1] += 1;
            if(pawnStraightAvailable(checkForPos, originalPiece.getColor())){
                if(originalPiece.getPosition()[1] == 1){
                    checkForPos[1] += 1;
                    pawnStraightAvailable(checkForPos, originalPiece.getColor());
                }
            }
            checkForPos = originalPiece.getPosition();
            //RightDown
            if(checkForPos[0] < 7 && checkForPos[1] < 7){
                checkForPos[0] += 1;
                checkForPos[1] += 1;
                pawnDiagonalEnemyAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos = originalPiece.getPosition();
            //LeftDown
            if(checkForPos[0] > 0 && checkForPos[1] < 7) {
                checkForPos[0] -= 1;
                checkForPos[1] += 1;
                pawnDiagonalEnemyAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos = originalPiece.getPosition();
            //Left en Passant
            if(checkForPos[0] > 0) checkForPos[0] -= 1;
            enPassantAvailable(checkForPos, originalPiece.getColor());
            checkForPos = originalPiece.getPosition();
            //Right en Passant
            if(checkForPos[0] < 7) checkForPos[0] += 1;
            enPassantAvailable(checkForPos, originalPiece.getColor());
        }else{//White
            //Up
            if(checkForPos[1] > 0) checkForPos[1] -= 1;
            if(pawnStraightAvailable(checkForPos, originalPiece.getColor())){
                if(originalPiece.getPosition()[1] == 6){
                    checkForPos[1] -= 1;
                    pawnStraightAvailable(checkForPos, originalPiece.getColor());
                }
            }
            checkForPos = originalPiece.getPosition();
            //LeftUp
            if(checkForPos[0] > 0 && checkForPos[1] > 0){
                checkForPos[0] -= 1;
                checkForPos[1] -= 1;
                pawnDiagonalEnemyAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos = originalPiece.getPosition();
            //RightUp
            if(checkForPos[0] < 7 && checkForPos[1] > 0){
                checkForPos[0] += 1;
                checkForPos[1] -= 1;
                pawnDiagonalEnemyAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos = originalPiece.getPosition();
            //Left en Passant
            if(checkForPos[0] > 0) checkForPos[0] -= 1;
            enPassantAvailable(checkForPos, originalPiece.getColor());
            checkForPos = originalPiece.getPosition();
            //Right en Passant
            if(checkForPos[0] < 7) checkForPos[0] += 1;
            enPassantAvailable(checkForPos, originalPiece.getColor());
        }



    }
    private void highlightForRook(Piece originalPiece){
        int[] checkForPos;
        boolean checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //Left
            if(checkForPos[0] > 0) checkForPos[0] -= 1;
            else checkForMove = false;
            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
        checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //Right
            if(checkForPos[0] < 7) checkForPos[0] += 1;
            else checkForMove = false;
            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
        checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //Up
            if(checkForPos[1] > 0) checkForPos[1] -= 1;
            else checkForMove = false;

            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
        checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //Down
            if(checkForPos[1] < 7) checkForPos[1] += 1;
            else checkForMove = false;
            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
    }
    private void highlightForBishop(Piece originalPiece){
        int[] checkForPos;
        boolean checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //LeftUp
            if(checkForPos[0] > 0 && checkForPos[1] > 0){
                checkForPos[0] -= 1;
                checkForPos[1] -= 1;
            }
            else checkForMove = false;
            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
        checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //RightDown
            if(checkForPos[0] < 7 && checkForPos[1] < 7){
                checkForPos[0] += 1;
                checkForPos[1] += 1;
            }
            else checkForMove = false;
            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
        checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //LeftDown
            if(checkForPos[0] > 0 && checkForPos[1] < 7) {
                checkForPos[0] -= 1;
                checkForPos[1] += 1;
            }
            else checkForMove = false;

            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
        checkForMove = true;
        checkForPos = originalPiece.getPosition();
        while(checkForMove){ //RightUp
            if(checkForPos[0] < 7 && checkForPos[1] > 0){
                checkForPos[0] += 1;
                checkForPos[1] -= 1;
            }
            else checkForMove = false;
            if(!isAvailable(checkForPos, originalPiece.getColor())) checkForMove = false;
        }
    }
    private void highlightForKing(Piece originalPiece){
        int[] checkForPos;
        checkForPos = originalPiece.getPosition();
        //Left
        if(checkForPos[0] > 0) checkForPos[0] -= 1;
        isAvailable(checkForPos, originalPiece.getColor());
        checkForPos = originalPiece.getPosition();
        //Right
        if(checkForPos[0] < 7) checkForPos[0] += 1;
        isAvailable(checkForPos, originalPiece.getColor());
        checkForPos = originalPiece.getPosition();
        //Up
        if(checkForPos[1] > 0) checkForPos[1] -= 1;
        isAvailable(checkForPos, originalPiece.getColor());
        checkForPos = originalPiece.getPosition();
        //Down
        if(checkForPos[1] < 7) checkForPos[1] += 1;
        isAvailable(checkForPos, originalPiece.getColor());
        checkForPos = originalPiece.getPosition();
        //LeftUp
        if(checkForPos[0] > 0 && checkForPos[1] > 0){
            checkForPos[0] -= 1;
            checkForPos[1] -= 1;
            isAvailable(checkForPos, originalPiece.getColor());
        }
        checkForPos = originalPiece.getPosition();
        //RightDown
        if(checkForPos[0] < 7 && checkForPos[1] < 7){
            checkForPos[0] += 1;
            checkForPos[1] += 1;
            isAvailable(checkForPos, originalPiece.getColor());
        }
        checkForPos = originalPiece.getPosition();
        //LeftDown
        if(checkForPos[0] > 0 && checkForPos[1] < 7) {
            checkForPos[0] -= 1;
            checkForPos[1] += 1;
            isAvailable(checkForPos, originalPiece.getColor());
        }
        checkForPos = originalPiece.getPosition();
        //RightUp
        if(checkForPos[0] < 7 && checkForPos[1] > 0){
            checkForPos[0] += 1;
            checkForPos[1] -= 1;
            isAvailable(checkForPos, originalPiece.getColor());
        }
    }
    private void highlightForKnight(Piece originalPiece){
        int[] checkForPos;
        checkForPos = originalPiece.getPosition();


        if(checkForPos[0] > 1) //Two Left, One up&down
        {
            checkForPos[0] -= 2;
            if(checkForPos[1] > 0) //Check above
            {
                checkForPos[1] -=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos[1] = originalPiece.getPosition()[1];
            if(checkForPos[1] < 7) //Check above
            {
                checkForPos[1] +=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
        }
        checkForPos = originalPiece.getPosition();
        if(checkForPos[0] < 6) //Two Right, One up&down
        {
            checkForPos[0] += 2;
            if(checkForPos[1] > 0) //Check above
            {
                checkForPos[1] -=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos[1] = originalPiece.getPosition()[1];
            if(checkForPos[1] < 7) //Check above
            {
                checkForPos[1] +=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
        }
        checkForPos = originalPiece.getPosition();
        if(checkForPos[1] > 1) //Two Up, One left&right
        {
            checkForPos[1] -= 2;
            if(checkForPos[0] > 0) //Check left
            {
                checkForPos[0] -=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos[0] = originalPiece.getPosition()[0];
            if(checkForPos[0] < 7) //Check right
            {
                checkForPos[0] +=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
        }
        checkForPos = originalPiece.getPosition();
        if(checkForPos[1] < 6) //Two Down, One left&right
        {
            checkForPos[1] += 2;
            if(checkForPos[0] > 0) //Check left
            {
                checkForPos[0] -=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
            checkForPos[0] = originalPiece.getPosition()[0];
            if(checkForPos[0] < 7) //Check right
            {
                checkForPos[0] +=1;
                isAvailable(checkForPos, originalPiece.getColor());
            }
        }
    }
}
