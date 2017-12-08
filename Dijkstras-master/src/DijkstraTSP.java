import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Graph{
	
	//create variable to hold edge
	//create map contain City Name as string and List of edges as value
	List<Edge> e;
	Map<String, List<Edge>> g = new LinkedHashMap<>();
	
	//Lists to hold data needed to compute Dijkstras
	List<String> vertices;
	List<String> previous;
	List<Integer> distance;
	
	/*
	 * Edge class
	 * Each edge contains a weight w, connected city v, & boolean to see if edge is visited
	 * Once a new edge is created, the edge is added to the edge list for each city
	 */
	class Edge{
		int w;
		String v;
		boolean visited = false;
		
		public Edge(int w, String v){
			this.w = w;
			this.v = v;
			e.add(this);
		}

		public int getW() { return w; }

		public void setW(int w) { this.w = w; }

		public String getV() { return v; }

		public void setV(String v) { this.v = v; }
		
		public boolean getVisited() { return visited; }
		
		public void setVisited(boolean visited) { this.visited = visited; }
	}
		
	//Method called in main to read the file and create a graph
	//Filled in with flight data
	public void readFromFile(){
		
		try{
			//get input file and create input stream
		    FileInputStream fstream = new FileInputStream("graph.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		          
		    /*
		     * read each line from the file and place into String array for each token
		     * then place odd indices as weights and even indices as city names
		     */
		    while ((strLine = br.readLine()) != null)   {
		    	String[] tokens = strLine.split(" "); 
		        	e = new ArrayList<>();
			        for(int i = 1; i < tokens.length; i++){
			        	int weightFromFile = Integer.parseInt(tokens[i]);
			        	Edge e1 = new Edge(weightFromFile, tokens[i+1]);
			        	i++;
			        }
			        g.put(tokens[0], e);
		        }
		   
		    in.close();
			}catch (Exception e){
				
			}
	}
		
	/**
	 * @param endpt
	 * Implementation of Dijkstras Algorithm using lists
	 */
	void dijkstra(String endpt){
		
		//initiating data structures to hold values
		vertices = new ArrayList<>(g.keySet());
		previous = new ArrayList<>();
		distance = new ArrayList<>();
		
		boolean end = false;//To know when the end point has been reached
		
		
		//Initializing the graph to the amount of vertices
		for(int i=0; i < vertices.size(); i++){
			distance.add(Integer.MAX_VALUE);
			previous.add("");
		}
		//set first index distance to 0
		distance.set(0, 0);
		
		int counter = 0;
		String smallest = "";
		int indexToGet = -1;
		List<Edge> edgeList;
		int toAdd = 0;
		System.out.println("\t\t\t~~~FLIGHT PATH~~~");
		System.out.print("\n\nCheck-in:LAX");
		
		while(!end){
			
			smallest = findMinCurrent(endpt); //finds best fit node 
			
			if (smallest.equals(endpt)){
				end = true;
				break;
			}
			int indexOfSmallest = vertices.indexOf(smallest);
			int smallestDistance = distance.get(indexOfSmallest);
			vertices.remove(indexOfSmallest); // remove the smallest node from list of vertices
			
			toAdd = 0;
			
			edgeList = g.get(smallest);
			distance.remove(indexOfSmallest);//to ensure data for city is also removed since the node has been processed
			for(int i=0; i < edgeList.size(); i++){
				
				toAdd = smallestDistance + edgeList.get(i).w;
				
				String vertexAt = edgeList.get(i).v;
				
				if(vertices.contains(vertexAt) && !vertexAt.equals(endpt)) {	
					indexToGet = vertices.indexOf(vertexAt);
					
				}else if(vertexAt.equals(endpt)){
					end = true;
					break;
				}else {
					break;
				}
				if(toAdd < distance.get(indexToGet)){
					distance.set(indexToGet, toAdd);
					previous.set(counter, smallest);
				}
				
			}
			
			counter++;
			
		}
		System.out.println(" => "+ endpt);
		System.out.println("\n\n\t\t\tTotal Cost: $" + toAdd);
		System.out.println("======================================================================================================================");
		
	}
	
	//Function returning city with shortest path to be processed
	String findMinCurrent(String endpt){
		String city; 
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int i = 0; i < distance.size(); i++){			
			if(distance.get(i) < min){
				min = distance.get(i);
				minIndex = i;
			}
		}
		city = vertices.get(minIndex);
		System.out.print(" => " + city);
		return city;
	}
	
	
}

public class DijkstraTSP{
	public static void main(String[] args){
		Graph g = new Graph();
		g.readFromFile();
		Scanner s = new Scanner(System.in);
		System.out.println("~~~~~~~~~~~~~~~~~~~Traveling Salesperson~~~~~~~~~~~~~~~~~~~");
		System.out.print("\nPlease enter Destination Location from LAX:");
		String endpt = s.nextLine();
		System.out.println("\n======================================================================================================================");
		g.dijkstra(endpt);
		s.close();
		
	}
}