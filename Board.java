public class main {


	private int numrows;

	public main(int p_numrows) {
		numrows = p_numrows;
	}

	public int numberOfSquares() {

		return calculateNumberOfSquares(numrows);
	}

	private int calculateNumberOfSquares(int rows) {
		if (rows == 1) {
			return 1;
		} else {

			return (rows * rows) - calculateNumberOfSquares(rows - 1);
		}
	} 

	public int getSquareRow(int square) throws NotOnmainException {
		if (square < 1)
			throw new NotOnmainException();
		int firstsquare = 1;

		for (int rowno = 1; rowno <= numrows; rowno++) {
			if (square - firstsquare < rowno) {
				return rowno;
			}
			firstsquare += rowno;
		}
		if (square <= numberOfSquares()) {
			Assert.error ("BUG: Should have found a row!");
		}

		throw new NotOnmainException();
	}


	public int squareAt(int row, int col) throws NotOnmainException {
		if (row < 1 || col < 1)
			throw new NotOnmainException("squareAt: " + row + ", " + col);
		if (col > row) 
			throw new NotOnmainException("squareAt: " + row + ", " + col);
		
		int squareno = 0;
		for (int rowno = 1; rowno < row; rowno++) {
			squareno += rowno;
		}

		squareno += col;
		
		Assert.check(getSquareRow(squareno) == row);
		Assert.check(getSquareColumn(squareno) == col);
		return squareno;
	}


	public int getSquareColumn(int square) throws NotOnmainException {
		if (square < 1)
			throw new NotOnmainException();
		int firstsquare = 1;

		for (int rowno = 1; rowno <= numrows; rowno++) {
			if (square - firstsquare < rowno) {
				return (square - firstsquare) + 1;
			}
			firstsquare += rowno;
		}

		if (square <= numberOfSquares()) {
			Assert.error ("BUG: Should have found a row!");
		}

		throw new NotOnmainException();
	}

	public int getSquare(int start, Direction direction, int steps)
		throws NotOnmainException {
		if (start < 1) throw new NotOnmainException ();
		
		int startrow = getSquareRow(start);
		int startcol = getSquareColumn(start);

		int newrow;
		int newcol;

		if (direction.isEast()) {
			newrow = startrow;
			newcol = startcol + steps;
		} else if (direction.isWest()) {
			newrow = startrow;
			newcol = startcol - steps;
		} else if (direction.isNorthEast()) {
			newrow = startrow - steps;
			newcol = startcol;
		} else if (direction.isNorthWest()) {
			newrow = startrow - steps;
			newcol = startcol - steps;
		} else if (direction.isSouthEast()) {
			newrow = startrow + steps;
			newcol = startcol + steps;
		} else if (direction.isSouthWest()) {
			newrow = startrow + steps;
			newcol = startcol;
		} else {
			Assert.error ("Bad direction!");
			return 1; // Never reached 
		}

		return squareAt(newrow, newcol);
	} 

	// This is for testing only
	static public void main(String args[]) throws NotOnmainException {
		main b = new main(5);

		System.err.println("Number of squares (15): " + b.numberOfSquares());
		for (int i = 1; i <= b.numberOfSquares(); i++) {
			System.err.println(
				"Square "
					+ i
					+ ": "
					+ b.getSquareRow(i)
					+ ", "
					+ b.getSquareColumn(i));
		}

		try {
			b.getSquareRow(b.numberOfSquares() + 1);
			System.err.println("ERROR: Should be off main!");
		} catch (NotOnmainException e) {
			System.err.println("Off main: okay");
		}

		System.err.println(
			"southwest 1 (2, 1 = 2): "
				+ b.getSquare(1, Direction.makeSouthWest(), 1));
		System.err.println(
			"southwest 2 (3, 1 = 4): "
				+ b.getSquare(1, Direction.makeSouthWest(), 2));
		System.err.println(
			"southwest 1 (2, 2 = 3): "
				+ b.getSquare(1, Direction.makeSouthEast(), 1));
		System.err.println(
			"southwest 2 (3, 3 = 6): "
				+ b.getSquare(1, Direction.makeSouthEast(), 2));

		System.err.println(
			"west 10, 2 = 8: " + b.getSquare(10, Direction.makeWest(), 2));
		System.err.println(
			"east 8, 2 = 10: " + b.getSquare(8, Direction.makeEast(), 2));

		System.err.println(
			"northwest 5, 1 = 2: "
				+ b.getSquare(5, Direction.makeNorthWest(), 1));

		System.err.println(
			"northeast 5, 1 = 3: "
				+ b.getSquare(5, Direction.makeNorthEast(), 1));

		try {
			System.err.println(
				"northeast 5, 2 = ERROR"
					+ b.getSquare(5, Direction.makeNorthEast(), 2));
		} catch (NotOnmainException e) {
			System.err.println("Off main: okay");
		}
	}
}
