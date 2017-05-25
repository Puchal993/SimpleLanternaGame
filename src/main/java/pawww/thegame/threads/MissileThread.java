/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.threads;

import com.googlecode.lanterna.screen.Screen;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import pawww.thegame.view.Interface;
import pawww.thegame.model.Enemy;
import pawww.thegame.model.Missile;
import pawww.thegame.model.Player;

/**
 *
 * @author Lukasz
 */
public class MissileThread implements Runnable {

    private Screen screen;
    private List<Enemy> list;
    private Missile mis;
    private Interface intf;

    public MissileThread(Screen screen, Interface intf, Player play, List<Enemy> list) {
        this.intf = intf;
        this.list = list;
        this.screen = screen;
        mis = new Missile(play.getX() + 2, play.getY() - 2);
    }

    public void run() {
         
        try {
            intf.drawMissile(mis,list);
        } catch (InterruptedException ex) {
            Logger.getLogger(MissileThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex) {
            Logger.getLogger(MissileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("THREAD SZTOP!!!");
    }
}
