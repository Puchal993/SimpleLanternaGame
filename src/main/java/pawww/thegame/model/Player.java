/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.model;

/**
 *
 * @author Lukasz
 */
public class Player extends Tank {

    public Player(int x, int y) {
        super(x, y);
    }

    public void goLeft() {
        if (x != 2) {
            x--;
        }
    }

    public void goRight() {
        x++;
    }

}
