/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.view;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import pawww.thegame.State;
import pawww.thegame.model.Enemy;
import pawww.thegame.model.Explosion;
import pawww.thegame.model.Player;
import pawww.thegame.threads.EnemieThread;
import pawww.thegame.threads.MissileThread;

/**
 *
 * @author Daniel
 */
public class Game  {

 
    Screen screen;
    ScreenWriter screenWriter;
    State state;
    public Key key;
    private final int maxX = 100;
    private final int maxY = 30;
    private final String string = "Wpisz swoje imię:";
    private String inputString = "";
    private Player player;
    private String score;
    private List<Enemy> enemies = new LinkedList<Enemy>();
    int del = 0;
    static public int speed = 2600;

    public Game(Screen screen, State state) {
        this.screen = screen;
        this.state = state;
        this.screenWriter = new ScreenWriter(this.screen);
        this.speed = 2500;
        this.score = "0";
        player = new Player(25, 26);

    }

    void drawExplosion(Screen screen, int x, int y) throws InterruptedException {
        Explosion e = new Explosion(x, y);
        screen.putString(e.getX() - 2, e.getY(), e.explosion[0][0], Terminal.Color.RED, Terminal.Color.BLACK);
        screen.refresh();
        sleep(100);
        for (int i = -1; i < 2; i++) {
            screen.putString(e.getX() - 2, e.getY() + i, e.explosion[1][i + 1], Terminal.Color.RED, Terminal.Color.BLACK);
        }
        screen.refresh();
        sleep(100);
        for (int i = -1; i < 2; i++) {
            screen.putString(e.getX() - 2, e.getY() + i, e.explosion[2][i + 1], Terminal.Color.RED, Terminal.Color.BLACK);
        }
        screen.refresh();
        sleep(100);
        for (int i = -1; i < 2; i++) {
            screen.putString(e.getX() - 2, e.getY() + i, e.explosion[3][i + 1], Terminal.Color.RED, Terminal.Color.BLACK);
        }
        screen.refresh();
    }

    public void addEnemy() {
        int i = 3;
        enemies.removeAll(enemies);
        while (i <= 98) {
            enemies.add(new Enemy(i, 0));
            i += 8;
        }
    }

    public void delIncrement() {
        del++;
    }

    public void delEnemy(int i, Screen screen) throws InterruptedException {
        enemies.get(i).del();
        for (int j = 0; j < 3; j++) {
            screen.putString(enemies.get(i).getX(), enemies.get(i).getY() + j, enemies.get(i).getShape()[j], Terminal.Color.RED, Terminal.Color.BLACK);
        }
        screen.putString(enemies.get(i).getX(), enemies.get(i).getY() - 1, enemies.get(i).getShape()[0], Terminal.Color.RED, Terminal.Color.BLACK);
        screen.refresh();
        drawExplosion(screen, enemies.get(i).getX() + 2, enemies.get(i).getY());
        System.out.println(del);
        if (del == enemies.size()) {
            del = 0;

            enemies.clear();
            addEnemy();
            if (speed > 2000) {
                speed -= 1000;
            } else if (speed <= 2000) {
                speed -= 500;
            } else if (speed <= 1000) {
                speed -= 100;
            } else if (speed <= 500);
        }
    }

    public Key readKey(Screen screen) {
        key = screen.readInput();

        while (key == null) {
            key = screen.readInput();
        }
        return key;
    }

    public void addScore() throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String foo = score += "+100";
        score = engine.eval(foo).toString();

    }

    public String getScore() {
        return score;
    }

    public void play() {
        state = State.PLAY;
        addEnemy();
        // Odpalamy
        Interface intf = new Interface(screen, this);
        screen.putString(29, 29, "SCORE: " + score, Terminal.Color.RED, Terminal.Color.BLACK);
        intf.drawPlayer(player);

        EnemieThread e = new EnemieThread(enemies, intf);
        (new Thread(e)).start();

        key = readKey(screen);

        while (key.getKind() != Key.Kind.Escape) {

            key = screen.readInput();

            while (key == null) {
                key = screen.readInput();
                if (state == State.GAMEOVER) {
                    enemies.clear();
                    screen.clear();
                    screen.refresh();
                    gameOver();
                    return;
                }
            }

            if (state == State.GAMEOVER) {
                enemies.clear();
                screen.clear();
                screen.refresh();
                gameOver();

                state = State.MENU;
                return;
            }

            switch (key.getKind()) {
                case ArrowLeft:
                    player.goLeft();
                    intf.drawPlayer(player);
                    break;
                case ArrowRight:
                    player.goRight();
                    intf.drawPlayer(player);
                    break;
                case Enter:
                    MissileThread m = new MissileThread(screen, intf, player, enemies);
                    (new Thread(m)).start();
                    break;
                default:
                    break;

            }
        }
        enemies.clear();
        screen.clear();
        screen.refresh();
        state = State.MENU;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void gameOver() {
        boolean exit = false;

        screen.putString(10, 1, " ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ ", Terminal.Color.RED, Terminal.Color.BLACK);
        screen.putString(10, 2, "██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗", Terminal.Color.RED, Terminal.Color.BLACK);
        screen.putString(10, 3, "██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝", Terminal.Color.RED, Terminal.Color.BLACK);
        screen.putString(10, 4, "██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗", Terminal.Color.RED, Terminal.Color.BLACK);
        screen.putString(10, 5, "╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝ ╚████╔╝ ███████╗██║  ██║", Terminal.Color.RED, Terminal.Color.BLACK);
        screen.putString(10, 6, " ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝   ╚═══╝  ╚══════╝╚═╝  ╚═╝", Terminal.Color.RED, Terminal.Color.BLACK);

        screenWriter.drawString(maxX / 2, maxY / 2 - 1, string);
        screenWriter.drawString(maxX / 2, maxY / 2 - 2, "Score: " + score);
        screenWriter.drawString(maxX / 2 - 20, maxY / 2 + 1, "Enter - Zapisz wynik  Esc - Wyjdź do menu");

        screen.refresh();
        while (!exit) {

            key = screen.readInput();
            while (key == null) {
                key = screen.readInput();
            }

            if (key.getKind() == Key.Kind.Backspace) {
                screenWriter.drawString(maxX / 2 + inputString.length() - 1, maxY / 2, " ");
                if (inputString.length() != 0) {
                    inputString = inputString.substring(0, inputString.length() - 1);
                }
                screenWriter.drawString(maxX / 2, maxY / 2, inputString);
                screen.refresh();
            } else if (key.getKind() == Key.Kind.Escape) {
                screen.clear();
                screen.refresh();
                exit = true;
            } else if (key.getKind() == Key.Kind.Enter) {
                try {
                    try (BufferedWriter bWriter = new BufferedWriter(new FileWriter("scores.txt", true))) {
                        bWriter.write(inputString + ":" + score);
                        bWriter.newLine();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }

                //zapisz wynik - do zrobienia
                exit = true;
            } else {
                char c = key.getCharacter();
                inputString = inputString.concat(String.valueOf(c));
                screenWriter.drawString(maxX / 2, maxY / 2, inputString);
                screen.refresh();
            }

        }
        state = State.MENU;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxX() {
        return maxX;
    }

 

}
