package student_player;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

import java.util.ArrayList;
import java.util.Random;

import static student_player.MyTools.*;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260608613");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...

        int currentPlayer = boardState.getTurnPlayer();
        int opponent = boardState.getOpponent();

        Random rand = new Random(1818); // Karl Marx birth year (the seeds you used are all related to some revolution right?)
        ArrayList<TablutMove> moves = boardState.getAllLegalMoves();
        int minNumberOfOpponentPieces = boardState.getNumberPlayerPieces(opponent);
        TablutMove bestMove = moves.get(rand.nextInt(moves.size()));

        int maxPlayerMoveValue = -1000000;
        int currentPlayerMoveValue = 0;


        for(TablutMove move : moves){
            TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
            cloneBS.processMove(move);

            if (cloneBS.getWinner() == currentPlayer) {
                bestMove = move;
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

            int maxOpponentMoveValue= MyTools.ValueNextOneStepMinMaxMove(cloneBS);

            if (maxPlayerMoveValue < currentPlayerMoveValue - maxOpponentMoveValue) {
                maxPlayerMoveValue = currentPlayerMoveValue - maxOpponentMoveValue;
                bestMove = move;
            }
        }

        // Return your move to be processed by the server.
        return bestMove;
    }
}