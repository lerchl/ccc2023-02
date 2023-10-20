import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Level4 extends Solver {

	// first y then x
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
			var route = search(pair.getC1(), pair.getC2(), new ArrayList<>(), new ArrayList<>());
			return route.stream().map(c -> c.getX() + "," + c.getY()).collect(Collectors.joining(" "));
		}).toList();

		// List<List<Coordinate>> routes = input.stream().skip(coordinateCountIndex + 1).map(row -> {
		// 	String[] coordinates = row.split(" ");
		// 	return Arrays.stream(coordinates).map(stringC -> {
		// 		String[] stringCoordinate = stringC.split(",");
		// 		return new Coordinate(Integer.parseInt(stringCoordinate[0]), Integer.parseInt(stringCoordinate[1]));
		// 	}).toList();
		// }).toList();

		// return routes.stream().map(route -> {
		// 	if (route.stream().map(c -> Collections.frequency(route, c)).anyMatch(count -> count > 1)) {
		// 		return false;
		// 	}

		// 	List<Pair> wouldCross = new ArrayList<>();
		// 	for (int i = 0; i < route.size() - 1; i++) {
		// 		Coordinate c1 = route.get(i);
		// 		Coordinate c2 = route.get(i + 1);

		// 		if (wouldCross.contains(new Pair(c1, c2))) {
		// 			return false;
		// 		}

		// 		Coordinate swappedC1 = new Coordinate(c2.getX(), c1.getY());
		// 		Coordinate swappedC2 = new Coordinate(c1.getX(), c2.getY());

		// 		wouldCross.add(new Pair(swappedC1, swappedC2));
		// 		wouldCross.add(new Pair(swappedC2, swappedC1));
		// 	}

		// 	return true;
		// }).map(b -> b ? "VALID" : "INVALID").toList();
	}

	private List<Coordinate> search(Coordinate from, Coordinate to, List<Coordinate> visited, List<Coordinate> route) {
		if (
			// x out of bounds
			from.getX() < 0 || from.getX() >= map[0].length ||
			// or y out of bounds
			from.getY() < 0 || from.getY() >= map.length
		) {
			return null;
		}

		if (visited.contains(from)) {
			return null;
		}

		visited.add(from);

		if (map[from.getY()][from.getX()].equals("L")) {
			return null;
		}

		if (from.getX() == to.getX() && from.getY() == to.getY()) {
			return route;
		}

		List<Coordinate> right = search(new Coordinate(from.getX() + 1, from.getY()), to, visited, route);
		List<Coordinate> left = search(new Coordinate(from.getX() - 1, from.getY()), to, visited, route);
		List<Coordinate> down = search(new Coordinate(from.getX(), from.getY() + 1), to, visited, route);
		List<Coordinate> up = search(new Coordinate(from.getX(), from.getY() - 1), to, visited, route);
		List<Coordinate> rightDown = search(new Coordinate(from.getX() + 1, from.getY() + 1), to, visited, route);
		List<Coordinate> leftDown = search(new Coordinate(from.getX() - 1, from.getY() + 1), to, visited, route);
		List<Coordinate> rightUp = search(new Coordinate(from.getX() + 1, from.getY() - 1), to, visited, route);
		List<Coordinate> leftUp = search(new Coordinate(from.getX() - 1, from.getY() - 1), to, visited, route);

		List<Coordinate>[] lists = new List[] {right, left, down, up, rightDown, leftDown, rightUp, leftUp};
		return Arrays.stream(lists).filter(Objects::nonNull).sorted((l1, l2) -> l2.size() - l1.size()).findFirst().orElseThrow(() -> new RuntimeException("No route found"));
	}
}
