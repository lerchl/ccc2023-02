import java.util.*;
import java.util.stream.Collectors;

// Assuming your Coordinate and Pair classes are defined elsewhere.

public class Level4 extends Solver {
	private String[][] map;

	Level4() {
		super(4);
	}

	@Override
	protected List<String> solve(List<String> input) {
		int mapSize = Integer.parseInt(input.get(0));
		map = input.stream().skip(1).limit(mapSize).map(row -> row.split("")).toArray(String[][]::new);
		int coordinateCountIndex = mapSize + 1;

		List<Pair> coordinatePairs = input.stream().skip(coordinateCountIndex + 1).map(row -> {
			String[] coordinates = row.split(" ");
			String[] stringCoordinate1 = coordinates[0].split(",");
			String[] stringCoordinate2 = coordinates[1].split(",");
			Coordinate coordinate1 = new Coordinate(Integer.parseInt(stringCoordinate1[0]), Integer.parseInt(stringCoordinate1[1]));
			Coordinate coordinate2 = new Coordinate(Integer.parseInt(stringCoordinate2[0]), Integer.parseInt(stringCoordinate2[1]));

			return new Pair(coordinate1, coordinate2);
		}).toList();

		return coordinatePairs.stream().map(pair -> {
			var route = aStarSearch(pair.getC1(), pair.getC2());
			return route.stream().map(c -> c.getX() + "," + c.getY()).collect(Collectors.joining(" "));
		}).toList();
	}

	private int heuristic(Coordinate a, Coordinate b) {
		// Manhattan distance
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}

	private List<Coordinate> aStarSearch(Coordinate start, Coordinate goal) {
		PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
		Map<Coordinate, Node> allNodes = new HashMap<>();
		Node startNode = new Node(start, null, 0, heuristic(start, goal));
		openList.add(startNode);
		allNodes.put(start, startNode);

		while (!openList.isEmpty()) {
			Node currentNode = openList.poll();

			if (currentNode.coordinate.equals(goal)) {
				List<Coordinate> path = new ArrayList<>();
				while (currentNode != null) {
					path.add(currentNode.coordinate);
					currentNode = currentNode.parent;
				}
				Collections.reverse(path);
				return path;
			}

			for (Coordinate neighborCoord : getNeighbors(currentNode.coordinate)) {
				if (neighborCoord.getX() < 0 || neighborCoord.getX() >= map[0].length ||
						neighborCoord.getY() < 0 || neighborCoord.getY() >= map.length ||
						map[neighborCoord.getY()][neighborCoord.getX()].equals("L")) {
					    continue; // Blocked by a wall.
				}

				Node neighbor = allNodes.getOrDefault(neighborCoord, new Node(neighborCoord));
				int tentativeG = currentNode.g + 1; // All moves cost 1 for simplicity.

				if (tentativeG < neighbor.g) {
					neighbor.parent = currentNode;
					neighbor.g = tentativeG;
					neighbor.f = tentativeG + heuristic(neighbor.coordinate, goal);
					if (!openList.contains(neighbor)) {
						openList.add(neighbor);
						allNodes.put(neighborCoord, neighbor);
					}
				}
			}
		}

		return Collections.emptyList(); // No path found.
	}

	private List<Coordinate> getNeighbors(Coordinate coordinate) {
		int x = coordinate.getX();
		int y = coordinate.getY();

		return Arrays.asList(
				new Coordinate(x + 1, y),
				new Coordinate(x - 1, y),
				new Coordinate(x, y + 1),
				new Coordinate(x, y - 1),
				new Coordinate(x + 1, y + 1),
				new Coordinate(x - 1, y + 1),
				new Coordinate(x + 1, y - 1),
				new Coordinate(x - 1, y - 1)
		);
	}

	static class Node {
		Coordinate coordinate;
		Node parent;
		int g, f;

		Node(Coordinate coordinate) {
			this(coordinate, null, Integer.MAX_VALUE, Integer.MAX_VALUE);
		}

		Node(Coordinate coordinate, Node parent, int g, int h) {
			this.coordinate = coordinate;
			this.parent = parent;
			this.g = g;
			this.f = g + h;
		}
	}
}
