import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

public class solution {

	static public JumpList solve(puzzle b) throws NoWinnerException {
		JumpList jumps = b.possibleJumps();

		for (Enumeration e = jumps.elements(); e.hasMoreElements();) {
			Jump j = (Jump) e.nextElement(); 

			b.executeJump(j);

			JumpList winningmoves;

			try {
				winningmoves = solve(b);
			} catch (NoWinnerException nwe) {
				continue; 
			} finally {
				b.reverseJump(j);


			winningmoves.insertJump(j);
			return winningmoves;
		}

		if (b.numPegs() == 1) {
		
			return new JumpList();
		} else {
			throw new NoWinnerException();
		}
	}

	static public void errorExit(String msg) {
		System.err.println(msg);
		System.exit(-1);
	}


	static public void main(String args[]) {
		if (args.length != 1) {
			errorExit("Usage: java solution <configuration file>");
			return;
		}

		String cfile = args[0];
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(cfile));
		} catch (FileNotFoundException e) {
			errorExit("Error: cannot open configuration file: " + cfile);
			return; 
		}

		String firstline;
		try {
			firstline = reader.readLine();
		} catch (IOException e1) {
			errorExit("Error: problem reading first line: " + e1);
			return;
		}
		int numrows = Integer.parseInt(firstline);

		if (numrows < 1) {
			errorExit("Error: the first line of the configuration file must be a number >= 1.");
			return;
		}

		Board b = new Board(numrows);
		puzzle bs = new puzzle(b);

		int rowno = 1;
		int pegno = 0;
		int inchar;

		try {
			while ((inchar = reader.read()) != -1) {
				char c = (char) inchar;

				if (c == ' '
					|| c == '\t'
					|| c == '\r') { 
				} else if (c == '\n') {
					rowno++;
				} else if (c == '*' || c == 'o') {
					pegno++;
					if (c == '*')
						bs.addPeg(pegno);
					if (b.getSquareRow(pegno) != rowno) {
						errorExit(
							"Row "
								+ rowno
								+ " in the configuration file "
								+ cfile
								+ " contains the wrong number of pegs.");
					}
				} else {
					System.err.println(
						"Skipping unrecognized character in configuration file: "
							+ c);
				}
			}
		} catch (IOException e2) {
			errorExit("Error: IO error reading file: " + e2);
		} catch (NotOnBoardException e2) {
			errorExit(
				"The configuration file contains too many pegs.  Read peg "
					+ pegno
					+ ".");
		}

		if (pegno != b.numberOfSquares()) {
			errorExit(
				"The configuration file does not contain enough information.  Only "
					+ pegno
					+ " peg or holes,  when "
					+ b.numberOfSquares()
					+ " is expected.");
		}

		System.out.println(
			"Read board containing " + bs.numPegs() + " pegs...");

		try {
			JumpList winning = solve(bs);
			System.out.println("Winning moves: " + winning);
		} catch (NoWinnerException nwe) {
			System.out.println("There is no winning sequence for the board.");
		}
	} 
}
