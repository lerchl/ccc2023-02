import java.util.List;

public class Level1 extends Solver {

	Level1() {
		super(1);
	}

	@Override
	protected List<String> solve(List<String> input) {
		int mapSize = Integer.parseInt(input.get(0));
		String[][] map = input.stream().skip(1).limit(mapSize).map(row -> row.split("")).toArray(String[][]::new);

		int coordinateCountIndex = mapSize + 1;

		return input.stream().skip(coordinateCountIndex + 1).map(row -> {
			var coordinate = row.split(",");
			return new Coordinate(Integer.parseInt(coordinate[0]), Integer.parseInt(coordinate[1]));
		}).map(c -> map[c.getY()][c.getX()]).toList();
	}
}
