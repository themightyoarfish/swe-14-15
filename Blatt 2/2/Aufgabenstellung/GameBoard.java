package minesweeper.model;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

/**
 * Model for a Minesweeper game instance. </p> A Minesweeper game consists of a
 * rectangular game board with n rows and m columns of game cells. Each cell may
 * hold a bomb. At the beginning of the game each cell looks the same. The
 * player may choose to open a cell or to set a flag on it. If they reveal the
 * cell which contains a bomb, the game is lost. Else, every neighbor cell which
 * has no neighboring cells with bombs will be revealed too. Every neighbor cell
 * which neighboring cells do contain a bomb will show how many bombs are in its
 * neighborhood. A cell on which a flag is set, cannot be revealed.The game is
 * won if every cell which does not contain a bomb is revealed. </p> A
 * {@code GameBoard} instance consists of an array of {@code Field} elements
 * representing the cells of the Minesweeper game. Changes on the {@code Field}
 * elements will be propagated to the corresponding {@code GameBoard} instance.
 * A {@code GameBoard} instance will notify its observes if its state changes.
 * 
 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
 */
public class GameBoard extends Observable {

   /**
    * Value returned by {@link #getState()} if this game is lost.
    */
   public static final int LOST = -1;
   /**
    * Value returned by {@link #getState()} if this game is still running.
    */
   public static final int RUNNING = 0;
   /**
    * Value returned by {@link #getState()} if this game is won.
    */
   public static final int WON = 1;

   /**
    * The actual game board
    */
   private Field[][] fields;

   /**
    * The total number of bombs on this {@code GameBoard}
    */
   private int numberOfBombs;

   /**
    * The actual number of flags set on all {@code Field} elements of this
    * {@code GameBoard}.
    */
   private int numberOfFlags;

   /**
    * The actual number of revealed {@code Field} elements of this
    * {@code GameBoard}.
    */
   private int numberOfRevealed;

   /**
    * The current state of this {@code GameBoard}. May be LOST, RUNNING or WON.
    */
   private int state;

   /**
    * Instantiates a new {@code GameBoard} with {@code width*height} elements
    * and {@code numberOfBombs} randomly distributed {@code Field} instances
    * which contain a bomb. All other {@code Field} elements wont contain a
    * bomb. No {@code Field} is revealed after instantiation.
    * 
    * @param width
    *           number of {@code Field} elements in each row
    * @param height
    *           number of rows
    * @param numberOfBombs
    *           number of bombs to be placed randomly in this game
    * 
    * @throws IllegalArgumentExcpetion
    *            if {@code width} is < 1
    * @throws IllegalArgumentException
    *            if {@code height} is < 1
    * @throws IllegalArguemntException
    *            if {@code numberOfBombs} is > width*height-1
    */
   public GameBoard(int width, int height, int numberOfBombs) {
      if (width < 1) {
         throw new IllegalArgumentException("Width must be >=1");
      }
      if (height < 1) {
         throw new IllegalArgumentException("Height must be >=1");
      }
      if (numberOfBombs >= width * height - 1) {
         throw new IllegalArgumentException("To many bombs");
      }

      fields = new Field[height][width];

      this.numberOfBombs = numberOfBombs;
      this.numberOfFlags = 0;
      this.numberOfRevealed = 0;
      this.state = RUNNING;

      /*
       * randomly set the fields which contain a bomb
       */
      int bombsSet = this.numberOfBombs;
      int row, col;
      Random random = new Random();

      while (bombsSet > 0) {
         row = random.nextInt(height);
         col = random.nextInt(width);
         if (fields[row][col] == null) {
            fields[row][col] = new Field(true);
            bombsSet--;
         }
      }

      /*
       * set all other fields
       */
      for (int j = 0; j < fields.length; j++) {
         for (int i = 0; i < fields[j].length; i++) {
            if (fields[j][i] == null) {
               fields[j][i] = new Field(false);
            }
         }
      }

      /*
       * initialize all fields. link them to their neighbors and calculate the
       * number of neighboring fields with bombs.
       */
      for (int j = 0; j < fields.length; j++) {
         for (int i = 0; i < fields[j].length; i++) {
            fields[j][i].init(i, j);
         }
      }

   }

