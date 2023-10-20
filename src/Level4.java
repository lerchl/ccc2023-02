import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
			var route = aStarSearch(pair.getC1().getX(), pair.getC1().getY(), pair.getC2().getX(), pair.getC2().getY());
			return route.stream().map(c -> c.getX() + "," + c.getY()).collect(Collectors.joining(" "));
		}).toList();
	}

	private int heuristic(int firstX, int firstY, int secondX, int secondY) {
		// Manhattan distance
		return Math.abs(firstX - secondX) + Math.abs(firstY - secondY);
	}

	private int hash(int x, int y) {
		return x >= y ? x * x + x + y : x + y * y;
	}

	private List<Coordinate> aStarSearch(int startX, int startY, int goalX, int goalY) {
		PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
		Map<Integer, Node> allNodes = new HashMap<>();
		Node startNode = new Node(startX, startY, null, 0, heuristic(startX, startY, goalX, goalY));

		openList.add(startNode);
		allNodes.put(hash(startX, startY), startNode);

		while (!openList.isEmpty()) {
			Node currentNode = openList.poll();

			if (currentNode.x == goalX && currentNode.y == goalY) {
				List<Coordinate> path = new ArrayList<>();
				while (currentNode != null) {
					path.add(new Coordinate(currentNode.x, currentNode.y));
					currentNode = currentNode.parent;
				}
				Collections.reverse(path);
				return path;
			}

			for (int[] neighborCoord : getNeighbors(currentNode.x, currentNode.y)) {
				int x = neighborCoord[0];
				int y = neighborCoord[1];

				if (
					x < 0 || x >= map[0].length ||
					y < 0 || y >= map.length ||
					map[y][x].equals("L")
				) {
					    continue;
				}

				Node neighbor = allNodes.getOrDefault(hash(goalX, goalY), new Node(x, y));
				int tentativeG = currentNode.g + 1; // All moves cost 1 for simplicity.

				if (tentativeG < neighbor.g) {
					neighbor.parent = currentNode;
					neighbor.g = tentativeG;
					neighbor.f = tentativeG + heuristic(neighbor.x, neighbor.y, goalX, goalY);
					if (!openList.contains(neighbor)) {
						openList.add(neighbor);
						allNodes.put(hash(neighbor.x, neighbor.y), neighbor);
					}
				}
			}
		}

		return Collections.emptyList(); // No path found.
	}


	private int[][] getNeighbors(int x, int y) {
		return new int[][] {
			{x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
			{x - 1, y},                 {x + 1, y},
			{x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}
		};
	}

	static class Node {
		int x, y;
		Node parent;
		int g, f;

		Node(int x, int y) {
			this(x, y, null, Integer.MAX_VALUE, Integer.MAX_VALUE);
		}

		Node(int x, int y, Node parent, int g, int h) {
			this.x = x;
			this.y = y;
			this.parent = parent;
			this.g = g;
			this.f = g + h;
		}
	}
}
