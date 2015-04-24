import java.util.ArrayList;
import java.util.Arrays;

public class AdjacencyList {
	
	private int numNodes;
	private AdjacencyNode[] lists;

	public AdjacencyList(int num) {
		numNodes = num;
		lists = new AdjacencyNode[numNodes];
	}

	public boolean addEdge(int a, int b, double weight) {
		if (a > lists.length || b > lists.length)
			return false;

		lists[a] = new AdjacencyNode(a, b, weight, lists[a]);
		lists[b] = new AdjacencyNode(b, a, weight, lists[b]);

		return true;
	}

	public boolean removeEdge(int a, int b) {
		if (a > numNodes || b > numNodes)
			return false;

		AdjacencyNode node = lists[a];
		AdjacencyNode prevNode = null;
		boolean found = false;

		while (node != null && !found) {
			if (node.getDest() == b) {
				found = true;
				if (prevNode == null) 
					lists[a] = node.getNext();
				else 
					prevNode.setNext(node.getNext());
			}
			prevNode = node;
			node = node.getNext();
		}

		if (found) {
			node = lists[b];
			prevNode = null;
			while (node != null && !found) {
				if (node.getDest() == a) {
					found = true;
					if (prevNode == null)
						lists[b] = node.getNext();
					else 
						prevNode.setNext(node.getNext());
				}
				prevNode = node;
				node = node.getNext();
			}
		}

		return found;
	}

	public AdjacencyNode getShortestWeighted(int start, int dest) {
		if (start > numNodes || dest > numNodes)
			return null;

		double[] distances = new double[numNodes];
		int[] shortest = new int[numNodes];
		boolean[] visited = new boolean[numNodes];

		int current = start;
		double distance;

		for (int i = 0; i < numNodes; i++) {
			distances[i] = -1;	
			shortest[i] = -1;		
			visited[i] = false;
		}

		distances[start] = 0;
		while (!visited[dest]) {
			AdjacencyNode node = getList(current);

			while (node != null) {
				if (!visited[node.getDest()]) {
					distance = distances[current] + node.getWeight();
					if (distances[node.getDest()] == -1 || distances[node.getDest()] > distance) {
						distances[node.getDest()] = distance;
						shortest[node.getDest()] = current;
					}	
				}
				node = node.getNext();
			}

			visited[current] = true;

			current = -1;
			for (int i = 0; i < numNodes; i++) 
				if (!visited[i] && distances[i] != -1 && 
					(current == -1 || distances[i] < distances[current])) 
						current = i;

			if (current == -1 && !visited[dest])
				return null;
		}

		int last = dest;
		AdjacencyNode head = null;

		while (shortest[last] != -1) {
			head = new AdjacencyNode(shortest[last], last, getWeight(shortest[last], last), head);
			last = shortest[last];
		}

		return head;
	}

	public AdjacencyNode getShortestUnweighted(int start, int dest) {
		if (start > numNodes || dest > numNodes)
			return null;

		int[] distances = new int[numNodes];
		int[] shortest = new int[numNodes];
		boolean[] visited = new boolean[numNodes];

		int current = start;
		int distance;

		for (int i = 0; i < numNodes; i++) {
			distances[i] = -1;	
			shortest[i] = -1;		
			visited[i] = false;
		}

		distances[start] = 0;
		while (!visited[dest]) {
			AdjacencyNode node = getList(current);

			while (node != null) {
				if (!visited[node.getDest()]) {
					distance = distances[current] + 1;
					if (distances[node.getDest()] == -1 || distances[node.getDest()] > distance) {
						distances[node.getDest()] = distance;
						shortest[node.getDest()] = current;
					}	
				}
				node = node.getNext();
			}

			visited[current] = true;

			current = -1;
			for (int i = 0; i < numNodes; i++) 
				if (!visited[i] && distances[i] != -1 && 
					(current == -1 || distances[i] < distances[current])) 
						current = i;

			if (current == -1 && !visited[dest])
				return null;
		}

		int last = dest;
		AdjacencyNode head = null;

		while (shortest[last] != -1) {
			head = new AdjacencyNode(shortest[last], last, 0, head);
			last = shortest[last];
		}

		return head;
	}

