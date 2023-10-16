import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DFS 
{
	private int nodesExpanded;
	private int[][] goalState;
	private TileGrid tilegrid;
	private DFSNode current;
	private Random random;
	//flags used to control algorithm's loops
	private boolean found, goingBackUp;

	public DFS(int[][] goalState, int[][] startState, int n)
	{
		//used to generate random numbers for which node to expand
		this.random = new Random();
		
		//initialise flags, tilegrid helper class, etc.
		this.found = false;
		this.goingBackUp = false;
		this.nodesExpanded = 0;
		this.goalState = goalState;
		this.tilegrid = new TileGrid(n, startState);
		
		//set initial head node
		this.current = new DFSNode(startState);
		
		//main loop
		while(!found)
		{
			//expand node, evaluate children nodes possible with valid moves
			if(this.current.children == null && !this.goingBackUp)
			{
				this.current.getChildren();
				this.nodesExpanded++;
			}
			//searchNode evaluates if node achieves goal state and determines next node to expand
			this.searchNode(this.current);
		}
	}
	
	//default uses goalstate and startstate from spec
	public DFS()
	{
		this(new int[][] {{1, 1}, {2, 1}, {3, 1}}, new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}}, 4);
	}
	
	//checks node for solution and sets next node to expand
	public void searchNode(DFSNode node)
	{
		//check if node's status = goal state
		if(Arrays.equals(node.state[0], this.goalState[0]) && Arrays.equals(node.state[1], this.goalState[1])
				&& Arrays.equals(node.state[2], this.goalState[2]))
		{
			this.found = true;
			System.out.println("Solution Found!");
			tilegrid.printGrid(node.state);
			System.out.println("Actions to get to this state: " + node.actions);
			System.out.println("Node depth/path cost: " + node.depth);
			System.out.println("Nodes expanded: " + this.nodesExpanded);
		}
		
		//set the next node to expand
		else
		{
			//if no children for node
			if(node.children.size() == 0)
			{
				//if no parent then no solution exists
				if(node.parent == null)
				{
					System.out.println("No solution.");
					this.found = true; //not found but necessary to end while loop
					this.goingBackUp = false;
				}
				//if no children from current node, return to parent and remove current from parent's children
				else
				{
					node.parent.children.remove(node);
					this.current = node.parent;
					this.goingBackUp = true;
				}
			}
			else
			{
				//choose random node to expand from children
				this.current = node.children.get(random.nextInt(node.children.size()));
				this.goingBackUp = false;
			}
		}
	}
	
	//custom node class for DFS
	class DFSNode
	{
		String actions;
		int depth;
		DFSNode parent;
		int[][] state;
		ArrayList<DFSNode> children;
		
		//constructor takes parent node, node's state, and the set of actions to reach it
		public DFSNode(DFSNode parent, String action, int[][] state)
		{
			this.parent = parent;
			this.actions = parent.actions + "," + action;
			this.depth = parent.depth + 1;
			this.state = state;
			this.children = null;
		}
		
		//default generates head node with start state
		public DFSNode(int[][] startState)
		{
			this.parent = null;
			this.actions = "";
			this.depth = 0;
			this.state = startState;
			this.children = null;
		}
		
		////populate array with children of states resulting from valid moves
		public void getChildren()
		{
			this.children = new ArrayList<DFSNode>();
			for(String direction : new String[] {"LEFT", "RIGHT", "UP", "DOWN"})
			{
				//for each possible action, add to children array with node with state of said action
				int[][] newState = DFS.this.tilegrid.moveState(direction, this.state);
				if(newState != null)
				{
					this.children.add(new DFSNode(this, direction, newState));
				}
			}
		}
	}
	
	//run dfs
	public static void main(String[] args)
	{
		new DFS();
	}
}