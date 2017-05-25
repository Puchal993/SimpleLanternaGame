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
public class Enemy extends Tank {

    private boolean hit=false;
    int z;
   String[] shape = new String[]{"8-_-8",
            "8( )8",
            "8 T 8"};
    
    public Enemy(int x, int y) {
        super(x, y);
        z=x+4;

    }

    public void doMove() {
        y++;
    }
    
    public void del(){
    shape=new String[]{"     ","     ","     "};
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit() {
        this.hit = true;
    }

    public String[] getShape() {
        return shape;
    }

    public void setShape(String[] shape) {
        this.shape = shape;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
    
    
 
}