   /**
    * Called if a {@code Field} has been revealed. Will check if the game is
    * lost or won and in all cases notify the observers of this
    * {@code GameBoard} ({@link GameBoard.Field#reveal()})
    * 
    * @param f
    *           {@code Field} that has been revealed
    */
   private void fieldRevealed(Field f) {

      this.numberOfRevealed++;
      if (f.isBomb()) {
         this.state = LOST;
      } else if (numberOfRevealed + numberOfBombs == fields.length
            * fields[0].length) {
         this.state = WON;
      }

      this.setChanged();
      this.notifyObservers();
   }

   /**
    * Called if the flag of a {@code Field} has been set or unset (
    * {@link GameBoard.Field#toggleFlagSet()})
    * 
    * @param f
    *           {@code Field} which flag has changed.
    */
   private void flagChanged(Field f) {

      if (f.isFlagSet()) {
         this.numberOfFlags++;
      } else {
         this.numberOfFlags--;
      }

      this.setChanged();
      this.notifyObservers();
   }

   /**
    * Returns the {@code GameBoard.Field} at the given position. Will return
    * {@code null} if {@code xPos} or {@code yPos} do not denote a valid postion
    * on this {@code GameBoard}.
    * 
    * @param xPos
    *           x-axis value of the requested {@code Field}
    * @param yPos
    *           y-axis value of the requested {@code Field}
    * @return the requested {@code Field} or {@code null} if the given position
    *         was not valid.
    */
   public Field getField(int xPos, int yPos) {
      if (yPos < 0 || yPos >= this.fields.length || xPos < 0
            || xPos >= this.fields[yPos].length) {
         return null;
      } else {
         return this.fields[yPos][xPos];
      }
   }

   /**
    * Returns the number of rows in this {@code GameBoard}
    * 
    * @return height i.e. number of rows in this {@code GameBoard}
    */
   public int getHeight() {
      return this.fields.length;
   }

   /**
    * Returns the total amount of {@code Field} elements which contain a bomb.
    * 
    * @return number of {@code Field} elements which contain a bomb
    */
   public int getNumberOfBombs() {
      return this.numberOfBombs;
   }

   /**
    * Returns the currently number of {@code Field} elements which flag is set.
    * 
    * @return number of {@code Field} element with a set flag.
    */
   public int getNumberOfFlags() {
      return this.numberOfFlags;
   }

   /**
    * Returns the currently number of revealed {@code Field} elements
    * 
    * @return number of {@code Field} elements which have been revealed yet.
    */
   public int getNumberOfRevealed() {
      return this.numberOfRevealed;
   }

   /**
    * Returns the state of this {@code GameBoard}. It may be {@link #RUNNING},
    * {@link #WON} or {@link #LOST}.
    * 
    * @return the state of this {@code GameBoard}
    */
   public int getState() {
      return this.state;
   }

   /**
    * Returns the number of {@code Field} elements if every row of this
    * {@code GameBoard}
    * 
    * @return width i.e. number of {@code Field} elements in every row of this
    *         {@code GameBoard}
    */
   public int getWidth() {
      return this.fields[0].length;
   }

   /**
    * A {@code Field} represents one cell in a Minesweeper game. It may contain
    * a bomb or not. A {@code Field} can be revealed ({@link #reveal()}) or a
    * flag can be set, such that it cannot be revealed until the flag is unset (
    * {@link #toggleFlagSet()}). If the state of a {@code Field} is changed it
    * will notify the corresponding {@code GameBoard} an all of its observers.
    * 
    * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
    */
   public class Field extends Observable {

      /**
       * denotes if this {@code Field} contains a bomb
       */
      private boolean bomb;
      /**
       * denotes if the flag of this {@code Field} is set
       */
      private boolean flagSet;
      /**
       * all neighbors of this {@code Field} (Moore Neigborhood)
       */
      private Iterable<Field> neighbors;

      /**
       * number of neighbor {@code Field} which contain a bomb
       */
      private int numberOfNeighborBombs;

      /**
       * denotes if this {@code Field} is revealed
       */
      private boolean revealed;

