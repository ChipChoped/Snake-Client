package utils;

public class Position {

	private int x;
	private int y;


	public Position(int x, int y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	public Position(Position position) {
		this.x = position.x;
		this.y = position.y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Position)) {
			return false;
		}

		Position p = (Position) obj;

		return this.x == p.x && this.y == p.y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
