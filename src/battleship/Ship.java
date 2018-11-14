/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

/**
 *
 * @author mkemper
 */
public class Ship
{
	protected String shipName;
	protected int[] shipPieces; 
	char Direction;
		
	Ship(String name, int[] pieces,char Direction)
	{
		this.Direction = Direction;
		this.shipName = name;
		shipPieces = new int[pieces.length];
		for(int i = 0; i < pieces.length; i++)
			shipPieces[i] = pieces[i];
		
	}
	public String getName()
	{
		return this.shipName;
	}
	public int[] getShipPieces()
	{
		return shipPieces;
	}
	public int getDirection()
	{
		return this.Direction;
	}
	public int length()
	{
		return shipPieces.length;
	}

}