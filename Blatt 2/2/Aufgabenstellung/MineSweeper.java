package minesweeper;

import javax.swing.JFrame;

import minesweeper.model.GameBoard;
import minesweeper.view.GameView;
import minesweeper.view.GameView.GameOverListener;

public class MineSweeper {

   /**
    * Print a short description of the usage of this program on the standard
    * output.
    */
   private static void usage() {
      System.out
            .println("java minesweeper.MineSweeper HEIGHT WIDTH NUMBER_OF_BOMBS");
   }

   /**
    * Handles an error in this program by printing the error message and a
    * description of the usage of the program on the standard output. It then
    * terminates the program with error code {@code 1}.
    * 
    * @param message
    *           the message to be printed before the program terminates
    */
   private static void handleError(String message) {
      System.out.println(message);
      usage();
      System.exit(1);
   }

   /**
    * Starts a new MineSweeper game in a new {@code JFrame}.
    * 
    * @param args
    *           height of the game in args[0], width of the game in args[1] and
    *           number of bombs in args[2].
    */
   public static void main(String[] args) {

      int height = 0, width = 0, bombs = 0;

      if (args.length == 3) {
         try {
            height = Integer.parseInt(args[0]);

            if (height < 1) {
               handleError("Height must be > 0");
            }
            width = Integer.parseInt(args[1]);

            if (width < 1) {
               handleError("Width must be > 0");
            }
            bombs = Integer.parseInt(args[2]);
            if (bombs >= width * height) {
               handleError("To many bombs. There must be at least one free field");
            }
         } catch (NumberFormatException ex) {
            handleError("Malformed integer.");
         }
      } else {
         handleError("Must be three arguments");
      }

      GameBoard model = new GameBoard(width, height, bombs);

      GameView view = new GameView(model, new GameOverListener() {
         @Override
         public void gameOver() {
            System.exit(0);
         }
      });

      JFrame frame = new JFrame("MineSweeper");
      frame.setContentPane(view);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);

   }

}
