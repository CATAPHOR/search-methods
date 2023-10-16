import java.util.Arrays;
import java.util.Scanner;

public class TileGrid 
{
	//stores positions of ABC and Agent
	private int[][] blockAndAgentPos;
	//size of grid
	private int n;
	
	//constructor initialises grid
	public TileGrid(int n, int[][] startPos)
	{
		this.n = n;
		this.blockAndAgentPos = startPos;
		
		//attempt to correct n if value given is less than the number of blocks + agent
		if (n < startPos.length)
		{
			this.n = startPos.length;
		}
	}
	
	//default constructor initialises in accordance to spec
	public TileGrid()
	{
		this(4, new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}});
	}
	
	//get the positions of blocks only
	public int[][] getBlockPos()
	{
		int[][] blockPos = new int[blockAndAgentPos.length - 1][2];
		System.arraycopy(this.blockAndAgentPos, 0, blockPos, 0, blockPos.length);
		return blockPos;
	}
	
	//generates and prints a grid to view
	public void printGrid(int[][] positions)
	{
		//create 2d character array
		char[][] grid = new char[this.n][this.n];
		
		//fill all tiles with squares
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				grid[i][j] = '□';
			}
		}
		
		//place ABC and agent characters in correct positions
		grid[positions[0][0]][positions[0][1]] = 'A';
		grid[positions[1][0]][positions[1][1]] = 'B';
		grid[positions[2][0]][positions[2][1]] = 'C';
		grid[positions[3][0]][positions[3][1]] = 'X';
		
		//print each char[] element of the array
		for(char[] row : grid)
		{
			System.out.println(row);
		}
	}
	
	//prints internal grid status (for testing)
	public void printGrid()
	{
		this.printGrid(this.blockAndAgentPos);
	}
	
	//create valid state without internal positions changing
	public int[][] moveState(String direction, int[][] initialState)
	{
		//get agent's position coord
		int[] agentPos = initialState[initialState.length - 1].clone();
		
		//generate new coordinate for agent
		if(direction.equals("UP"))
		{
			agentPos[0] -= 1;
		}
		else if(direction.equals("DOWN"))
		{
			agentPos[0] += 1;
		}
		else if(direction.equals("LEFT"))
		{
			agentPos[1] -= 1;
		}
		else if(direction.equals("RIGHT"))
		{
			agentPos[1] += 1;
		}
		else
		{
			return null;
		}
		
		//check if grid position invalid
		if(agentPos[0] < 0 || agentPos[0] >= this.n || agentPos[1] < 0 || agentPos[1] >= this.n )
		{
			return null;
		}
		else
		{
			int[][] newState = initialState.clone();
			
			//for through block positions, swap on iter if same position
			for(int i = 0; i < newState.length - 1; i++)
			{
				if(Arrays.equals(newState[i], agentPos))
				{
					//move block to agent's position coordinate; update agent position; return to terminate
					newState[i] = newState[newState.length - 1].clone();
					newState[newState.length - 1] = agentPos;
					return newState;
				}
			}
			
			//hasn't found match, just moves agent
			newState[newState.length - 1] = agentPos;
			return newState;
		}
	}
	
	//move, changing internal positions (for testing)
	public boolean move(String direction)
	{
		int[][] newState = this.moveState(direction, this.blockAndAgentPos.clone());
		if(newState == null)
		{
			return false;
		}
		else
		{
			this.blockAndAgentPos = newState;
			return true;
		}
	}
	
	//for testing purposes
	public static void main(String[] args)
	{	
		TileGrid tilegrid = new TileGrid();
		tilegrid.printGrid();
		tilegrid.getBlockPos();
		
		Scanner s = new Scanner(System.in);
		String input = "";
		while(true)
		{
			input = s.next();
			if(input.equals("STOP"))
			{
				break;
			}
			System.out.println("Valid Move: " + tilegrid.move(input));
			tilegrid.printGrid();
		}
		s.close();
	}
}
