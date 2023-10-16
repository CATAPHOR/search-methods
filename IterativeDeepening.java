import java.util.ArrayList;
import java.util.Arrays;

public class IterativeDeepening 
{
	private long nodesExpanded;
	private int[][] goalState;
	private TileGrid tilegrid;
	private ItNode current;
	private int maxDepth;
	//flags used to control algorithm's loops
	private boolean found, notfound, goingBackUp;

	//constructor takes specific goalstate and startstate and contains main loop
	public IterativeDeepening(int[][] goalState, int[][] startState, int n)
	{
		//initialise flags, tilegrid helper class, etc.
		this.found = false;
		this.nodesExpanded = 0;
		this.goalState = goalState;
		this.tilegrid = new TileGrid(n, startState);
		this.goingBackUp = false;
		
		//main loop with maxdepth increment until found allows for break
		for(this.maxDepth = 0; ; this.maxDepth++)
		{
			//start anew with head node on each iteration
			this.notfound = false;
			this.current = new ItNode(startState);
			
			while(!this.found && !this.notfound)
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
			
			//break loop if found
			if (this.found)
			{
				break;
			}
		}
		
		if(!this.found)
		{
			System.out.println("No solution.");
		}
	}
	
	//default uses goalstate and startstate from spec
	public IterativeDeepening()
	{
		this(new int[][] {{1, 1}, {2, 1}, {3, 1}}, new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}}, 4);
	}
	
	//checks node for solution and sets next node to expand
	public void searchNode(ItNode node)
	{
		//check if node's status = goal state
		if(Arrays.equals(node.state[0], this.goalState[0]) && Arrays.equals(node.state[1], this.goalState[1])
				&& Arrays.equals(node.state[2], this.goalState[2]) && !this.goingBackUp)
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
			//if no children for node, or node depth exceeds algorithm iteration max depth
			if(node.children.size() == 0 || node.depth >= this.maxDepth)
			{
				//if no parent then no solution exists for current iterative depth
				if(node.parent == null)
				{
					this.notfound = true;
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
			//node has children and is within iteration's max depth; select "left"-most child
			else
			{
				this.current = node.children.get(0);
				this.goingBackUp = false;
			}
		}
	}
	
	//custom node class for algorithm
	class ItNode
	{
		String actions;
		int depth;
		ItNode parent;
		int[][] state;
		ArrayList<ItNode> children;
		
		//constructor takes parent node, tilegrid state, and string of actions to state
		public ItNode(ItNode parent, String action, int[][] state)
		{
			this.parent = parent;
			this.actions = parent.actions + "," + action;
			this.depth = parent.depth + 1;
			this.state = state;
			this.children = null;
		}
		
		//default generates head node with start state
		public ItNode(int[][] startState)
		{
			this.parent = null;
			this.actions = "";
			this.depth = 0;
			this.state = startState;
			this.children = null;
		}
		
		//method to populate array with children of states resulting from valid moves
		public void getChildren()
		{
			this.children = new ArrayList<ItNode>();
			for(String direction : new String[] {"LEFT", "RIGHT", "UP", "DOWN"})
			{
				//for each possible action, add to children array with node with state of said action
				int[][] newState = IterativeDeepening.this.tilegrid.moveState(direction, this.state);
				if(newState != null)
				{
					this.children.add(new ItNode(this, direction, newState));
				}
			}
		}
	}
	
	//run iterative deepening
	public static void main(String[] args)
	{
		new IterativeDeepening();
	}
}