import java.util.Enumeration;

public class puzzle {

	private boolean [] pegs;
	private Board board;


	public puzzle(Board b) {
		pegs = new boolean [b.numberOfSquares()];
		board = b;
	}

	public boolean containsPeg (int square) throws NotOnBoardException {
		if (square < 1 || square > pegs.length) {
			throw new NotOnBoardException ();
		}
		
		return pegs[square - 1];
	}

	public void addPeg(int square) throws NotOnBoardException {
		if (square < 1 || square > pegs.length) {
			throw new NotOnBoardException ();
		}
		
		pegs[square - 1] = true;
	}
		

	public void removePeg(int square) throws NotOnBoardException {
		if (square < 1 || square > pegs.length) {
			throw new NotOnBoardException ();
		}
		
		pegs[square - 1] = false;
	}		

	public int numPegs() {
		int count = 0;

		for (int i = 0; i < pegs.length; i++) {
			if (pegs[i])
				count++;
		}

		return count;
	}


	public void executeJump (Jump jump) {
		try {
			removePeg (jump.getStart ());
			removePeg (jump.getHop ());
			addPeg (jump.getLand ());					
		} catch (NotOnBoardException nbe) {
			Assert.error ("BUG: jump is not on the board");
		}
	}

	public void reverseJump (Jump jump) {
		try {
			addPeg (jump.getStart ());
			addPeg (jump.getHop ());
			removePeg (jump.getLand ());					
		} catch (NotOnBoardException nbe) {
			Assert.error ("BUG: jump is not on the board");
		}
	}


	private boolean legalJump (Jump jump) {
		try {
			return (containsPeg (jump.getStart ())
					&& containsPeg (jump.getHop ())
					&& !containsPeg (jump.getLand ()));					
		} catch (NotOnBoardException nbe) {
			Assert.error ("BUG: jump is not on the board");
			return false;
		}
	}

	public JumpList possibleJumps() {
		JumpList res = new JumpList();

		for (int i = 0; i < pegs.length; i++) {
			if (pegs[i]) {

				int startsquare = i + 1;

				for (Enumeration dirs = Direction.allDirections();
					dirs.hasMoreElements();
					) {
					Direction d = (Direction) dirs.nextElement(); 
			
					try {
						int middlesquare = board.getSquare(startsquare, d, 1);
						int endsquare = board.getSquare(startsquare, d, 2);
						Jump jump =
							new Jump(startsquare, middlesquare, endsquare);
						if (legalJump(jump)) {
							res.add(jump);
						}
					} catch (NotOnBoardException nbe) {

					}

				}

			}
		}
		return res;
	}
}
