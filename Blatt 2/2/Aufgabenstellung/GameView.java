package minesweeper.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import minesweeper.controller.FieldController;
import minesweeper.model.GameBoard;

/**
 * View for a {@link GameBoard}. Consists of a grid of
 * {@code GameView.FieldButton} elements, each representing one {@linke
 * GameBoard.Field} and a {@code JLabel} which shows how many bombs are left.
 * Will show a Dialog if the game is won or lost and then call the
 * {@link GameOverListener#gameOver()} of the given {@code GameOverListener}.
 * 
 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
 * 
 */
public class GameView extends JPanel implements Observer {

	/*
	 * String constants for all strings used in this GameView
	 */
	private static final String GAME_OVER = "Game Over";
	private static final String GAME_STATE_MESSAGE = "Bombs to find";
	private static final String LOST_MESSAGE = "Bomb exploded - you lost";
	private static final String WON_MESSAGE = "Congratulations, you found all bombs!";

	/**
	 * Graphical representation of the {@code GameBoard}
	 */
	private JPanel board;
	/**
	 * Displays a message with the current state of the game
	 */
	private JLabel message;
	/**
	 * Corresponding {@link GameBoard}
	 */
	private GameBoard model;
	/**
	 * Listener which will be called when the game is lost or won.
	 */
	private GameOverListener gameOver;

	/**
	 * Creates a new View and all necessary components and controllers. Will
	 * also link the model to this {@code GameView}.
	 * 
	 * @param model
	 *            the corresponding model
	 * @param gameOver
	 *            Listener which will be called if the game is lost of won.
	 */
	public GameView(GameBoard model, GameOverListener gameOver) {

		this.gameOver = gameOver;
		this.setLayout(new BorderLayout());
		this.board = new JPanel();
		this.message = new JLabel();

		this.board
				.setLayout(new GridLayout(model.getHeight(), model.getWidth()));

		/*
		 * create a new FieldButton for every GameBoard.Field
		 */
		for (int j = 0; j < model.getHeight(); j++) {
			for (int i = 0; i < model.getWidth(); i++) {
				this.board.add(new FieldButton(model.getField(i, j)));
			}
		}

		this.message.setText(GAME_STATE_MESSAGE + " "
				+ model.getNumberOfBombs());

		/*
		 * place the components
		 */
		this.add(this.message, BorderLayout.NORTH);
		this.add(this.board, BorderLayout.CENTER);

		/*
		 * link the model to this view.
		 */
		this.model = model;
		this.model.addObserver(this);

	}

	/**
	 * Will show a Dialog if the corresponding game is won or lost and call
	 * {gameOver()} on the given {@code GameOverListener}. Will always update
	 * the current state of the game.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (this.model.getState() == GameBoard.WON) {
			JOptionPane.showMessageDialog(this.getParent(), WON_MESSAGE,
					GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
			this.gameOver.gameOver();
		} else if (this.model.getState() == GameBoard.LOST) {
			JOptionPane.showMessageDialog(this.getParent(), LOST_MESSAGE,
					GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
			this.gameOver.gameOver();
		}

		message.setText(GAME_STATE_MESSAGE+" "
				+ (model.getNumberOfBombs() - model.getNumberOfFlags()));
	}

	/**
	 * Listener which will be used by a {@code GameView} if the state of the
	 * corresponding {@link GameBoard} changes to {@code WON} or {@code LOST}.
	 * 
	 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
	 * 
	 */
	public interface GameOverListener {
		/**
		 * Do something if the corresponding game is won or lost.
		 */
		public void gameOver();
	}

	/**
	 * View for a {@link GameBoard.Field}. As long as the corresponding
	 * {@link GameBoard.Field} is not revealed, this View will show a Button
	 * which can be clicked. After the corresponding model is revealed, this
	 * view will show a JLabel which will not react on any further events. In
	 * the Button-perspective this {@code FieldButton} may show a
	 * {@code JButton} with a number, showing the number of neigbor fields which
	 * contain bombs or a ! if the flag of the corresponding model has been set.
	 * 
	 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
	 * 
	 */
	public class FieldButton extends JPanel implements Observer {

		/*
		 * constants to switch between the button and the label which are shown
		 * whether the corrensponding GameBoard.Field has been revealed or not.
		 */
		private static final String BUTTON = "button";
		private static final String LABEL = "label";

		private JButton button;

		private GameBoard.Field field;
		private JLabel label;

		/**
		 * Creates a new FieldButton and its {@link FieldController}. Links this
		 * {@code FieldButton} to the corresponding {@link GameBoard.Field}.
		 * 
		 * @param field
		 *            Model for this view.
		 */
		public FieldButton(GameBoard.Field field) {
			this.button = new JButton();
			this.label = new JLabel();

			this.label.setHorizontalAlignment(JLabel.CENTER);

			this.setLayout(new CardLayout());

			this.add(button, BUTTON);
			this.add(label, LABEL);

			this.setPreferredSize(new Dimension(40, 40));

			this.field = field;
			this.field.addObserver(this);
			FieldController controller = new FieldController(field);
			this.button.addMouseListener(controller);

		}

		public void refreshView() {
			CardLayout layout = (CardLayout) this.getLayout();
			if (this.field.isRevealed()) {

				int count = this.field.getNumberOfNeighborBombs();

				if (button.isShowing()) {
					layout.show(this, LABEL);
				}

				if (this.field.isBomb()) {
					label.setText("B");
				} else if (count == 0) {
					label.setText(" ");
				} else {
					label.setText("" + count);
				}

			} else {
				if (label.isShowing()) {
					layout.show(this, BUTTON);
				}
				if (this.field.isFlagSet()) {
					button.setText("!");
				} else {
					button.setText("");
				}
			}
		}

		@Override
		public void update(Observable o, Object arg) {
			this.refreshView();
		}
	}
}
