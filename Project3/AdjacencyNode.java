public class AdjacencyNode implements Comparable<AdjacencyNode> {
	private AdjacencyNode next;
	private int src;
	private int dest;
	private double wght;

	public AdjacencyNode(int source, int destination, double weight) {
		this(destination, source, weight, null);
	}

	public AdjacencyNode(int source, int destination, double weight, AdjacencyNode nxt) {
		src = source;
		dest = destination;
		wght = weight;
		next = nxt;
	}

	public int getSource() { return src; }
	public int getDest() { return dest; }
	public double getWeight() { return wght; }
	public AdjacencyNode getNext() { return next; }
	public void setNext(AdjacencyNode nxt) { next = nxt; }

	public void add(AdjacencyNode newNode) {
		if (next == null)
			next = newNode;
		else 
			next.add(newNode);
	}

	public AdjacencyNode copyOf() {
		if (next == null)
			return new AdjacencyNode(src, dest, wght);
		return new AdjacencyNode(src, dest, wght, next.copyOf());
	}

	public int compareTo(AdjacencyNode o) {
		if (this.wght > o.wght)
			return 1;
		if (o.wght > this.wght)
			return -1;
		return 0;
	}
}