package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "P";
    }
    
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);

        //Movimento do peão uma casa
        if (getColor() == Color.WHITE) {
            p.setValues(position.getRow() - 1, position.getColumn()); //posição acima
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){ //se estiver vazia e existir ele pode mover
                mat[p.getRow()][p.getColumn()] = true;
            }
            //Primeiro movimento do peão duas casas
            p.setValues(position.getRow() - 2, position.getColumn()); 
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)&& getMoveCount() == 0){ //se estiver vazia e existir ele pode mover
                mat[p.getRow()][p.getColumn()] = true;
            }
            //caso exista uma peça diagonal à esquerda
            p.setValues(position.getRow() - 1, position.getColumn() - 1); //posição acima
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){ //se estiver vazia e existir ele pode mover
                mat[p.getRow()][p.getColumn()] = true;
            }
            //caso exista uma peça diagonal à direita
            p.setValues(position.getRow() - 1, position.getColumn() + 1); //posição acima
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){ //se estiver vazia e existir ele pode mover
                mat[p.getRow()][p.getColumn()] = true;
            }

            // #specialmove en passant white
            if (position.getRow() == 3) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                //testando se a posição existe e se tem uma peça oponente e se a peça está vulneravel a tomar o en passant
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() - 1][left.getColumn()] = true;
                }
                
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                //testando se a posição existe e se tem uma peça oponente e se a peça está vulneravel a tomar o en passant
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() - 1][right.getColumn()] = true;
                }
            }
        }
        else{
            p.setValues(position.getRow() + 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){ 
                mat[p.getRow()][p.getColumn()] = true;
            }
            //Primeiro movimento do peão duas casas
            p.setValues(position.getRow() + 2, position.getColumn()); //posição acima
            Position p2 = new Position(position.getRow() + 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)&& getMoveCount() == 0){ //se estiver vazia e existir ele pode mover
                mat[p.getRow()][p.getColumn()] = true;
            }
            //caso exista uma peça diagonal à esquerda
            p.setValues(position.getRow() + 1, position.getColumn() - 1); 
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){ 
                mat[p.getRow()][p.getColumn()] = true;
            }
            //caso exista uma peça diagonal à direita
            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){ 
                mat[p.getRow()][p.getColumn()] = true;
            }

               // #specialmove en passant black
            if (position.getRow() == 4) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                //testando se a posição existe e se tem uma peça oponente e se a peça está vulneravel a tomar o en passant
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() + 1][left.getColumn()] = true;
                }
                
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                //testando se a posição existe e se tem uma peça oponente e se a peça está vulneravel a tomar o en passant
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() + 1][right.getColumn()] = true;
                }
            }
        }
        return mat;
    }
}

