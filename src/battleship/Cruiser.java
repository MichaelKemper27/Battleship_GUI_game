/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import javafx.scene.image.Image;

public class Cruiser extends Ship {

    static String Name = "Cruiser";
    static int [] pieces = new int[]{0,4};
    static char Direction = 'H';
    Image[] imgShipsH = new Image[5];
    Image[] imgShipsV = new Image[5];
    public Cruiser(char Direction) {
        super(Name, pieces, Direction);
    }
    public Cruiser(String name, int[] pieces, char Direction) {
        super(name, pieces, 'H');
    }
    public String battImgString(String battName)
    {
        return("file:Images\\batt" + battName + ".gif");
    }
    
    public Image[] frigateH()
    {
        imgShipsH[0] = new Image(battImgString("1"));
        imgShipsH[1] = new Image(battImgString("2"));
        imgShipsH[2] = new Image(battImgString("3"));
        imgShipsH[3] = new Image(battImgString("5"));
        return imgShipsH;
    }
    public Image[] frigateV()
    {
        imgShipsV[0] = new Image(battImgString("6"));
        imgShipsV[1] = new Image(battImgString("7"));
        imgShipsV[2] = new Image(battImgString("8"));
        imgShipsV[3] = new Image(battImgString("10"));
        return imgShipsV;
    }

}