import java.util.Arrays;

public class BoardGeneration {
	// Starts the game with the standard chess board
	public static void initiateStandardChess() {
		// Initialize bitboards for each piece
		long WP = 0L, WN = 0L, WB = 0L, 
			 WR = 0L, WQ = 0L, WK = 0L, 
			 BP = 0L, BN = 0L, BB = 0L, 
			 BR = 0L, BQ = 0L, BK = 0L;
		// Initialize chess board in starting position
		String board[][] = {
			{"r", "n", "b", "q", "k", "b", "n", "r"},
			{"p", "p", "p", "p", "p", "p", "p", "p"},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{"P", "P", "P", "P", "P", "P", "P", "P"},
			{"R", "N", "B", "Q", "K", "B", "N", "R"}
		};
		// Convert the starting position to bitboards
		arrayToBitboards(board, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
	}
	// Starts the game with a random chess board
	public static void initiateChess960() {
		// Initialize bitboards for each piece
		long WP = 0L, WN = 0L, WB = 0L, 
			 WR = 0L, WQ = 0L, WK = 0L, 
			 BP = 0L, BN = 0L, BB = 0L, 
			 BR = 0L, BQ = 0L, BK = 0L;
		// Initialize chess board array (pawns only)
		String board[][] = {
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{"p", "p", "p", "p", "p", "p", "p", "p"},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{"P", "P", "P", "P", "P", "P", "P", "P"},
			{" ", " ", " ", " ", " ", " ", " ", " "}
		};
		// Randomly place one bishop on each side
		int bishop1 = (int)(Math.random() * 8);
		board[0][bishop1] = "b";
		board[7][bishop1] = "B";
		// Randomly place the other bishop on each side on opposite color squares
		int bishop2 = (int)(Math.random() * 8);
		while (bishop2 % 2 == bishop1 % 2) {
			bishop2 = (int)(Math.random() * 8);
		}
		board[0][bishop2] = "b";
		board[7][bishop2] = "B";
		// Randomly place the queens in an unfilled column
		int queen = (int)(Math.random() * 8);
		while (!board[0][queen].equals(" ")) {
			queen = (int)(Math.random() * 8);
		}
		board[0][queen] = "q";
		board[7][queen] = "Q";
		// Randomly place the knights in unfilled columns
		int knight1 = (int)(Math.random() * 8);
		while (!board[0][knight1].equals(" ")) {
			knight1 = (int)(Math.random() * 8);
		}
		board[0][knight1] = "n";
		board[7][knight1] = "N";
		int knight2 = (int)(Math.random() * 8);
		while (!board[0][knight2].equals(" ")) {
			knight2 = (int)(Math.random() * 8);
		}
		board[0][knight2] = "n";
		board[7][knight2] = "N";
		// Place the rooks and kings in the remaining squares
		// Follow order rook, king, rook to ensure legal castling
		int counter = 0;
		for (int i = 0; i < 8; i++) {
			if (board[0][i].equals(" ")) {
				switch (counter) {
					case 0 -> {
						board[0][i] = "r";
						board[7][i] = "R";
						counter++;
					} case 1 ->{
						board[0][i] = "k";
						board[7][i] = "K";
						counter++;
					} case 2 -> {
						board[0][i] = "r";
						board[7][i] = "R";
						counter++;
					}
				}
			}
		}
		// Convert the starting position to bitboards
		arrayToBitboards(board, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
	}
	// Converts the string array representation of the position to bitboards
	public static void arrayToBitboards(String[][] board, 
										long WP, long WN, long WB, 
										long WR, long WQ, long WK, 
										long BP, long BN, long BB, 
										long BR, long BQ, long BK) {
		String Binary;
		// Loop through each square on the board
		for (int i = 0; i < 64; i++) {
			// Initialize the binary string to all 0s
			Binary = "0000000000000000000000000000000000000000000000000000000000000000";
			// Set the ith bit to 1
			Binary = Binary.substring(i + 1) + "1" + Binary.substring(0, i);
			// Add the binary representation of the current square to the appropriate bitboard
			switch (board[i / 8][i % 8]) {
				case "P" -> WP += convertStringToBitboard(Binary);
				case "N" -> WN += convertStringToBitboard(Binary);
				case "B" -> WB += convertStringToBitboard(Binary);
				case "R" -> WR += convertStringToBitboard(Binary);
				case "Q" -> WQ += convertStringToBitboard(Binary);
				case "K" -> WK += convertStringToBitboard(Binary);
				case "p" -> BP += convertStringToBitboard(Binary);
				case "n" -> BN += convertStringToBitboard(Binary);
				case "b" -> BB += convertStringToBitboard(Binary);
				case "r" -> BR += convertStringToBitboard(Binary);
				case "q" -> BQ += convertStringToBitboard(Binary);
				case "k" -> BK += convertStringToBitboard(Binary);
			}
		}
		UserInterface.WP = WP; UserInterface.WN = WN; UserInterface.WB = WB;
		UserInterface.WR = WR; UserInterface.WQ = WQ; UserInterface.WK = WK;
		UserInterface.BP = BP; UserInterface.BN = BN; UserInterface.BB = BB;
		UserInterface.BR = BR; UserInterface.BQ = BQ; UserInterface.BK = BK;
		drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
	}
	// Converts a binary string to a long
	public static long convertStringToBitboard(String Binary) {
		if (Binary.charAt(0) == '0') { // number is positive
			// Convert the binary string to a long
			return Long.parseLong(Binary, 2);
		} else { // number is negative
			// Convert the two's complement to a positive number
			return Long.parseLong("1" + Binary.substring(2), 2) * 2;
		}
	}
	// Draws the board from the bitboards
	public static void drawArray(long WP, long WN, long WB,
															 long WR, long WQ, long WK,
															 long BP, long BN, long BB,
															 long BR, long BQ, long BK) {
		// Initialize an empty board array
		String board[][] = new String[8][8];
		// Loop through each square on the board
		for (int i = 0; i < 64; i++) {
			// Set each square to a space string
			board[i / 8][i % 8]=" ";
		}
		// Loop through each square on the board
		for (int i = 0; i < 64; i++) {
			// if the ith bit in a bitboard is 1, set the square to the corresponding piece
			if (((WP >> i) & 1) == 1) { board[i / 8][i % 8] = "P"; }
			else if (((WN >> i) & 1) == 1) { board[i / 8][i % 8] = "N"; }
			else if (((WB >> i) & 1) == 1) { board[i / 8][i % 8] = "B"; }
			else if (((WR >> i) & 1) == 1) { board[i / 8][i % 8] = "R"; }
			else if (((WQ >> i) & 1) == 1) { board[i / 8][i % 8] = "Q"; }
			else if (((WK >> i) & 1) == 1) { board[i / 8][i % 8] = "K"; }
			else if (((BP >> i) & 1) == 1) { board[i / 8][i % 8] = "p"; }
			else if (((BN >> i) & 1) == 1) { board[i / 8][i % 8] = "n"; }
			else if (((BB >> i) & 1) == 1) { board[i / 8][i % 8] = "b"; }
			else if (((BR >> i) & 1) == 1) { board[i / 8][i % 8] = "r"; }
			else if (((BQ >> i) & 1) == 1) { board[i / 8][i % 8] = "q"; }
			else if (((BK >> i) & 1) == 1) { board[i / 8][i % 8] = "k"; }
		}
		// Print the board
		for (int i = 0; i < 8; i++) {
			System.out.println(Arrays.toString(board[i]));
		}
	}
}