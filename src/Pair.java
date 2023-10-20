public class Pair {
	
	private Coordinate c1;
	private Coordinate c2;

	public Pair(Coordinate c1, Coordinate c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public boolean equals(Object o) {
		Pair other = (Pair) o;
		return c1.equals(other.c1) && c2.equals(other.c2);
	}

	public Coordinate getC1() {
		return c1;
	}

	public Coordinate getC2() {
		return c2;
	}
}
