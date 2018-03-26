package student_player;

import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.ArrayList;

public class MyTools {
    public static int ValueNextOneStepMinMaxMove(TablutBoardState tbs) {

        int currentPlayer = tbs.getTurnPlayer();
        int opponent = tbs.getOpponent();

        int maxPlayerMoveValue = -1000000; // High value means "good" move

        ArrayList<TablutMove> moves = tbs.getAllLegalMoves();

        int minNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);

        for (TablutMove move : moves) {
            int currentPlayerMoveValue = 0;
            TablutBoardState cloneBS = (TablutBoardState) tbs.clone();
            cloneBS.processMove(move);

            if (cloneBS.getWinner() == currentPlayer) {
                maxPlayerMoveValue = 100000;
                break;
            }

            if (currentPlayer == tbs.MUSCOVITE) {
                //OFFENSE
                currentPlayerMoveValue = getOffenseValue(cloneBS, opponent, move, minNumberOfOpponentPieces);
            } else {
                //DEFENSE
                Coord kingPos = cloneBS.getKingPosition();
                int minDistance = Coordinates.distanceToClosestCorner(kingPos);
                currentPlayerMoveValue = getDefenseValue(cloneBS, opponent, minDistance, minNumberOfOpponentPieces);
            }

            int maxOpponentMoveValue = LeafStepValue(cloneBS);

            if (maxPlayerMoveValue < currentPlayerMoveValue - maxOpponentMoveValue) {
                maxPlayerMoveValue = currentPlayerMoveValue - maxOpponentMoveValue;
            }
        }
        return maxPlayerMoveValue;
    }

    public static int LeafStepValue(TablutBoardState tbs) {

        int currentPlayer = tbs.getTurnPlayer();
        int opponent = tbs.getOpponent();

        int maxPlayerMoveValue = 0; // High value means "good" move

        ArrayList<TablutMove> moves = tbs.getAllLegalMoves();
        int minNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);
        int currentPlayerMoveValue = 0;

        for (TablutMove move : moves) {

            TablutBoardState cloneBS = (TablutBoardState) tbs.clone();
            cloneBS.processMove(move);

            if (cloneBS.getWinner() == currentPlayer) {
                maxPlayerMoveValue = 100000;
                break;
            }

            if (currentPlayer == TablutBoardState.MUSCOVITE) {
                //OFFENSE
                currentPlayerMoveValue = getOffenseValue(cloneBS, opponent, move, minNumberOfOpponentPieces);
            } else {
                // DEFENSE
                Coord kingPos = cloneBS.getKingPosition();
                int minDistance = Coordinates.distanceToClosestCorner(kingPos);
                currentPlayerMoveValue = getDefenseValue(cloneBS, opponent, minDistance, minNumberOfOpponentPieces);
            }

            if (currentPlayerMoveValue > maxPlayerMoveValue) {
                maxPlayerMoveValue = currentPlayerMoveValue;
            }
        }

        return maxPlayerMoveValue;
    }

    public static int getOffenseValue(TablutBoardState tbs, int opponent, TablutMove move, int minNumberOfOpponentPieces) {
        int currentPlayerMoveValue = 0;

        int newNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);
        if (newNumberOfOpponentPieces < minNumberOfOpponentPieces) {
            currentPlayerMoveValue += 1;
        }

        Coord newPos = move.getEndPosition();
        for (Coord neighbor : Coordinates.getNeighbors(newPos)) {
            if (tbs.getKingPosition().distance(neighbor) == 0) {
                currentPlayerMoveValue += 2;
            }
        }

        return currentPlayerMoveValue;
    }

    public static int getDefenseValue(TablutBoardState tbs, int opponent, int minDistance, int minNumberOfOpponentPieces) {
        int currentPlayerMoveValue = 0;

        int newNumberOfOpponentPieces = tbs.getNumberPlayerPieces(opponent);
        if (newNumberOfOpponentPieces < minNumberOfOpponentPieces) {
            currentPlayerMoveValue = 1;
        }

        Coord newKingPos = tbs.getKingPosition();
        int newDistance = Coordinates.distanceToClosestCorner(newKingPos);

        if (newDistance < minDistance) {
            currentPlayerMoveValue += 2;
        }

        return currentPlayerMoveValue;
    }
}
