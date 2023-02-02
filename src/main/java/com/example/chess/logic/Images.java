package com.example.chess.logic;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.InputStream;

public class Images {

    public Image[] getPNGs(){
        Image[] images = new Image[12];
        images[0] = getImage("src/main/textures/ChessPiecesPNGs/wPawn.png");
        images[1] = getImage("src/main/textures/ChessPiecesPNGs/wRook.png");
        images[2] = getImage("src/main/textures/ChessPiecesPNGs/wKnight.png");
        images[3] = getImage("src/main/textures/ChessPiecesPNGs/wBishop.png");
        images[4] = getImage("src/main/textures/ChessPiecesPNGs/wQueen.png");
        images[5] = getImage("src/main/textures/ChessPiecesPNGs/wKing.png");
        images[6] = getImage("src/main/textures/ChessPiecesPNGs/bPawn.png");
        images[7] = getImage("src/main/textures/ChessPiecesPNGs/bRook.png");
        images[8] = getImage("src/main/textures/ChessPiecesPNGs/bKnight.png");
        images[9] = getImage("src/main/textures/ChessPiecesPNGs/bBishop.png");
        images[10] = getImage("src/main/textures/ChessPiecesPNGs/bQueen.png");
        images[11] = getImage("src/main/textures/ChessPiecesPNGs/bKing.png");
        return images;
    }
    private Image getImage(String url){
        try{
            InputStream stream = new FileInputStream(url);
            Image image = new Image(stream);
            return image;
        }catch (Exception e){
            System.out.println("No such URL");
            return null;
        }
    }
}
