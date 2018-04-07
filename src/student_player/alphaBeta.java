package student_player;

import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.ArrayList;

public class alphaBeta {
    public static int ValueNextOneStepMinMaxMove(TablutBoardState tbs, int n, int previousMoveValue, int alpha, int beta, boolean isMaximizer) {

        int currentPlayer = tbs.getTurnPlayer();
        int opponent = tbs.getOpponent();

        int cumulativeOpponentMoveValue = 0;

        ArrayList<TablutMove> moves = tbs.getAllLegalMoves();

        int minNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);

        for (TablutMove move : moves) {
            int currentPlayerMoveValue = 0;
            TablutBoardState cloneBS = (TablutBoardState) tbs.clone();
            cloneBS.processMove(move);

            if (cloneBS.getWinner() == currentPlayer) {
                if (isMaximizer)
                    alpha = 100000;
                else
                    beta = -100000;
                break;
            }

            if (currentPlayer == TablutBoardState.MUSCOVITE) {
                //OFFENSE
                currentPlayerMoveValue += getOffenseValue(cloneBS, opponent, move, minNumberOfOpponentPieces);
            } else {
                //DEFENSE
                Coord kingPos = cloneBS.getKingPosition();
                int minDistance = Coordinates.distanceToClosestCorner(kingPos);
                currentPlayerMoveValue += getDefenseValue(cloneBS, opponent, minDistance, minNumberOfOpponentPieces);
            }

            if (isMaximizer)
                currentPlayerMoveValue += previousMoveValue; 
            else 
                currentPlayerMoveValue = previousMoveValue - currentPlayerMoveValue;

            if (n==0){
                cumulativeOpponentMoveValue = LeafStepValue(cloneBS, currentPlayerMoveValue, alpha, beta, !isMaximizer);
            } else {
                cumulativeOpponentMoveValue = ValueNextOneStepMinMaxMove(cloneBS, n-1, currentPlayerMoveValue, alpha, beta, !isMaximizer);
            }

            if (isMaximizer && alpha < cumulativeOpponentMoveValue){
                alpha = cumulativeOpponentMoveValue;
                if (alpha > beta)
                    return alpha;
            } else if (!isMaximizer && beta > cumulativeOpponentMoveValue) {
                beta = cumulativeOpponentMoveValue;
                if (beta < alpha)
                    return beta;
            }
        }
        if (isMaximizer){
            return alpha;
        }
        return beta;
    }

    public static int LeafStepValue(TablutBoardState tbs, int previousMoveValue, int alpha, int beta, boolean isMaximizer) {

        int currentPlayer = tbs.getTurnPlayer();
        int opponent = tbs.getOpponent();


        ArrayList<TablutMove> moves = tbs.getAllLegalMoves();
        int minNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);
        int currentPlayerMoveValue = 0;

        for (TablutMove move : moves) {

            TablutBoardState cloneBS = (TablutBoardState) tbs.clone();
            cloneBS.processMove(move);

            if (cloneBS.getWinner() == currentPlayer) {
                if (isMaximizer)
                    return 100000;
                else
                    return  -100000;
            }

            if (currentPlayer == TablutBoardState.MUSCOVITE) {
                //OFFENSE
                currentPlayerMoveValue += getOffenseValue(cloneBS, opponent, move, minNumberOfOpponentPieces);
            } else {
                // DEFENSE
                Coord kingPos = cloneBS.getKingPosition();
                int minDistance = Coordinates.distanceToClosestCorner(kingPos);
                currentPlayerMoveValue += getDefenseValue(cloneBS, opponent, minDistance, minNumberOfOpponentPieces);
            }

            if (isMaximizer)
                currentPlayerMoveValue += previousMoveValue;
            else
                currentPlayerMoveValue = previousMoveValue - currentPlayerMoveValue;

            if (isMaximizer && alpha < currentPlayerMoveValue){
                alpha = currentPlayerMoveValue;
                if (alpha > beta)
                    return alpha;
            } else if (!isMaximizer && beta > currentPlayerMoveValue) {
                beta = currentPlayerMoveValue;
                if (beta < alpha)
                    return beta;
            }
        }
        if (isMaximizer){
            return alpha;
        }
        return beta;
    }

    public static int getOffenseValue(TablutBoardState tbs, int opponent, TablutMove move, int minNumberOfOpponentPieces) {
        int currentPlayerMoveValue = 0;

        int pieceCoeff = 1;
        int kingCoeff = 4;

        int newNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);
        if (newNumberOfOpponentPieces < minNumberOfOpponentPieces) {
            currentPlayerMoveValue += pieceCoeff*(minNumberOfOpponentPieces - newNumberOfOpponentPieces);
        }

        Coord newPos = move.getEndPosition();
        for (Coord neighbor : Coordinates.getNeighbors(newPos)) {
            if (tbs.getKingPosition().distance(neighbor) == 0) {
                currentPlayerMoveValue += kingCoeff;
            }
        }
        return currentPlayerMoveValue;
    }

    public static int getDefenseValue(TablutBoardState tbs, int opponent, int minDistance, int minNumberOfOpponentPieces) {
        int currentPlayerMoveValue = 0;

        int pieceCoeff = 1;
        int kingCoeff = 1;

        int newNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);
        if (newNumberOfOpponentPieces - minNumberOfOpponentPieces < 0) {
            currentPlayerMoveValue += pieceCoeff*(minNumberOfOpponentPieces - newNumberOfOpponentPieces);
        }

        Coord newKingPos = tbs.getKingPosition();
        int newDistance = Coordinates.distanceToClosestCorner(newKingPos);

        if (newDistance - minDistance < 0 ) {
            currentPlayerMoveValue += kingCoeff*(minDistance - newDistance);
        }

        return currentPlayerMoveValue;
    }
}
