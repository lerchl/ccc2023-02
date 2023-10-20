import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Level3 extends Solver {

	// first y then x
	private String[][] map;

	Level3() {
		super(3);
	}

	@Override
	protected List<String> solve(List<String> input) {
		int mapSize = Integer.parseInt(input.get(0));
		map = input.stream().skip(1).limit(mapSize).map(row -> row.split("")).toArray(String[][]::new);

		int coordinateCountIndex = mapSize + 1;

		List<List<Coordinate>> routes = input.stream().skip(coordinateCountIndex + 1).map(row -> {
			String[] coordinates = row.split(" ");
			return Arrays.stream(coordinates).map(stringC -> {
				String[] stringCoordinate = stringC.split(",");
				return new Coordinate(Integer.parseInt(stringCoordinate[0]), Integer.parseInt(stringCoordinate[1]));
			}).toList();
		}).toList();

		return routes.stream().map(route -> {
			if (route.stream().map(c -> Collections.frequency(route, c)).anyMatch(count -> count > 1)) {
				return false;
			}

			List<Pair> wouldCross = new ArrayList<>();
			for (int i = 0; i < route.size() - 1; i++) {
				Coordinate c1 = route.get(i);
				Coordinate c2 = route.get(i + 1);

				if (wouldCross.contains(new Pair(c1, c2))) {
					return false;
				}

				Coordinate swappedC1 = new Coordinate(c2.getX(), c1.getY());
				Coordinate swappedC2 = new Coordinate(c1.getX(), c2.getY());

				wouldCross.add(new Pair(swappedC1, swappedC2));
				wouldCross.add(new Pair(swappedC2, swappedC1));
			}

			return true;
		}).map(b -> b ? "VALID" : "INVALID").toList();
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
