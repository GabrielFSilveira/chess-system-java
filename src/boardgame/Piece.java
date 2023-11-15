package boardgame;

public class Piece {

	protected Position position;
	private Board board;
	
//Para criar a peça tem que informar o board
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
}
