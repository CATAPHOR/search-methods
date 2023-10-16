import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class AStar 
{
	private int[][] goalState;
	private HashMap<AStarNode, Integer> expansionCandidates;
	private boolean found;
	private TileGrid tilegrid;
	private int nodesExpanded;
	
	//constructor takes a tilegrid-format goal state and start state and contains main loop
	public AStar(int[][] goalState, int[][] startState, int n)
	{
		//initialise flags, tilegrid helper class, etc.
		this.found = false;
		this.tilegrid = new TileGrid(n, startState);
		this.nodesExpanded = 0;
		this.goalState = goalState;
		
		//HashMap stores node fringe; initialise and add head node with tilegrid start state
		expansionCandidates = new HashMap<AStarNode, Integer>();
		expansionCandidates.put(new AStarNode(startState), this.hCost(startState));
		
		//main algorithm loop
		while (!found)
		{
			//lowestCost stores current lowest found expansion candidate entry
			Entry<AStarNode, Integer> lowestCost = null;
			//identify node with lowest cost from expansionCandidates
			for(Entry<AStarNode, Integer> candidate : this.expansionCandidates.entrySet()) 
			{
			    if(lowestCost == null || lowestCost.getValue() > candidate.getValue()) 
			    {
			        lowestCost = candidate;
			    }
			}
			
			//call expand on node specified
			this.expand(lowestCost.getKey());
		}
	}
	
	//default constructor gives spec-defined start and goal states
	public AStar()
	{
		this(new int[][] {{1, 1}, {2, 1}, {3, 1}}, new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}}, 4);
	}
	
	//estimate of cost to goal state; gets sum of manhattan distances for each block from goal state
	public int hCost(int[][] testState)
	{
		int output = 0;
		for(int i = 0; i < this.goalState.length; i++)
		{
			output += Math.abs(testState[i][0] - this.goalState[i][0]) + 
					Math.abs(testState[i][1] - this.goalState[i][1]);
		}
		return output;
	}
	
	public void expand(AStarNode node)
	{
		//add all valid move state nodes as keys to expansionCandidates with g() + h() calculation vals
		for(String direction : new String[] {"LEFT", "RIGHT", "UP", "DOWN"})
		{
			//for each possible action, add to expansion candidates node with resultant state
			int[][] newState = AStar.this.tilegrid.moveState(direction, node.state);
			if(newState != null)
			{
				this.expansionCandidates.put(new AStarNode(node, direction, newState), 
						node.gCost + 1 + hCost(newState));
			}
		}
		this.nodesExpanded++;
		
		//check node state correct
		if(Arrays.equals(node.state[0], this.goalState[0]) && Arrays.equals(node.state[1], this.goalState[1])
				&& Arrays.equals(node.state[2], this.goalState[2]))
		{
			this.found = true;
			System.out.println("Solution Found!");
			tilegrid.printGrid(node.state);
			System.out.println("Actions to get to this state: " + node.actions);
			System.out.println("Node path cost: " + node.gCost);
			System.out.println("Nodes expanded: " + this.nodesExpanded);
		}
		
		//remove node from expansionCandidates
		this.expansionCandidates.remove(node);
	}
	
	//custom node class for A* algo
	class AStarNode
	{
		String actions;
		AStarNode parent;
		int[][] state;
		int gCost;
		
		//constructor ensures node has parent reference, tilegrid state, and string of actions to state
		public AStarNode(AStarNode parent, String action, int[][] state)
		{
			this.parent = parent;
			this.actions = parent.actions + "," + action;
			this.gCost = parent.gCost + 1; //with each succession, step cost 1 so cost = depth
			this.state = state;
		}
		
		//default generates head node with start state, auto-expands
		public AStarNode(int[][] state)
		{
			this.parent = null;
			this.actions = "";
			this.gCost = 0;
			this.state = state;
		}
	}
	
	//run A*
	public static void main(String[] args)
	{
		new AStar(new int[][] {{1, 1}, {2, 1}, {3, 1}}, new int[][] {{1, 1}, {2, 1}, {3, 2}, {3, 1}}, 4);
	}
}
