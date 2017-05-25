/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.threads;

import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pawww.thegame.view.Interface;
import pawww.thegame.model.Enemy;
import pawww.thegame.view.Game;

/**
 *
 * @author Lukasz
 */
public class EnemieThread implements Runnable {

    private List<Enemy> list;
    private Interface intf;

    public EnemieThread(List<Enemy> list, Interface intf) {
        this.list = list;
        this.intf = intf;
    }

    public void run() {
        while (!list.isEmpty()) {
            intf.drawEnemies(list);
            try {
                sleep(Game.speed);
            } catch (InterruptedException ex) {
                Logger.getLogger(EnemieThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
