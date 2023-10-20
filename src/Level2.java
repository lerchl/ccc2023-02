import java.util.ArrayList;
import java.util.List;

public class Level2 extends Solver {

	private String[][] map;

	Level2() {
		super(2);
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
			return search(pair.getC1(), pair.getC2(), new ArrayList<>()) ? "SAME" : "DIFFERENT";
		}).toList();
	}

	private boolean search(Coordinate from, Coordinate to, List<Coordinate> visited) {
		if (
			// x out of bounds
			from.getX() < 0 || from.getX() >= map[0].length ||
			// or y out of bounds
			from.getY() < 0 || from.getY() >= map.length
		) {
			return false;
		}

		if (visited.contains(from)) {
			return false;
		}

		visited.add(from);

		if (map[from.getY()][from.getX()].equals("W")) {
			return false;
		}

		if (from.getX() == to.getX() && from.getY() == to.getY()) {
			return true;
		}

		return search(new Coordinate(from.getX() + 1, from.getY()), to, visited) ||
				search(new Coordinate(from.getX() - 1, from.getY()), to, visited) ||
				search(new Coordinate(from.getX(), from.getY() + 1), to, visited) ||
				search(new Coordinate(from.getX(), from.getY() - 1), to, visited);
	}
}
