package nathanbitikoferexercise6.maze;

/**
 * A cell is a data structure that has a row and a column index. It's used in 
 * the maze class.
 * @author nathan
 */

public class Cell {

    //Store a cell location at a row and col
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int row;
    public int col;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        if(o instanceof Cell){
            Cell toCompare = (Cell) o;
            return (this.col == toCompare.col && this.row == toCompare.row);
          }
        return false;
    }
}