      /**
       * Create a new {@code Field} which may contain a bomb or not. No
       * {@code Field} will be revealed or its flag be set after instantiation.
       * 
       * @param bomb
       *           if the {@code Field} should contain a bomb or not
       */
      private Field(boolean bomb) {
         this.bomb = bomb;
         this.revealed = false;
         this.flagSet = false;
      }

      /**
       * Calculates the number of neighboring {@code Field} instances which
       * contain a bomb.
       * 
       * @return the number of neighboring {@code Field} instances which contain
       *         a bomb
       */
      private int calculateNumberOfNeighborBombs() {
         int bombs = 0;
         for (Field f : getNeighbors()) {
            if (f.isBomb()) {
               bombs++;
            }
         }
         return bombs;
      }

      /**
       * Returns all neighbor {@code Field} elements of this {@code Field}.
       * 
       * @return all neighbor {@code Field} elements
       */
      private Iterable<Field> getNeighbors() {
         return this.neighbors;
      }

      /**
       * Returns the number of neighbor {@code Field} elements which contain a
       * bomb.
       * 
       * @return number of neighbor {@code Field} elements which contain a bomb
       */
      public int getNumberOfNeighborBombs() {
         return this.numberOfNeighborBombs;
      }

      /**
       * Links neighbors to this {@code Field} and calculates the number of
       * neighboring bombs.
       * 
       * @param xPos
       *           x-axis value of the position of this {@code Field}
       * @param yPos
       *           y-axis value of the position of this {@code Field}
       */
      private void init(int xPos, int yPos) {
         neighbors = this.collectNeighbours(xPos, yPos);
         numberOfNeighborBombs = this.calculateNumberOfNeighborBombs();
      }

      /**
       * Returns if this {@code Field} contains a bomb or not
       * 
       * @return {@code true} if this {@code Field} contains a bomb.
       */
      public boolean isBomb() {
         return this.bomb;
      }

      /**
       * Returns if the flag of this {@code Field} is set
       * 
       * @return {@code true} if the flag of this {@code Field} is set.
       */
      public boolean isFlagSet() {
         return this.flagSet;
      }

      /**
       * Returns if this {@code Field} has already been revealed.
       * 
       * @return {@code true} if this {@code Field} has already been revealed.
       */
      public boolean isRevealed() {
         return this.revealed;
      }

      /**
       * Gets all neighbor {@code Field} elements to the given position and
       * returns them as an {@code Iterable}. A {@code Field} may have up to 8
       * neighbor {@code Field} elements (Moore-Neigborhood).
       * 
       * @param xPos
       *           x-axis value of the {@code Field} which neighbors should be
       *           collected
       * @param yPos
       *           y-axis value of the {@code Field} which neighbors should be
       *           collected
       * @return all neigboring {@code Field} elements to the given positions
       */
      private Iterable<Field> collectNeighbours(int xPos, int yPos) {
         LinkedList<Field> l = new LinkedList<Field>();
         for (int j = yPos - 1; j <= yPos + 1; j++) {
            for (int i = xPos - 1; i <= xPos + 1; i++) {
               Field n = getField(i, j);
               if (n != null && n != this) {
                  l.add(n);
               }

            }
         }
         return l;
      }

      /**
       * Reveals this {@code Field}. If it does not contain a bomb and no
       * neighbor {@code Field} elements to contain a bomb, all neighbor
       * {@code Field} elements will be revealed too. A {@code Field} which flag
       * is set will not be revealed. Notifies all observers and the
       * corresponding {@code GameBoard} (
       * {@link GameBoard#fieldRevealed(Field)})
       */
      public void reveal() {
         if (!this.isRevealed()) {

            if (!this.isFlagSet()) {
               this.revealed = true;

               if (!this.isBomb() && this.getNumberOfNeighborBombs() == 0) {
                  for (Field f : this.getNeighbors()) {
                     f.reveal();
                  }
               }
               this.setChanged();
               this.notifyObservers();
               fieldRevealed(this);
            }
         }
      }

      /**
       * Toggles the flag of this {@code Field}. Notifies all observers and the
       * corresponding {@code GameBoard} ( {@link GameBoard#flagChanged(Field)}
       * ).
       */
      public void toggleFlagSet() {
         this.flagSet = !this.flagSet;
         this.setChanged();
         this.notifyObservers();
         flagChanged(this);
      }
   }
}
