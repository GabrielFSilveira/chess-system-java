package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;

	private List<Piece> piecesOnTheBoard = new ArrayList();
	private List<Piece> capturedPieces = new ArrayList();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck(){
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	// Esse metodo retorna uma matriz de peças correspondente a essa partida
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		// Percorrendo a matriz de peça do tabuleiro, para cada peça eu faço um
		// Downcasting para ChessPiece
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}

	// mostrar possiveis casas onde a peça pode ir
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		// convertendo a posição de xadrez para uma matriz normal
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	// Executa o movimento
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		// Valida a posição de origem da peça
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}

		check = (testCheck(opponet(currentPlayer))) ? true : false;

		if (testCheckMate(opponet(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); // retirei a peça que tava na posição de origem
		Piece capturedPiece = board.removePiece(target); // removi a possivel peça capturada que ta na posição de destino
		board.placePiece(p, target); //colocando na posição de destino a peça que tava na posição de origem
		
		if (capturedPiece != null){
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p = board.removePiece(target); //tira a peça do destino
		board.placePiece(p, source); //devolve para a origem
		//caso alguma peça foi capturada, irei voltar a peça
		if (capturedPiece != null){
			board.placePiece(capturedPiece, target); //voltando a peça
			capturedPieces.remove(capturedPiece); //tirando a peça da lista de capturadas
			piecesOnTheBoard.add(capturedPiece); //adicionando a peça a lista de peças no tabuleiro
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		//verifica se a peça é do proprio jogador
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
			throw new ChessException("The chosen piece is not yours");
		}
		// Caso não tenha nenhum movimento possivel
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		// se para a peça de origem a posição de destino não é um movimento possivel,
		// então não pode mexer para lá
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private Color opponet(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king (Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board"); //não deverar vir para essa exceção
	}
	//testando se o rei da cor está em check
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); //pegando a posição do rei no formato da matriz
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponet(color)).collect(Collectors.toList()); //lista de peças do oponente
		for (Piece p : opponentPieces){
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
				return true;
			} 
		}
		return false;

	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)){
			return false;
		}
		//pegando todas as peças da cor passada pelo parâmetro
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++){
				for (int j=0; j<board.getColumns(); j++){
					// analisando se tem como sair do check 
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturPiece = makeMove(source, target);
						boolean testCheck = testCheck(color); //testando se o rei ainda está em check
						undoMove(source, target, capturPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		//colocando a peça no tabuleiro
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		//colocando a peça na lista de peça no tabuleiro
		piecesOnTheBoard.add(piece);
	}

	// metodo para colocar as peças no tabuleiro
	private void initialSetup() {
		placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));

		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));
	}
}
