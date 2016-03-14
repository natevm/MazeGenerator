package nathanbitikoferexercise6.maze;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import nathanbitikoferexercise6Display.StdDraw;
import java.util.Timer.*;
import javax.swing.Timer;

/**
 * Maze.java
 * The following class generates a maze using prim's algorithm and solves it using
 * a depth first search.
 * @author nathan
 */
public class Maze {

    public int[][] cellArray;
    public final int CELL = 1;
    public final int VISITEDCELL = 2;
    public final int WALL = 0;

    public int startRow = 1, startCol = 1;
    public int endRow, endCol = 0;

    public ArrayList<Cell> discoveredList = new ArrayList<>();
    public Stack<Cell> path = new Stack<>();
    
    public ArrayList<Double> timeList = new ArrayList<>();

    public void generate(String style, int rows, int collums) {
        if (rows % 2 == 0) {
            rows++;
        }
        if (collums % 2 == 0) {
            collums++;
        }
        cellArray = new int[rows][collums];
        switch (style) {
            case "PRIM":
                generatePrimStyle();
                break;
        }
        endRow = cellArray.length-2;
        endCol = cellArray[0].length-2;
    }

    public void generate(int rows, int collums) {
        cellArray = new int[rows][collums];
        initializeCells();
    }

    private void generatePrimStyle() {
        //1: Start with a grid full of walls.
        initializeCells();
        linkCells();

        //2: Pick a cell, mark it as part of the maze. 
        //half the elements are cells. each cell is an odd multiple of 2.
        //int currentRow = (int)(((Math.random() * (cellArray.length/2))*2)+1); 
        int currentRow = (int) (Math.random() * (int) (cellArray.length / 2));
        int currentCol = (int) (Math.random() * (int) (cellArray[0].length / 2));
        currentRow *= 2;
        currentCol *= 2;
        currentRow += 1;
        currentCol += 1;
        cellArray[currentRow][currentCol] = VISITEDCELL;

        //Add the walls of the cell to the wall list.
        ArrayList<WallAndOppositeCell> wallList = new ArrayList<>();
        //top
        if (currentRow - 1 > 0) {
            wallList.add(new WallAndOppositeCell(currentRow - 1, currentCol, currentRow - 2, currentCol));
        }
        //bottom
        if (currentRow + 1 < cellArray.length - 1) {
            wallList.add(new WallAndOppositeCell(currentRow + 1, currentCol, currentRow + 2, currentCol));
        }
        //right
        if (currentCol + 1 < cellArray[0].length - 1) {
            wallList.add(new WallAndOppositeCell(currentRow, currentCol + 1, currentRow, currentCol + 2));
        }
        //left
        if (currentCol - 1 > 0) {
            wallList.add(new WallAndOppositeCell(currentRow, currentCol - 1, currentRow, currentCol - 2));
        }

        //3: While there are walls in the list:
        while (!wallList.isEmpty()) {
            //1: Pick a random wall from the list. 
            WallAndOppositeCell wallAndCell = pickRandomWallFromList(wallList);

            //If the cell on the opposite side isn't in the maze yet:
            if (cellArray[wallAndCell.oppositeCellRow][wallAndCell.oppositeCellCol] != VISITEDCELL) { //Make the wall a passage and mark the cell on the opposite side as part of the maze.
                cellArray[wallAndCell.wallRow][wallAndCell.wallCol] = CELL;
                cellArray[wallAndCell.oppositeCellRow][wallAndCell.oppositeCellCol] = VISITEDCELL;

                //Add the neighboring walls of the cell to the wall list.
                addNeighboringWallsToWallList(wallAndCell, wallList);
            }
            //2: Remove the wall from the list.
            wallList.remove(wallAndCell);
        }
    }