	public ArrayList<AdjacencyNode> weighLessThan(double weight) {
		ArrayList<AdjacencyNode> result = new ArrayList<AdjacencyNode>();
		for (int i = 0; i < numNodes; i++) {
			boolean[] visited = new boolean[numNodes];
			visited[i] = true;
			result.addAll(weighLessThan(weight, i, visited));
		}
			
		return result;
	}

	private ArrayList<AdjacencyNode> weighLessThan(double weight, int current, boolean[] visited) {
		ArrayList<AdjacencyNode> result = new ArrayList<AdjacencyNode>();
		AdjacencyNode node = getList(current);
		while (node != null) {
			if (node.getWeight() < weight && !visited[node.getDest()]) {
				boolean[] temp = Arrays.copyOf(visited, numNodes);
				temp[node.getDest()] = true;
				result.addAll(weighLessThan(weight - node.getWeight(), node.getDest(), temp));
				for (int i = 0; i < result.size(); i++) 
					result.set(i, new AdjacencyNode(node.getSource(), node.getDest(), 
						node.getWeight(), result.get(i)));
				
				result.add(node);

			}
			node = node.getNext();
		} 		

		return result;
	}

	public static ArrayList<AdjacencyList> getMinSpan(AdjacencyList list) {
		ArrayList<AdjacencyList> resultList = new ArrayList<AdjacencyList>();

		AdjacencyList result = new AdjacencyList(list.numNodes);
		AdjacencyList original = list.copyOf();
		boolean[] included = new boolean[list.numNodes];
		int numIncluded = 0;

		for (int i = 0; i < list.numNodes; i++) {
			if (list.lists[i] != null) {
				included[i] = true;
				numIncluded = 1;
				break;
			}
		}

		if (numIncluded == 0)
			return resultList;

		while (numIncluded < list.numNodes) {
			AdjacencyNode bestEdge = null;

			for (int i = 0; i < list.numNodes; i++) {
				if (included[i]) {
					AdjacencyNode node = original.getList(i);

					while (node != null) {
						if (!included[node.getDest()] && 
							(bestEdge == null || node.getWeight() < bestEdge.getWeight()))
							bestEdge = node;
						node = node.getNext();
					}
				}
			}


			if (bestEdge == null) {
				resultList.add(result);
				return resultList;
			}

			result.addEdge(bestEdge.getSource(), bestEdge.getDest(), bestEdge.getWeight());
			original.removeEdge(bestEdge.getSource(), bestEdge.getDest());
			included[bestEdge.getDest()] = true;
			numIncluded++;
		}

		resultList.add(result);
		return resultList;
	}



	public double getWeight(int a, int b) {
		AdjacencyNode node = lists[a];
		while (node != null) {
			if (node.getDest() == b)
				return node.getWeight();
			node = node.getNext();
		}

		return -1;
	}

	public String toString() {
		String result = "";
		AdjacencyNode nxt = null;

		for (int i = 0; i < lists.length; i++) {
			result += i + " ~ ";
			nxt = lists[i];
			while (nxt != null) {
				result += " (" + nxt.getDest() + ", " + nxt.getWeight() + ") ";
				nxt = nxt.getNext();
			}
			result += "\n";
		}

		return result;
	}

	public AdjacencyList copyOf() {
		AdjacencyList copy = new AdjacencyList(numNodes);
		for (int i = 0; i < numNodes; i++) 
			copy.lists[i] = lists[i].copyOf();

		return copy;
	}

	public AdjacencyNode getList(int num) { return lists[num]; }
	public int getNumNodes() { return numNodes;	}
}