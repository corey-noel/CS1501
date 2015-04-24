import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class Airline {
	
	private static int numCities;
	private static AdjacencyList prices;
	private static AdjacencyList distances;
	private static String[] names;
	private static String fileName = null;
	private static boolean running = true;

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.print("Welcome to the flight control console.\n"); 
		System.out.print("Please input a file to be read from: ");

		while (fileName == null) {
			fileName = kb.nextLine();

			if (fileName.equals("q")) {
				System.out.println("Quitting...");
				System.exit(0);
			}

			if (!loadFromFile(fileName)) {
				fileName = null;
				System.out.print("Please input a file to be read from: ");
			}
		}


		System.out.printf("\nRead in from file %s.\n\n", fileName);
		
		boolean running = true;
		String input = "";
		int choice;
		while (running) {
			System.out.println("(1) Show all");
			System.out.println("(2) Display minimum-spanning tree");
			System.out.println("(3) Shortest path (miles)...");
			System.out.println("(4) Shortest path (cost)...");
			System.out.println("(5) Shortest path (steps)...");
			System.out.println("(6) Costs less than...");
			System.out.println("(7) Add route...");
			System.out.println("(8) Remove route...");
			System.out.println("(9) Quit/Save");
			System.out.print("\nWhat would you like to do? ");



			input = kb.nextLine();
			choice = -1;
			try {
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {}

			System.out.println();

			if (choice == 1) {
				System.out.println("Showing all routes...");
				showAllRoutes();


			} else if (choice == 2) {
				System.out.println("Showing the minimum-spanning tree...");
				showMinSpan();


			} else if (choice == 3) {
				int start = -1, dest = -1;

				System.out.println("Showing shortest path (miles)...");

				while (start == -1) {
					System.out.print("\tStarting city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							start = i;
					}

					if (start == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				while (dest == -1) {
					System.out.print("\tDestination city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							dest = i;
					}

					if (dest == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				shortestMiles(start, dest);


			} else if (choice == 4) {
				int start = -1, dest = -1;

				System.out.println("Showing cheapest path (cost)...");

				while (start == -1) {
					System.out.print("\tStarting city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							start = i;
					}

					if (start == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				while (dest == -1) {
					System.out.print("\tDestination city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							dest = i;
					}

					if (dest == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				shortestPrice(start, dest);


			} else if (choice == 5) {
				int start = -1, dest = -1;

				System.out.println("Showing shortest path (steps)...");

				while (start == -1) {
					System.out.print("\tStarting city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							start = i;
					}

					if (start == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				while (dest == -1) {
					System.out.print("\tDestination city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							dest = i;
					}

					if (dest == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				shortestSteps(start, dest);
			} else if (choice == 6) {
				double maxCost = -1;

				System.out.println("Showing all paths costing less than...");

				while (maxCost == -1) {
					System.out.print("\tMax cost: ");
					input = kb.nextLine().trim();
					try {
						maxCost = Double.parseDouble(input);
					} catch (NumberFormatException e) {}

					if (maxCost <= 0) {
						System.out.println("\tNot a valid max cost.");
					}
				}

				costsLessThan(maxCost);


			} else if (choice == 7) {
				int start = -1;
				int dest = -1;
				double price = -1;
				double distance = -1;

				System.out.println("Adding route...");

				while (start == -1) {
					System.out.print("\tStarting city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							start = i;
					}

					if (start == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				while (dest == -1) {
					System.out.print("\tDestination city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							dest = i;
					}

					if (dest == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				while (price == -1) {
					System.out.print("\tPrice: ");
					input = kb.nextLine().trim();
					try {
						price = Double.parseDouble(input);
					} catch (NumberFormatException e) {}

					if (price <= 0) {
						System.out.println("\tNot a valid price.");
					}
				}

				while (distance == -1) {
					System.out.print("\tDistance: ");
					input = kb.nextLine().trim();
					try {
						distance = Double.parseDouble(input);
					} catch (NumberFormatException e) {}

					if (distance <= 0) {
						System.out.println("\tNot a valid price.");
					}
				}

				if (prices.addEdge(start, dest, price)) {
					distances.addEdge(start, dest, distance);
					System.out.printf("Route added from %s to %s (%d miles, $%.2f)\n", 
						names[start],
						names[dest],
						(int)distance,
						price);
				} else {
					System.out.println("Route not added.");
				}


			} else if (choice == 8) {
				int start = -1;
				int dest = -1;

				System.out.println("Removing route...");

				while (start == -1) {
					System.out.print("\tStarting city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							start = i;
					}

					if (start == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				while (dest == -1) {
					System.out.print("\tDestination city: ");
					input = kb.nextLine().trim();
					for (int i = 0; i < names.length; i++) {
						if (names[i].equals(input))
							dest = i;
					}

					if (dest == -1) {
						System.out.printf("\tCould not find city %s\n", input);
					}
				}

				if (prices.removeEdge(start, dest)) {
					distances.removeEdge(start, dest);
					System.out.printf("Route removed from %s to %s.\n",
						names[start],
						names[dest]);
				} else {
					System.out.println("Route not removed.");
				}

			} else if (choice == 9) {

				System.out.printf("Saving information to %s...\n", fileName);
				try {
					FileWriter output = new FileWriter(new File(fileName), false);

					output.write(numCities + "\n");
					for (int i = 0; i < numCities; i++) {
						output.write(names[i] + "\n");
					}
					
					AdjacencyNode node = null;
					for (int i = 0; i < numCities; i++) {
						node = prices.getList(i);
						while (node != null) {
							if (node.getSource() > node.getDest()) {
								output.write(String.format("%d %d %d %.2f\n",
									node.getSource() + 1,
									node.getDest() + 1,
									(int)distances.getWeight(node.getSource(), node.getDest()),
									node.getWeight()
								));
							}

							node = node.getNext();
						}
					}

					output.close();
					running = false;
				} catch (IOException e) {
					System.out.printf("Could not save to %s!", fileName);
				}

			} else {
				System.out.printf("I don't know what %s means! Please try again.\n", input);
			}
		}


		System.out.println("Quitting...");
	}

	public static boolean loadFromFile(String fileName) {
		Scanner sc = null;

		try {
			sc = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.err.printf("File %s not found!\n", fileName);
			return false;
		}

		try {
			numCities = sc.nextInt();
			sc.nextLine();
			names = new String[numCities];
			prices = new AdjacencyList(numCities);
			distances = new AdjacencyList(numCities);

			for (int i = 0; i < numCities; i++)
				names[i] = sc.nextLine();

			int a = 0; 
			int b = 0;

			while (sc.hasNext()) {
				a = sc.nextInt() - 1;
				b = sc.nextInt() - 1;

				distances.addEdge(a, b, sc.nextInt());
				prices.addEdge(a, b, sc.nextDouble());
			}

		} catch(NoSuchElementException e) {
			System.err.printf("File %s is not formatted properly!\n", fileName);
			return false;
		}

		return true;
	}

	public static void showAllRoutes() {
		for (int i = 0; i < names.length; i++) {
			AdjacencyNode priceNode = prices.getList(i);
			AdjacencyNode distNode = distances.getList(i);

			System.out.printf("Flights from %s...\n", names[i]);
			while (priceNode != null) {
				System.out.printf("\t...to %s: $%.2f, %d miles\n", 
					names[priceNode.getDest()], priceNode.getWeight(), (int)distNode.getWeight());

				priceNode = priceNode.getNext();
				distNode = distNode.getNext();
			}
			System.out.println();
		}
	}

	public static void showMinSpan() {
		ArrayList<AdjacencyList> result = AdjacencyList.getMinSpan(distances);

		if (result.size() == 0) {
			System.out.println("No minimum-spanning graph found.\n");
		} else {
			if (result.size() == 1)
				System.out.println("Minimum-spanning graph: ");

			else 
				System.out.println("Minimum-spanning forest: ");

			for (int i = 0; i < result.size(); i++) {
				AdjacencyList resultList = result.get(i);
				for (int j = 0; j < resultList.getNumNodes(); j++) {
					AdjacencyNode node = resultList.getList(j);

					while (node != null) {
						if (node.getDest() < node.getSource()) {
							System.out.printf("\t%s to %s (%d miles, $%.2f)\n", 
								names[node.getSource()],
								names[node.getDest()], 
								(int)distances.getWeight(node.getSource(), node.getDest()),
								prices.getWeight(node.getSource(), node.getDest())); 
						}

						node = node.getNext();
					}
				}
				System.out.println();
			}
		}
	}

	public static void shortestMiles(int start, int dest) {
		AdjacencyNode result = distances.getShortestWeighted(start, dest);

		if (result == null) {
			System.out.printf("No path found from %s to %s.\n", names[start], names[dest]);
			return;
		}

		double totalDist = 0;
		double totalPrice = 0;

		String out = "Shortest path (distance) from " + names[start] + " to " 
			+ names[dest] + " (%d miles, $%.2f): \n";
		out += String.format("From %s...\n", names[start]);
		while (result != null) {
			double price = prices.getWeight(result.getSource(), result.getDest());
			totalDist += result.getWeight();
			totalPrice += price;
			out += String.format("\t...to %s (%d miles, $%.2f)\n", 
				names[result.getDest()],	
				(int) result.getWeight(),
				price);

			result = result.getNext();
		}

		System.out.printf(out, (int)totalDist, totalPrice);
		System.out.println();
	}

	public static void shortestPrice(int start, int dest) {
		AdjacencyNode result = prices.getShortestWeighted(start, dest);

		if (result == null) {
			System.out.printf("No path found from %s to %s.\n", names[start], names[dest]);
			return;
		}

		double totalDist = 0;
		double totalPrice = 0;

		String out = "Cheapest path (cost) from " + names[start] + " to " 
			+ names[dest] + " (%d miles, $%.2f): \n";
		out += String.format("From %s...\n", names[start]);
		while (result != null) {
			double distance = distances.getWeight(result.getSource(), result.getDest());
			totalDist += distance;
			totalPrice += result.getWeight();
			out += String.format("\t...to %s (%d miles, $%.2f)\n", 
				names[result.getDest()],	
				(int) distance,
				result.getWeight());

			result = result.getNext();
		}

		System.out.printf(out, (int)totalDist, totalPrice);
		System.out.println();
	}

	public static void shortestSteps(int start, int dest) {
		AdjacencyNode result = prices.getShortestUnweighted(start, dest);

		if (result == null) {
			System.out.printf("No path found from %s to %s.\n", names[start], names[dest]);
			return;
		}

		double totalDist = 0;
		double totalPrice = 0;

		String out = "Shortest path (steps) from " + names[start] + " to " 
			+ names[dest] + " (%d miles, $%.2f): \n";
		out += String.format("From %s...\n", names[start]);
		while (result != null) {
			double distance = distances.getWeight(result.getSource(), result.getDest());
			double price = prices.getWeight(result.getSource(), result.getDest());
			totalDist += distance;
			totalPrice += price;
			out += String.format("\t...to %s (%d miles, $%.2f)\n", 
				names[result.getDest()],	
				(int) distance,
				price);

			result = result.getNext();
		}

		System.out.printf(out, (int)totalDist, totalPrice);
		System.out.println();
	}

	public static void costsLessThan(double cost) {
		ArrayList<AdjacencyNode> result = prices.weighLessThan(cost);
		System.out.printf("All routes costing less than $%.2f...\n", cost);

		for (AdjacencyNode n : result) {
			AdjacencyNode node = n;
			int src = node.getSource();
			int dest = -1;
			double totalMiles = 0;
			double totalPrice = 0;
			double miles = 0; 
			double price = 0;
			String out = "\tFrom %s to %s (%d miles, $%.2f)...\n\tFrom %s...\n";

			while (node != null) {
				dest = node.getDest();
				miles = distances.getWeight(node.getSource(), node.getDest());
				price = prices.getWeight(node.getSource(), node.getDest());
				totalMiles += miles;
				totalPrice += price;

				out += String.format("\t\t...to %s (%d miles, $%.2f)\n", names[dest], (int)miles, price);

				node = node.getNext();
			}

			out += "\n";
			if (totalPrice < cost)
				System.out.printf(out, names[src], names[dest], (int)totalMiles, totalPrice, names[src]);
		}
	}
}