    private void addNeighboringWallsToWallList(WallAndOppositeCell wallAndCell, ArrayList<WallAndOppositeCell> wallList) {
        //top
        if (wallAndCell.oppositeCellRow - 1 > 0) {
            if (cellArray[wallAndCell.oppositeCellRow - 1][wallAndCell.oppositeCellCol] == WALL) {
                wallList.add(new WallAndOppositeCell(wallAndCell.oppositeCellRow - 1, wallAndCell.oppositeCellCol,
                        wallAndCell.oppositeCellRow - 2, wallAndCell.oppositeCellCol));
            }
        }
        //bottom
        if (wallAndCell.oppositeCellRow + 1 < cellArray.length - 1) {
            if (cellArray[wallAndCell.oppositeCellRow + 1][wallAndCell.oppositeCellCol] == WALL) {
                wallList.add(new WallAndOppositeCell(wallAndCell.oppositeCellRow + 1, wallAndCell.oppositeCellCol,
                        wallAndCell.oppositeCellRow + 2, wallAndCell.oppositeCellCol));
            }
        }
        //left
        if (wallAndCell.oppositeCellCol - 1 > 0) {
            if (cellArray[wallAndCell.oppositeCellRow][wallAndCell.oppositeCellCol - 1] == WALL) {
                wallList.add(new WallAndOppositeCell(wallAndCell.oppositeCellRow, wallAndCell.oppositeCellCol - 1,
                        wallAndCell.oppositeCellRow, wallAndCell.oppositeCellCol - 2));
            }
        }
        //right
        if (wallAndCell.oppositeCellCol + 1 < cellArray[0].length - 1) {
            if (cellArray[wallAndCell.oppositeCellRow][wallAndCell.oppositeCellCol + 1] == WALL) {
                wallList.add(new WallAndOppositeCell(wallAndCell.oppositeCellRow, wallAndCell.oppositeCellCol + 1,
                        wallAndCell.oppositeCellRow, wallAndCell.oppositeCellCol + 2));
            }
        }
    }

    private WallAndOppositeCell pickRandomWallFromList(ArrayList<WallAndOppositeCell> wallList) {
        int randomNumber = (int) (Math.random() * wallList.size());
        return wallList.get(randomNumber);
    }

    public void initializeCells() {
        for (int[] cellArray1 : cellArray) {
            for (int j = 0; j < cellArray[0].length; j++) {
                cellArray1[j] = WALL;
            }
        }
    }

    public void linkCells() {
        for (int i = 1; i < cellArray.length; i += 2) {
            for (int j = 1; j < cellArray[0].length; j += 2) {
                cellArray[i][j] = CELL;
            }
        }
    }

    public void solve() {
        path = new Stack<>();
        discoveredList = new ArrayList<>();
        Cell current = new Cell(startRow, startCol);
        path.push(current);
        while (!path.isEmpty())
        {
            current = path.peek();
            if (current.equals(new Cell(endRow, endCol))){
                return;
            }
            
            Cell nextCell;
            if (!discoveredList.contains(new Cell(current.row, current.col +1))
                    && cellArray[current.row][current.col + 1] != WALL)
            {
                nextCell = new Cell(current.row, current.col+1);
            }
            else if (!discoveredList.contains(new Cell(current.row, current.col -1))
                    && cellArray[current.row][current.col - 1] != WALL)
            {
                nextCell = new Cell(current.row, current.col-1);
            }
            else if (!discoveredList.contains(new Cell(current.row +1, current.col))
                    && cellArray[current.row+1][current.col] != WALL)
            {
                nextCell = new Cell(current.row+1, current.col);
            }
            else if (!discoveredList.contains(new Cell(current.row -1, current.col))
                    && cellArray[current.row - 1][current.col] != WALL)
            {
                nextCell = new Cell(current.row -1, current.col);
            }
            else 
                nextCell = null;
            if (discoveredList.contains(current))
            {
                if (nextCell!=null)
                    path.push(nextCell);
                else
                    path.pop();
            }
            else
            {
                discoveredList.add(current);
                if (nextCell != null)
                    path.push(nextCell);
            }
        }
    }

    public void runMonteCarlo() {
       
        for (int i = 0; i < 1000; i++)
        {
            long startTime = System.currentTimeMillis();
            
            generate("PRIM",101,121);
            solve();
            int endTime = (int)System.currentTimeMillis();
            int elapsedTime = (int)(endTime - startTime);
            timeList.add((double)elapsedTime);
        }
    }
}
