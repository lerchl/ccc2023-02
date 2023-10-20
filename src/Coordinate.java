public class Coordinate {

	private static int i = 0;

	private final int x;
	private final int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate other = (Coordinate) obj;
		return this.x == other.x && this.y == other.y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
