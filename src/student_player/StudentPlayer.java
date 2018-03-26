package student_player;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

import java.util.ArrayList;
import java.util.Random;

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

        int opponent = boardState.getOpponent();
        Random rand = new Random(1818); // Karl Marx birth year (the seeds you used are all related to some revolution right?)
        ArrayList<TablutMove> moves = boardState.getAllLegalMoves();
        int minNumberOfOpponentPieces = boardState.getNumberPlayerPieces(opponent);
        TablutMove bestMove = moves.get(rand.nextInt(moves.size()));
        boolean moveCaptures = false;


        if(boardState.getTurnPlayer() == TablutBoardState.MUSCOVITE){
            // Offensive (greedy like)
            for (TablutMove move : moves) {
                // To evaluate a move, clone the boardState so that we can do modifications on
                // it.
                TablutBoardState cloneBS = (TablutBoardState) boardState.clone();

                // Process that move, as if we actually made it happen.
                cloneBS.processMove(move);

                if (cloneBS.getWinner() == player_id) {
                    bestMove = move;
                    moveCaptures = true;
                    break;
                }

                int opponentMoveValue = MyTools.ValueNextOneStepMinMaxMove(cloneBS);



            }
            return bestMove;

        } else {
            //defensive (less greedy)

            Coord kingPos = boardState.getKingPosition();

            // Don't do a move if it wouldn't get us closer than our current position.
            int minDistance = Coordinates.distanceToClosestCorner(kingPos);

            // Iterate over moves from a specific position, the king's position!
            for (TablutMove move : boardState.getLegalMovesForPosition(kingPos)) {
                /*
                 * Here it is not necessary to actually process the move on a copied boardState.
                 * Note that it is more efficient NOT to copy the boardState. Consider this
                 * during implementation...
                 */
                int moveDistance = Coordinates.distanceToClosestCorner(move.getEndPosition());
                if (moveDistance < minDistance) {
                    minDistance = moveDistance;
                    bestMove = move;
                }
            }

            for (TablutMove move : moves) {
                TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                cloneBS.processMove(move);

                if (cloneBS.getWinner() == player_id) {
                    bestMove = move;
                    moveCaptures = true;
                    break;
                }
            }
        }






        // Is random the best you can do? Well... yes :D
        Move myMove = boardState.getRandomMove();

        // Return your move to be processed by the server.
        return myMove;
    }
}