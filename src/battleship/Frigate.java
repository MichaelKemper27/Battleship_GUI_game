/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import javafx.scene.image.Image;

public class Frigate extends Ship {

    static String frigateName = "Frigate";
    static int [] frigatepieces = new int[]{0,4};
    static char frigateDirection = 'H';
    Image[] imgShipsH = new Image[2];
    Image[] imgShipsV = new Image[2];
    public Frigate(char Direction) {
        super(frigateName, frigatepieces, Direction);
    }
    public Frigate(String name, int[] pieces, char Direction) {
        super(frigateName, pieces, 'H');
    }
    public String battImgString(String battName)
    {
        return("file:Images\\batt" + battName + ".gif");
    }
    
    public Image[] frigateH()
    {
        imgShipsH[0] = new Image(battImgString("1"));
        imgShipsH[1] = new Image(battImgString("5"));
        return imgShipsH;
    }
    public Image[] frigateV()
    {
        imgShipsV[0] = new Image(battImgString("6"));
        imgShipsV[1] = new Image(battImgString("10"));
        return imgShipsV;
    }

}
