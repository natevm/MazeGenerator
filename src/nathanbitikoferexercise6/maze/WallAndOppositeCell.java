package nathanbitikoferexercise6.maze;

/**
 * The following class is a data structure that contains two cells. 
 * It's used in the maze class for prim's algorithm.
 * @author nathan
 */
public class WallAndOppositeCell {

    public WallAndOppositeCell(int wallRow, int wallCol, int oppositeCellRow, int oppositeCellCol) {
        this.wallRow = wallRow;
        this.wallCol = wallCol;
        this.oppositeCellRow = oppositeCellRow;
        this.oppositeCellCol = oppositeCellCol;
    }
    int wallRow;
    int wallCol;
    int oppositeCellRow;
    int oppositeCellCol;
}