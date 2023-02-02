package com.example.chess.logic;

import com.example.chess.ViewController;

public class LogicController extends HighlightLogic{

    private static LogicController controller;
    private HighlightLogic highlightLogic;
    private ViewController viewController;

    public static LogicController get(){
        if(controller == null) controller = new LogicController();
        return controller;
    }

    public void setController(ViewController viewController){
        this.viewController = viewController;
    }



}
