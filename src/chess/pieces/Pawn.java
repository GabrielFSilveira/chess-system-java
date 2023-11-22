package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    public Pawn(Board board, Color color) {
        super(board, color);
        
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
        }
        return mat;
    }
}

