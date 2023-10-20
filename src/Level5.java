import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Assuming your Coordinate and Pair classes are defined elsewhere.

public class Level5 extends Solver {

	String[][] map;

	Level5() {
		super(5);
	}

	@Override
	protected List<String> solve(List<String> input) {
		int mapSize = Integer.parseInt(input.get(0));
		map = input.stream().skip(1).limit(mapSize).map(row -> row.split("")).toArray(String[][]::new);
		int coordinateCountIndex = mapSize + 1;

		List<Coordinate> coordinatePairs = input.stream().skip(coordinateCountIndex + 1).map(row -> {
			String[] coordinates = row.split(",");
			return new Coordinate(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
		}).toList();

		return coordinatePairs.stream().map(coord -> {
			int x = coord.getX();
			int y = coord.getY();
			var route = circle(x, y);
			return route.stream().map(c -> c.getX() + "," + c.getY()).collect(Collectors.joining(" "));
		}).toList();
	}

	String[][] copyMap(String[][] map) {
		String[][] copy = new String[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			System.arraycopy(map[i], 0, copy[i], 0, map[i].length);
		}
		return copy;
	}

	private List<Coordinate> circle(int startX, int startY) {
		// Define the directions to move
		int[][] DIRECTIONS = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
		int currentDir = 0; // Start by moving up

		int x = startX;
		int y = startY;

		// Find the initial sea tile by moving left
		while (map[y][x].equals("L")) {
			x--;
		}

		int startXSea = x, startYSea = y;

		List<Coordinate> path = new ArrayList<>();
		path.add(new Coordinate(x, y));

		while (true) {
			int nextDir = (currentDir + 1) % 4; // turn right
			int dx = DIRECTIONS[nextDir][0];
			int dy = DIRECTIONS[nextDir][1];

			// Check if the tile to the right is sea and move there
			if (!map[y + dy][x + dx].equals("L")) {
				x += dx;
				y += dy;
				currentDir = nextDir;
			} else {
				// Move forward
				x += DIRECTIONS[currentDir][0];
				y += DIRECTIONS[currentDir][1];
			}

			// If we hit the starting point, break
			if ((x == startXSea && y == startYSea) || path.size() > 2 * map[0].length) {
				break;
			}

			path.add(new Coordinate(x, y));
		}

		return path;
	}

	private int[][] getNeighbors(int x, int y) {
		return new int[][] {
				{ x - 1, y - 1 }, { x, y - 1 }, { x + 1, y - 1 },
				{ x - 1, y }, { x + 1, y },
				{ x - 1, y + 1 }, { x, y + 1 }, { x + 1, y + 1 }
		};
	}

	static class Node {
		int x, y;
		Node parent;
		int g, f;

		static int i = 0;

		Node(int x, int y) {
			this(x, y, null, 1000000, 1000000);
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
