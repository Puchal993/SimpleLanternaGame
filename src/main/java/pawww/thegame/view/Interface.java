/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.view;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import pawww.thegame.State;
import pawww.thegame.model.Enemy;
import pawww.thegame.model.Missile;
import pawww.thegame.model.Player;
import pawww.thegame.threads.MissileThread;

/**
 *
 * @author Lukasz
 */
public class Interface {

    private Screen screen;
    private Game game;
    // Shapes shape=new Shapes();

    public Interface(Screen screen, Game game) {
        this.screen = screen;
        this.game = game;
    }

    String[] epmty = new String[]{"     ", "     ", "     "};

    public void drawPlayer(Player player) {
        String[] playerShape = new String[]{"8 I 8",
            "8(_)8",
            "8-=-8"};
        for (int i = 0; i < 3; i++) {

            screen.putString(player.getX() - 1, player.getY() + i, playerShape[i], Terminal.Color.RED, Terminal.Color.BLACK);
            screen.putString(player.getX() - 2, player.getY() + i, " ", Terminal.Color.RED, Terminal.Color.BLACK);
            screen.putString(player.getX() + 4, player.getY() + i, " ", Terminal.Color.RED, Terminal.Color.BLACK);
        }

        screen.refresh();

    }

    public void drawMissile(Missile mis, List<Enemy> list) throws ScriptException, InterruptedException {
        boolean hit = false;
        int y = 8;
        for (int i = 0; i <= 25; i++) {

            while (!hit && y < 102) {

                if (mis.getX() - 1 >= list.get((y / 8) - 1).getX() && mis.getX() - 1 <= list.get((y / 8) - 1).getZ()) {
                    if (mis.getY() == list.get((y / 8) - 1).getY()) {

                        if (!list.get((y / 8) - 1).isHit()) {
                            game.delIncrement();
                            list.get((y / 8) - 1).setHit();
                            game.delEnemy((y / 8) - 1, screen);
                            game.addScore();

                            screen.putString(29, 29, "      ", Terminal.Color.RED, Terminal.Color.BLACK);
                            screen.putString(29, 29, "SCORE: " + game.getScore(), Terminal.Color.RED, Terminal.Color.BLACK);
                            screen.refresh();
                            hit = true;
                        }
                    }
                    break;
                } else {
                    y += 8;
                    System.out.println("THREAD WORK");
                    if (y > 102) {
                        break;
                    }
                }
            }
            if (!hit) {
                screen.putString(mis.getX() - 1, mis.getY(), "^", Terminal.Color.RED, Terminal.Color.BLACK);
                screen.putString(mis.getX() - 1, mis.getY() + 1, " ", Terminal.Color.RED, Terminal.Color.BLACK);
                mis.doMove();
                screen.refresh();
                try {
                    sleep(60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MissileThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void drawEnemies(List<Enemy> list) {

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < 3; j++) {
                screen.putString(list.get(i).getX(), list.get(i).getY() + j, list.get(i).getShape()[j], Terminal.Color.RED, Terminal.Color.BLACK);

            }
            screen.putString(list.get(i).getX(), list.get(i).getY() - 1, "     ", Terminal.Color.RED, Terminal.Color.BLACK);
            list.get(i).doMove();

            if (list.get(i).getY() >= game.getMaxY() - 4) {
                game.setState(State.GAMEOVER);
            }//wejscie w pole gracza
        }
        screen.refresh();

    }

}
