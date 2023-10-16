import java.util.ArrayList;
import java.util.Arrays;

public class BFS 
{
	private long nodesExpanded;
	private int[][] goalState;
	private TileGrid tilegrid;
	private boolean found;
	private ArrayList<BFSNode> expanding, checking;

	//constructor takes goal state and start state and contains main loop
	public BFS(int[][] goalState, int[][] startState, int n)
	{	
		//initialise flags, tilegrid helper class, etc.
		this.found = false;
		this.nodesExpanded = 0;
		this.goalState = goalState;
		this.tilegrid = new TileGrid(n, startState);
		
		//create sliding window of nodes to check and nodes to expand
		this.checking = new ArrayList<BFSNode>();
		this.expanding = new ArrayList<BFSNode>();
		
		this.checking.add(new BFSNode(startState));
		
		//main algorithm loop
		while(!found)
		{	
			//evaluate all nodes in checking
			for (BFSNode node : this.checking)
			{
				this.checkNode(node);
				//no need to check other nodes if goalstate found as it will be optimal
				if(found)
				{
					break;
				}
			}
			
			//if no nodes contain goal state
			if (!found)
			{
				//place checked nodes in expanding array and reset checked nodes array
				this.expanding = this.checking;
				this.checking = new ArrayList<BFSNode>();
				
				//each node in expanding has its child nodes added to checking array
				for (BFSNode node : this.expanding)
				{
					node.getChildren();
					this.nodesExpanded++;
				}
			}
		}
	}
	
	//default uses goalstate and startstate from spec
	public BFS()
	{
		this(new int[][] {{1, 1}, {2, 1}, {3, 1}}, new int[][] {{3, 0}, {3, 1}, {3, 2}, {3, 3}}, 4);
	}
	
	//check if node's status = goal state
	public void checkNode(BFSNode node)
	{
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
	}
	
	//custom node class for algorithm
	class BFSNode 
	{
		String actions;
		int depth;
		int[][] state;
		
		//constructor only needs depth and actions up to creation of node
		public BFSNode(int depth, int[][] state, String actions)
		{
			this.actions = actions;
			this.depth = depth;
			this.state = state;
		}
		
		//default with start state
		public BFSNode(int[][] startState)
		{
			this.actions = "";
			this.depth = 0;
			this.state = startState;
		}
		
		//add node's children to BFS' checking arraylist
		public void getChildren()
		{
			//for each possible action
			for(String direction : new String[] {"LEFT", "RIGHT", "UP", "DOWN"})
			{
				int[][] newState = BFS.this.tilegrid.moveState(direction, this.state);
				if(newState != null)
				{
					BFS.this.checking.add(new BFSNode(this.depth + 1, newState, 
							this.actions + "," + direction));
				}
			}
		}
	}
	
	//run bfs
	public static void main(String[] args)
	{
		new BFS();
	}
}