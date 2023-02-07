package com.example.chess;

import com.example.chess.logic.Images;
import com.example.chess.logic.LogicController;
import com.example.chess.pieces.Piece;
import com.example.chess.pieces.Pieces;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Random;

public class ViewController {
    @FXML
    private GridPane grid;
    @FXML
    private HBox dead1;
    @FXML
    private HBox dead2;
    @FXML
    private Button b1;
    @FXML
    private Button b2;

    ArrayList<StackPane> fields = new ArrayList<>();
    ArrayList<VBox> backgrounds = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<ImageView> pieceImageViews = new ArrayList<>();
    ArrayList<Piece> pieces = Pieces.getPieces();

    public void initialize() {
        initGrid();
        initPieces();
        highlightTurn();
        b1.setOnAction(e -> click(1));
        b1.setOnAction(e -> killPiece(pieces.get(5)));
        b2.setOnAction(e -> killPiece(pieces.get(6)));
    }

    public void click(int id) {
        int[] position = new int[2];
        Random rnd = new Random();
        position[0] = rnd.nextInt(0, 8);
        position[1] = rnd.nextInt(0, 8);
        movePiece(pieces.get(id), position);
    }

    private void initGrid() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                StackPane newField = new StackPane();
                grid.add(newField, j, i);
                fields.add(newField);
            }
        }
        for (int i = 0; i < fields.size(); i++) {
            //Backgrounds
            VBox background = new VBox();
            background.setMaxSize(72, 72);
            fields.get(i).getChildren().add(background);
            backgrounds.add(background);
            //Buttons
            Button button = new Button(" ");
            button.setMaxSize(72, 72);
            button.setOpacity(0);
            int index = i;
            button.setOnMouseEntered(e -> mouseOver(index, true));
            button.setOnMouseExited(e -> mouseOver(index, false));
            button.setOnAction(e -> fieldClicked(index));
            fields.get(i).getChildren().add(button);
            buttons.add(button);
            mouseOver(index, false);
        }
    }

    private void initPieces() {
        Images pieceImages = new Images();
        Image[] images = pieceImages.getPNGs();
        for (int i = 0; i < pieces.size(); i++) {
            ImageView newPiece = new ImageView();
            newPiece.setFitHeight(65);
            newPiece.setFitWidth(65);
            if (pieces.get(i).getColor()) { //Black
                switch (pieces.get(i).getType()) {
                    case "Pawn":
                        newPiece.setImage(images[6]);
                        break;
                    case "Rook":
                        newPiece.setImage(images[7]);

                        break;
                    case "Knight":
                        newPiece.setImage(images[8]);
                        break;
                    case "Bishop":
                        newPiece.setImage(images[9]);
                        break;
                    case "Queen":
                        newPiece.setImage(images[10]);
                        break;
                    case "King":
                        newPiece.setImage(images[11]);
                        break;
                }
            } else { //White
                switch (pieces.get(i).getType()) {
                    case "Pawn":
                        newPiece.setImage(images[0]);
                        break;
                    case "Rook":
                        newPiece.setImage(images[1]);
                        break;
                    case "Knight":
                        newPiece.setImage(images[2]);
                        break;
                    case "Bishop":
                        newPiece.setImage(images[3]);
                        break;
                    case "Queen":
                        newPiece.setImage(images[4]);
                        break;
                    case "King":
                        newPiece.setImage(images[5]);
                        break;
                }
            }
            fields.get(xyToIndex(pieces.get(i).getPosition())).getChildren().add(1, newPiece);
            pieceImageViews.add(newPiece);
        }
    }

    private int xyToIndex(int[] position) {
        return position[0] + position[1] * 8;
    }

    private int[] indexToXY(int index) {
        int[] pos = new int[2];
        int y = 0;
        while (index >= 8) {
            index -= 8;
            y++;
        }
        int x = index;
        pos[0] = x;
        pos[1] = y;
        return pos;
    }

    public void movePiece(Piece piece, int[] moveTo) {
        int currentPos = xyToIndex(piece.getPosition());
        int toPos = xyToIndex(moveTo);
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == piece) {
                fields.get(currentPos).getChildren().remove(pieceImageViews.get(i));
                fields.get(toPos).getChildren().add(1, pieceImageViews.get(i));
            }
        }
    }

    public void killPiece(Piece piece) {
        int currentPos = xyToIndex(piece.getPosition());
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == piece) {
                fields.get(currentPos).getChildren().remove(pieceImageViews.get(i));
                if (piece.getColor()) {//Black
                    dead2.getChildren().add(pieceImageViews.get(i));
                } else { //White
                    dead1.getChildren().add(pieceImageViews.get(i));
                }
            }
        }
    }

    public void winner(boolean color) {

    }

    public void updateBoard() {
        for (int i = 0; i < fields.size(); i++) {
            mouseOver(i, false);
        }
    }

    public void fieldClicked(int index) {
        Piece piece = Pieces.getInstance().getPieceAt(indexToXY(index));
        LogicController.get().choosePiece(piece);
        updateBoard();
    }

    private void mouseOver(int index, boolean isOver) {
        Node node = backgrounds.get(index);
        Piece piece = Pieces.getInstance().getPieceAt(indexToXY(index));

        if (LogicController.get().isHighlighted(index)) { //Highlight
            if (Math.floor(index / 8) % 2 == 0) {
                if (index % 2 == 0) {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ead5aa");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #fffad6");
                } else {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ffec9e");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #ffe9b8");
                }
            } else {
                if (index % 2 == 1) {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ead5aa");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #fffad6");
                } else {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ffec9e");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #ffe9b8");
                }
            }
        } else { //Normal grey/White
            if (Math.floor(index / 8) % 2 == 0) {
                if (index % 2 == 0) {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #dddddd");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #eeeeee");
                } else {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #bbbbbb");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #cccccc");
                }
            } else {
                if (index % 2 == 1) {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #dddddd");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #eeeeee");
                } else {
                    if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #bbbbbb");
                    else node.setStyle(node.getStyle() + "; -fx-background-color: #cccccc");
                }
            }
        }

        if (!piece.getType().equals("Empty")) {
            if (piece.getChosen()) { //Choose with green
                if (Math.floor(index / 8) % 2 == 0) {
                    if (index % 2 == 0) {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #bcffa1");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #c9ffb3");
                    } else {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #8fff62");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #a8ff84");
                    }
                } else {
                    if (index % 2 == 1) {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #bcffa1");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #c9ffb3");
                    } else {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #8fff62");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #a8ff84");
                    }
                }
            } else if (piece.getMarked()) { //Mark with Red
                if (Math.floor(index / 8) % 2 == 0) {
                    if (index % 2 == 0) {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ff6969");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #ff9595");
                    } else {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ff1a1a");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #ff5c5c");
                    }
                } else {
                    if (index % 2 == 1) {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ff6969");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #ff9595");
                    } else {
                        if (isOver) node.setStyle(node.getStyle() + "; -fx-background-color: #ff1a1a");
                        else node.setStyle(node.getStyle() + "; -fx-background-color: #ff5c5c");
                    }
                }
            }
        }


    }

    public void highlightTurn() {
        boolean turn = Pieces.getInstance().isTurn();
        if (turn) {
            dead1.setStyle(dead1.getStyle() + "; -fx-border-color: #aaaaaa; -fx-background-color: #eeeeee");
            dead2.setStyle(dead1.getStyle() + "; -fx-border-color: #52bb19; -fx-background-color: #c3e7c7");
        } else {
            dead1.setStyle(dead1.getStyle() + "; -fx-border-color: #52bb19; -fx-background-color: #c3e7c7");
            dead2.setStyle(dead1.getStyle() + "; -fx-border-color: #aaaaaa; -fx-background-color: #eeeeee");
        }
    }
}

    /*
     public void moveCar(int player, int amount){
        ImageView car = playerCars[player];

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(200));
        transition.setNode(car);
        int yDif = 24;
        car.setTranslateY(yDif);
        int current = 0;
        for(int i = 0; i < fields.length; i++){
            if(fields[i].getChildren().contains(car)){
                current = i;
                break;
            }
        }
        boolean direction = false;
        if(current < 10){ //Move left (negative)
            transition.setToX(-fields[current].getWidth());
            transition.setToY(yDif);
            direction = true;
        }
        else if(current < 20) { //Move up (negative)
            transition.setToX(0);
            transition.setToY((-fields[current].getHeight()) + yDif);
            direction = true;
        }
        else if(current < 30){ //Move right (positive)
            transition.setToX(-fields[current].getWidth());
            transition.setToY(yDif);
            direction = false;
        }
        else if(current < 40){ //Move down (positive)
            transition.setToX(0);
            transition.setToY(fields[current].getHeight() + yDif);
            direction = false;
        }
        if(direction) car.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        else car.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        transition.setCycleCount(1);
        transition.play();

        int finalAmount = amount;
        int finalCurrent = current;
        transition.setOnFinished(e -> {
            int moveTo = 0;
            if(finalCurrent < 39) moveTo = finalCurrent+1;
            else moveTo = 0;
            car.setTranslateX(transition.getByX());
            car.setTranslateY(transition.getByY());
            fields[finalCurrent].getChildren().remove(car);
            try{
                fields[moveTo].getChildren().add(car);
            }catch (IllegalArgumentException p){
                System.out.println();
            }

            multipleCars(player, moveTo);
            if(finalAmount > 1){
                moveCar(player, finalAmount-1);
            }
        });
    }
     */
