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
public class Explosion extends Tank {

    public String[][] explosion = new String[4][];

    public Explosion(int x, int y) {
        super(x, y);
        explosion[0] = new String[]{"  @  "};
        explosion[1] = new String[]{"  #  ",
            " #@# ",
            "  #  "};
        explosion[2] = new String[]{"# # #"," #@# ","# # #"};
        explosion[3] = new String[]{"     ","     ","     "};
    }

}
