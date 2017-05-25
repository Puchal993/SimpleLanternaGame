/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.view;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import pawww.thegame.State;

/**
 *
 * @author Daniel
 */
public class ScoreView {

    private Screen screen;
    Key key;
    private int maxY = 100;
    private int maxX = 30;
    private String[] shape;
    private State state;
    Scanner scanner;
    String filename = "scores.txt";
    String line = null;
    ArrayList<ArrayList<String>> items;
    int pageCount;
    int currentPage;

    public ScoreView(Screen screen, State state) {
        this.screen = screen;
        this.state = state;
        this.shape = new String[7];

        this.shape[0] = "################################";
        for (int i = 1; i < 6; i++) {
            this.shape[i] = "#                        |     #";
        }
        this.shape[6] = "################################";

        try {
            scanner = new Scanner(new File(filename));
            scanner.useDelimiter(":");
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        currentPage = 1;
    }

    public void drawTable() throws InterruptedException {
        screen.clear();
        screen.refresh();
        screen.putString(maxX / 2 + 30, 9, "Wyniki:", Terminal.Color.WHITE, Terminal.Color.BLACK);
        screen.refresh();
        for (int i = 0; i < 7; i++) {
            sleep(50);
            screen.putString(maxX / 2 + 20, 10 + i, this.shape[i], Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        }
        screen.putString(maxX/2 + 44, 18, "<     >", Terminal.Color.WHITE, Terminal.Color.BLACK);
        readScores();
        writeScores(currentPage);
    }

    public void readScores() {
        items = new ArrayList();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            items.add(new ArrayList(Arrays.asList(line.split(":"))));
        }

        pageCount = (items.size() + 4) / 5;

    }

    public void writeScores(int page) {
        for(int i = 0;i<5;i++){
            screen.putString(maxX / 2 + 22, 11 + i , "                       ", Terminal.Color.WHITE, Terminal.Color.BLACK);
                screen.putString(maxX / 2 + 46, 11 + i, "     ", Terminal.Color.WHITE, Terminal.Color.BLACK);
                screen.refresh();
        }
                
                
                
        for (int i = 0 + (5 * (page - 1)); i < 5 + (5 * (page - 1)); i++) {
            if (i < items.size()) {

                screen.putString(maxX / 2 + 22, 11 + i - (5 * (page - 1)), items.get(i).get(0), Terminal.Color.WHITE, Terminal.Color.BLACK);
                screen.putString(maxX / 2 + 46, 11 + i - (5 * (page - 1)), items.get(i).get(1), Terminal.Color.WHITE, Terminal.Color.BLACK);
                screen.refresh();
            }
        }
        screen.putString(maxX / 2 + 46, 18, Integer.toString(page) + "/" + Integer.toString(pageCount), Terminal.Color.WHITE, Terminal.Color.BLACK);
        screen.refresh();
    }

    public void update() {
        boolean exit = false;

        while (!exit) {

            key = screen.readInput();
            while (key == null) {
                key = screen.readInput();
            }

            switch (key.getKind()) {
                case ArrowRight:
                    if (currentPage < pageCount) {
                        currentPage++;
                    }
                    writeScores(currentPage);
                    break;
                case ArrowLeft:
                    if(currentPage<=pageCount&&currentPage>1){
                        currentPage--;
                    }
                        writeScores(currentPage);
                    break;
                case Escape:

                    exit = true;
                    state = State.MENU;
                    break;
                default:
                    break;

            }
        }
    }

    public State getState() {
        return state;
    }

}
