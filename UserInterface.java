import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UserInterface extends JPanel {
    static long WP = 0L, WN = 0L, WB = 0L, 
				WR = 0L, WQ = 0L, WK = 0L,
				BP = 0L, BN = 0L, BB = 0L, 
				BR = 0L, BQ = 0L, BK = 0L;
    // static long UniversalWP = 0L, UniversalWN = 0L, UniversalWB = 0L, 
	// 			UniversalWR = 0L, UniversalWQ = 0L, UniversalWK = 0L,
	// 			UniversalBP = 0L, UniversalBN = 0L, UniversalBB = 0L, 
	// 			UniversalBR = 0L, UniversalBQ = 0L, UniversalBK = 0L;
    static int humanIsWhite = 1;
    // static int rating = 0;
    static int border = 10; // the amount of empty space around the frame
    static double squareSize = 64; // the size of a chess board square
    static JFrame frame = new JFrame("Chess Engine created by Josiah Voight"); // must be declared as static so that other classes can repaint
    static UserInterface javaUI = new UserInterface(); // must be declared as static so that other classes can repaint
	// Main method (initialize the javaUI)
    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(javaUI);
        frame.setSize(757, 570);
        frame.setLocation(
			(Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2,
            (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2
		);
        frame.setVisible(true);
        newGame();
        frame.repaint();
    }
	// Paints the javaUI
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                squareSize = (double)(Math.min(getHeight(), getWidth() - 200 - border) - 2 * border) / 8;
            }
        });
        drawBorders(g);
        drawBoard(g);
        drawPieces(g);
    }
	// Draws the chess board
    public void drawBoard(Graphics g) {
        for (int i = 0; i < 64 ; i += 2) {
			// Draw the light squares
            g.setColor(new Color(255, 200, 100));
            g.fillRect(
				(int)((i % 8 + (i / 8) % 2) * squareSize) + border, 
				(int)((i / 8) * squareSize) + border, 
				(int)squareSize, 
				(int)squareSize
			);
			// Draw the dark squares
            g.setColor(new Color(150, 50, 30));
            g.fillRect(
				(int)(((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize) + border, 
				(int)(((i+1)/8)*squareSize)+border, 
				(int)squareSize, 
				(int)squareSize);
        }
    }
	// Draws the chess pieces
    public void drawPieces(Graphics g) {
        // Get the chess pieces image
        BufferedImage chessPieceImage = getChessPieceImage();
        // Loop through each square on the board
		for (int i = 0; i < 64; i++) {
			// Determine the piece and color (if any)
            int j = -1, k = -1;
            if (((WP >> i) & 1) == 1) {j = 5; k = 1 - humanIsWhite;}
            else if (((BP >> i) & 1) == 1) { j = 5; k = humanIsWhite; }
            else if (((WB >> i) & 1) == 1) { j = 3; k = 1 - humanIsWhite; }
            else if (((BB >> i) & 1) == 1) { j = 3; k = humanIsWhite; }
            else if (((WN >> i) & 1) == 1) { j = 4; k = 1 - humanIsWhite; }
            else if (((BN >> i) & 1) == 1) { j = 4; k = humanIsWhite; }
            else if (((WQ >> i) & 1) == 1) { j = 1; k = 1 - humanIsWhite; }
            else if (((BQ >> i) & 1) == 1) { j = 1; k = humanIsWhite; }
            else if (((WR >> i) & 1) == 1) { j = 2; k = 1 - humanIsWhite; }
            else if (((BR >> i) & 1) == 1) { j = 2; k = humanIsWhite; }
            else if (((WK >> i) & 1) == 1) { j = 0; k = 1 - humanIsWhite; }
            else if (((BK >> i) & 1) == 1) { j = 0; k = humanIsWhite; }
			// Draw the piece if found
            if (j != -1 && k != -1) {
                g.drawImage(
					chessPieceImage,
                    (int)((i % 8) * squareSize) + border, 
					(int)((i / 8) * squareSize) + border, 
					(int)((i % 8 + 1) * squareSize) + border, 
					(int)((i / 8 + 1) * squareSize) + border, 
					j * 64, k * 64, (j + 1) * 64, (k + 1) * 64, null
				);
            }
        }
    }
	// Draws the borders of the chess board
    public void drawBorders(Graphics g) {
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, border, border, (int)(8 * squareSize), true);
        g.fill3DRect(
			(int)(8 * squareSize) + border, border, 
			border, (int)(8 * squareSize), true
		);
        g.fill3DRect(border, 0, (int)(8 * squareSize), border, true);
        g.fill3DRect(
			border, (int)(8 * squareSize) + border, 
			(int)(8 * squareSize), border, true
		);
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, 0, border, border, true);
        g.fill3DRect(0, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, (int)(8*squareSize)+border, border, border, true);
        
        g.setColor(new Color(0,100,0));
        g.fill3DRect((int)(8*squareSize)+2*border, 0, 200, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+2*border, (int)(8*squareSize)+border, 200, border, true);
    }
    // Gets the chess pieces image
    public static BufferedImage getChessPieceImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("ChessPieces.png"));
        } catch (IOException e) {}
        return image;
    }
    public static void newGame() {
        BoardGeneration.initiateStandardChess();
    }
}