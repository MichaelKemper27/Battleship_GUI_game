/*
 Michael Kemper
ID: 0506582
December 3rd, 2017
This is Midterm 1
 */
package battleship;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.*;
import java.awt.Color;
import javafx.geometry.Insets;
import java.util.Random;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author mkemper
 */
public class BattleShip extends Application {
    
    private static final int MAXSHIPS = 14;
    private static final int GRIDSIZE  = 16;
    private GridPane pnlPlayer = new GridPane();
    private Label[][] lblPlayer = new Label[GRIDSIZE][GRIDSIZE];
    private Image[] imgShips = new Image[10];
    private Ship[] shipInfo = new Ship[8];
    private char[][] ocean = new char[16][16];    
    private int[][] ShipPart = new int[16][16]; 
    private int[][] ShipNumber = new int[16][16];
    private char[][] ShipDirection = new char[16][16];
    private boolean[][] hit = new boolean[16][16];
    private static int num = 0;
    private Label lblScore = new Label("Misses: 0");
    private static int misses = 0;
    
    @Override
    public void start(Stage primaryStage) {
                
        BorderPane root = new BorderPane();
                
        Scene scene = new Scene(root, 290, 320);
        
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.show();
        lblScore.setFont(Font.font("Arial", FontWeight.BOLD, 12.0));
        lblScore.setTextFill(javafx.scene.paint.Color.RED);
        this.initOcean();
        this.createPlayerPanel();
        createShips();
        root.setCenter(pnlPlayer);
        placeShips();
        this.createOverlay();
        oceanCover();
        GridPane botPane  = new GridPane();
        Button btnReset = new Button("Reset");
        Button giveUp = new Button("Give Up");
        botPane.setHgap(3);
        root.setBottom(botPane);
        lblScore.setPrefWidth(75);
        botPane.add(btnReset, 0, 0);
        botPane.add(lblScore, 10, 0);
        botPane.add(giveUp, 15, 0);
        botPane.setStyle("-fx-background-color:#0000FF;");
        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initOcean();
                misses = 0;
                lblScore.setText("Misses: " + String.valueOf(misses));
                placeShips();
                oceanCover();
            }
        });
        giveUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) 
            {
                Uncover();
            }
        });
        
    }
    private void createPlayerPanel()
    {
       pnlPlayer.setStyle("-fx-background-color:#0000FF;");
       for(int row = 0; row < GRIDSIZE; row++)
       {
           for(int col = 0; col < GRIDSIZE; col++)
           {
               lblPlayer[row][col] = new Label();               
               Image imgShip = new Image("file:Images\\batt100.gif");
               lblPlayer[row][col].setGraphic(new ImageView(imgShip));
               lblPlayer[row][col].setMaxSize(16.0, 16.0);
               lblPlayer[row][col].setStyle("-fx-border-width:1;-fx-border-color:black;");
               pnlPlayer.add(lblPlayer[row][col], col, row);    
                
           
           }
       }
    }
    private int convertLiveToDeadGraphic(int startingPartNumber)
    {
        int newNum;
        switch(startingPartNumber)
        {
            case 1:
                newNum = 1;
                break;
            case 2:
            case 3:
            case 4:
                newNum = 2;
                break;
            case 5:
                newNum = 3;
                break;
            case 6:
                newNum = 4;
                break;
            case 7:
            case 8:
            case 9:
                newNum = 5;
                break;
            case 10:
                newNum = 6;
                break;
            default:
                newNum = startingPartNumber;    
        }
        return newNum;     
    }
    private int shipPartCounter(char shipChar)
    {
        int partNum = 5;
        switch(shipChar)
        {
            case 70:
                partNum = 2;
                break;
            case 77:
                partNum = 3;
                break;
            case 67:
                partNum = 4;
                break;
            case 66:
                partNum = 5;
                break; 
        }
        return partNum;  
    }
    private void createOverlay()
    {
        pnlPlayer.setStyle("-fx-background-color:#0000FF;");
        for(int row = 0; row < GRIDSIZE; row++)
        {
            for(int col = 0; col < GRIDSIZE; col++)
            {
                //lblPlayer[row][col] = new Label();               
                //Image imgShip = new Image("file:Images\\batt101.gif");
                //lblPlayer[row][col].setGraphic(new ImageView(imgShip));
                //lblPlayer[row][col].setMaxSize(16.0, 16.0);
                //lblPlayer[row][col].setStyle("-fx-border-width:1;-fx-border-color:black;");
                //pnlPlayer.add(lblPlayer[row][col], col, row); 
                int r = row;
                int c = col;
                lblPlayer[row][col].setOnMouseClicked(new EventHandler<MouseEvent>() 
                {
                   
                    @Override
                    public void handle(MouseEvent event) 
                    {
                        
                        System.out.println(ShipPart[r][c] + "   |   " + ShipNumber[r][c] );
                        if (hit[r][c])
                        {
                            System.out.println("Already shot there");
                        }
                        else
                        {
                            if (ocean[r][c] == 'O')
                            {
                                hit[r][c] = true;
                                System.out.println("missed");
                                misses++;
                                lblScore.setText("Misses: " + String.valueOf(misses));
                                Image miss = new Image("file:Images\\batt102.gif");
                                lblPlayer[r][c].setGraphic(new ImageView(miss));
                            }
                            else
                            {
                                System.out.println("hit " + ocean[r][c]);
                                hit[r][c] = true;
                                Image hitImg = new Image("file:Images\\batt103.gif");
                                lblPlayer[r][c].setGraphic(new ImageView(hitImg));
                                int partCounter = shipPartCounter(ocean[r][c]);
                                int hitCounter = 0;
                                if (ShipDirection[r][c] =='H')
                                {
                                    int shipStartPos = c - (ShipPart[r][c] - 1);
                                    for (int i = shipStartPos; i < shipStartPos + partCounter; i++)
                                    {
                                        if (ShipNumber[r][i] == ShipNumber[r][c] && hit[r][i])
                                        {
                                            hitCounter++;
                                        }
                                    }
                                }
                                else if (ShipDirection[r][c] =='V')
                                {
                                    int shipStartPos = r - (ShipPart[r][c] - 1);
                                    for (int i = shipStartPos; i < shipStartPos + partCounter; i++)
                                    {
                                        if (ShipNumber[i][c] == ShipNumber[r][c] && hit[i][c])
                                        {
                                            hitCounter++;
                                        }
                                    }
                                }
                                
                                if (partCounter == hitCounter)
                                {
                                    if (ShipDirection[r][c] =='H')
                                    {
                                        int shipStartPos = c - (ShipPart[r][c] - 1);
                                        for (int i = shipStartPos; i < shipStartPos + partCounter; i++)
                                        {
                                            if (ShipNumber[r][i] == ShipNumber[r][c] && hit[r][i])
                                            {
                                                Image newShipGraphic = new Image("file:Images\\batt20" + convertLiveToDeadGraphic(ShipPart[r][i]) + ".gif");
                                                lblPlayer[r][i].setGraphic(new ImageView(newShipGraphic));
                                                if (ShipPart[r][i] == partCounter)
                                                {
                                                    Image endShipGraphic = new Image("file:Images\\batt203.gif");
                                                    lblPlayer[r][i].setGraphic(new ImageView(endShipGraphic)); 
                                                }
                                            }
                                        }
                                    }
                                    else if (ShipDirection[r][c] =='V')
                                    {
                                        int shipStartPos = r - (ShipPart[r][c] - 1);
                                        for (int i = shipStartPos; i < shipStartPos + partCounter; i++)
                                        {
                                            if (ShipNumber[i][c] == ShipNumber[r][c] && hit[i][c])
                                            {
                                                System.out.println(ShipPart[i][c]+ "     " +partCounter );
                                                Image newShipGraphic = new Image("file:Images\\batt20" + convertLiveToDeadGraphic(ShipPart[i][c]+5) + ".gif");
                                                lblPlayer[i][c].setGraphic(new ImageView(newShipGraphic));
                                                if (ShipPart[i][c] == partCounter)
                                                {
                                                    Image endShipGraphic = new Image("file:Images\\batt206.gif");
                                                    lblPlayer[i][c].setGraphic(new ImageView(endShipGraphic)); 
                                                }
                                                //System.out.println("ship part is: " + ShipPart[i][c] + " ||| dead new ship part is: " + convertLiveToDeadGraphic(ShipPart[i][c]));
                                                //System.out.println("ship part is: " + ShipPart[i][c] + " ||| part counter is: " + partCounter);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
           
            }
        }
      
    }
    private void createShips()
    {
        this.loadShipImages();
        this.createShipInfo();
    }
    private void loadShipImages()
    {
        for(int i = 0; i < 10 ; i++)
        {
            imgShips[i] = new Image("file:Images\\batt" + (i + 1) + ".gif");
        }
    }
    private void oceanCover()
    {
        for(int row = 0; row < GRIDSIZE; row++)
       {
           for(int col = 0; col < GRIDSIZE; col++)
           {              
               Image imgShip = new Image("file:Images\\batt100.gif");
               lblPlayer[row][col].setGraphic(new ImageView(imgShip));
           }
       }
    }
    private void Uncover()
    {
        for(int row = 0; row < GRIDSIZE; row++)
       {
           for(int col = 0; col < GRIDSIZE; col++)
           {
               if (!hit[row][col] && ocean[row][col] != 'O')
               {
                   if (ShipDirection[row][col] == 'H')
                   {
                        Image newShipGraphic = new Image("file:Images\\batt" + ShipPart[row][col] + ".gif");
                        lblPlayer[row][col].setGraphic(new ImageView(newShipGraphic));
                        switch(ocean[row][col])
                        {
                            case 70:
                                if (ShipPart[row][col] == 2)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt5.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break;
                            case 77:
                                if (ShipPart[row][col] == 3)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt5.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break;
                            case 67:
                                if (ShipPart[row][col] == 4)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt5.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break;
                            case 66:
                                if (ShipPart[row][col] == 5)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt5.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break; 
                        }
                   }
                   else
                   {
                        Image newShipGraphic = new Image("file:Images\\batt" + (5+ShipPart[row][col]) + ".gif");
                        lblPlayer[row][col].setGraphic(new ImageView(newShipGraphic));
                        switch(ocean[row][col])
                        {
                            case 70:
                                if (ShipPart[row][col] == 2)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt10.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break;
                            case 77:
                                if (ShipPart[row][col] == 3)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt10.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break;
                            case 67:
                                if (ShipPart[row][col] == 4)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt10.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break;
                            case 66:
                                if (ShipPart[row][col] == 5)
                                {
                                    Image endShipGraphic = new Image("file:Images\\batt10.gif");
                                    lblPlayer[row][col].setGraphic(new ImageView(endShipGraphic));
                                }
                                break; 
                        }
                   }
               }
           }
       }
    }
    private void createShipInfo()
    {
        //Start with the frigate, we create 2 of them here but will place 3 total randomly it as two images
		int[] frigateH = {0,4};
		shipInfo[0] = new Ship("Frigate", frigateH, 'H');
		int[] frigateV = {5,9};
		shipInfo[1] = new Ship("Frigate", frigateV, 'V');
		
        // Create the mine Sweep it has 3 pieces
		int[] mineSweepH = {0,1,4};
		shipInfo[2] = new Ship("Minesweep", mineSweepH, 'H');
		int[] mineSweepV = {5,6,9};
		shipInfo[3] = new Ship("Minesweep", mineSweepV, 'V');
		
		int[] cruiserH = {0,1,2,4};
		shipInfo[4] = new Ship("Cruiser", cruiserH, 'H');
		int[] cruiserV = {5,6,7,9};
		shipInfo[5] = new Ship("Cruiser", cruiserV, 'V');
		
		int[] battleShipH = {0,1,2,3,4};
		shipInfo[6] = new Ship("Battleship", battleShipH, 'H');
		int[] battleShipV = {5,6,7,8,9};
		shipInfo[7] = new Ship("Battleship", battleShipV, 'V'); 
    }
    private void initOcean()
    {
        for(int row = 0; row < 16; row++)
        {
            for(int col = 0; col < 16; col++)
            {
                    ocean[row][col] = 'O';
                    ShipPart[row][col] = 0;
                    ShipNumber[row][col] = 0;
                    ShipDirection[row][col] = 'N';
                    hit[row][col] = false;
            }
        }
    }
    private void placeShips()
    {
       // Create a Random object to select ships
        Random r = new Random();

        // Create random objects to place the ship at a row and a column
        Random randCol = new Random();
        Random randRow = new Random();

        //Place the ships, typically there are 14
        for(int ships = 0; ships < MAXSHIPS; ships++)
        {
                //Get a random ship
                Ship si = shipInfo[r.nextInt(8)];

                int row = randRow.nextInt(16);
                int col = randCol.nextInt(16);
                int direction = checkDirection(si, row, col);
                while(direction == 0) // 0 direction says that we can not place the ship
                {
                        row = randRow.nextInt(16);
                        col = randCol.nextInt(16);
                        direction = checkDirection(si, row, col);
                }
                // got a clear path, let put the ship on the ocean
                int shipPieces[] = si.getShipPieces();
                if(si.Direction == 'H')  // place horizontal
                {
                        if(direction == 1)
                        {
                            for(int i = col, j = 0; i < col + si.length(); i++, j++)
                            {                                                          
                                lblPlayer[row][i].setGraphic(new ImageView(imgShips[shipPieces[j]]));
                                String name = si.getName();
                                ocean[row][i] = name.charAt(0);
                                ShipPart[row][i] = j+1;
                                ShipNumber[row][i] = num;
                                ShipDirection[row][i] = 'H';
                            }
                            num++;
                        }
                        else
                        {
                            for(int i = col + si.length(), j = 0 ; i > col; i--, j++)
                            {
                                lblPlayer[row][i].setGraphic(new ImageView(imgShips[shipPieces[j]]));	
                                String name = si.getName();
                                ocean[row][i] = name.charAt(0);
                                ShipPart[row][i] = j+1;
                                ShipNumber[row][i] = num;
                                ShipDirection[row][i] = 'H';
                            }
                            num++;
                        }
                }
                else // Must be vertical direction
                {
                        if(direction == 1) // place pieces in positive direction
                        {
                            for(int i = row, j = 0; i < row + si.length(); i++, j++)
                            {
                                lblPlayer[i][col].setGraphic(new ImageView(imgShips[shipPieces[j]]));	
                                String name = si.getName();
                                ocean[i][col] = name.charAt(0);
                                ShipPart[i][col] = j+1;
                                ShipNumber[i][col] = num;
                                ShipDirection[i][col] = 'V';
                            }
                            num++;
                        }
                        else
                        {
                            for(int i = row + si.length(), j = 0; i > row; i--, j++)
                            {
                                lblPlayer[i][col].setGraphic(new ImageView(imgShips[shipPieces[j]]));	
                                String name = si.getName();
                                ocean[i][col] = name.charAt(0);
                                ShipPart[i][col] = j+1;
                                ShipNumber[i][col] = num;
                                ShipDirection[i][col] = 'V';
                            }
                            num++;
                        }
                }			
        } 
    }
    private int checkDirection(Ship si, int row, int col)
    {
        if(si.Direction == 'H')
            return checkHorizontal(si, row, col);
        else
            return checkVertical(si, row, col);
    }
    int checkHorizontal(Ship si,int row, int col)
    {
            boolean clearPath = true;

            int len = si.length();
            System.out.println(len);
            for(int i = col; i < (col + si.length()); i++)
            {
                    if(i >= GRIDSIZE) //This would put us outside the ocean
                    {
                            clearPath = false;
                            break;
                    }
                    if(ocean[row][i] != 'O') // Ship already exists in this spot
                    {
                            clearPath = false;
                            break;
                    }
            }
            if(clearPath == true) // ok to move in the positive direction
                    return 1; 

            //Next Chec the negative direction
            for(int i = col; i > (col - si.length()); i--)
            {
                    if(i < 0) //This would put us outside the ocean
                    {
                            clearPath = false;
                            break;
                    }
                    if(ocean[row][i] != 'O') // Ship already exists in this spot
                    {
                            clearPath = false;
                            break;
                    }			

            }
            if(clearPath == true) //Ok to move in negative direction
                    return -1;
            else
                    return 0;   // No place to move			

    }
	
    int checkVertical(Ship si,int row, int col)
    {
            boolean clearPath = true;
            int len = si.length();
            System.out.println(len);

            for(int i = row; i < (row + si.length()); i++)
            {
                if(i >= GRIDSIZE) //This would put us outside the ocean
                {
                        clearPath = false;
                        break;
                }
                if(ocean[i][col] != 'O') // Ship already exists in this spot
                {
                        clearPath = false;
                        break;
                }
            }
            if(clearPath == true) // ok to move in the positive direction
                    return 1; 

            //Next Check the negative direction
            for(int i = row; i > (row - si.length() ); i--)
            {
                if(i < 0) //This would put us outside the ocean
                {
                        clearPath = false;
                        break;
                }
                if(ocean[i][col] != 'O') // Ship already exists in this spot
                {
                        clearPath = false;
                        break;
                }			

            }
            if(clearPath == true) //Ok to move in negative direction
                return -1;
            else
                return 0;   // No place to move			

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
