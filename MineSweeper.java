package com.minesweeper;
import java.util.Random;
/**
 * MineSweeper class.
 */
public class MineSweeper{

 
    //******************************************************
    //*******    BELOW THIS LINE IS PROVIDED code    *******
    //*******            Do NOT edit code!           *******
    //*******		  Remember to add JavaDoc		 *******
    //******************************************************

    /**
     * enum with different difficulties.
     */
    public enum Level {
        /**
         * Tiny, Easy, Medium, Hard, and custom levels.
         */
        TINY, EASY, MEDIUM, HARD, CUSTOM 
    }
    
    //each level has a different board size (number of rows/columns) 
    //and a different number of mines

    /**
     * Rows for easy level.
     */
    private static int ROWS_EASY = 9;
    /**
     * Cols for easy level.
     */
    private static int COLS_EASY = 9;
    /**
     * Mines for easy level.
     */
    private static int MINES_EASY = 10;
    /**
     * Rows for tiny level.
     */
    private static int ROWS_TINY = 5;
    /**
     * Cols for tiny level.
     */
    private static int COLS_TINY = 5;
    /**
     * Mines for tiny level.
     */
    private static int MINES_TINY = 3;
    /**
     * Rows for medium level.
     */
    private static int ROWS_MEDIUM = 16;
    /**
     * Cols for medium level.
     */
    private static int COLS_MEDIUM = 16;
    /**
     * Mines for medium level.
     */
    private static int MINES_MEDIUM = 40;
    /**
     * Rows for hard level.
     */
    private static int ROWS_HARD = 16;
    /**
     * Cols for hard level.
     */
    private static int COLS_HARD = 30;
    /**
     * Mines for hard level.
     */
    private static int MINES_HARD = 99;

    /**
     * The 2d board of cells.
     */
    private DynGrid310<Cell> board;
    /**
     * Number of rows of the board.
     */
    private int rowCount;
    /**
     * Number of columns of the board.
     */
    private int colCount;
    /**
     * Number of mines in the board.
     */
    private int mineTotalCount;
    /**
     * Number of cells clicked / exposed.
     */
    private int clickedCount; 
    /**
     * Number of cells flagged as a mine.
     */
    private int flaggedCount; 


    /**
     * Possible statuses for game.
     */
    public enum Status {
        /**
         * INIT, INGAME, EXPLODED, and SOlVED game Statuses.
         */
        INIT, INGAME, EXPLODED, SOLVED
    }
    /**
     * Status of game.
     */
    private Status status; 

    /**
     * String names of status.
     * @return Array of Strings.
     */
    public final static String[] Status_STRINGS = {
        "INIT", "IN_GAME", "EXPLODED", "SOLVED"
    };
    
    /**
     * Initialize game based on a provided seed for random numbers and the specified level.
     * @param seed for random numbers.
     * @param level difficulty of game.
     */
    public MineSweeper(int seed, Level level){

        //if level is customized, need more details (number of rows/columns/mines)
        if (level==Level.CUSTOM)
            throw new IllegalArgumentException("Customized games need more parameters!");
            
        //set number of rows, columns, mines based on the pre-defined levels
        switch(level){
            case TINY:
                rowCount = ROWS_TINY;
                colCount = COLS_TINY;
                mineTotalCount = MINES_TINY;
                break;
            case EASY:
                rowCount = ROWS_EASY;
                colCount = COLS_EASY;
                mineTotalCount = MINES_EASY;
                break;
            case MEDIUM:
                rowCount = ROWS_MEDIUM;
                colCount = COLS_MEDIUM;
                mineTotalCount = MINES_MEDIUM;
                break;
            case HARD:
                rowCount = ROWS_HARD;
                colCount = COLS_HARD;
                mineTotalCount = MINES_HARD;
                break;
            default:
                //should not be able to reach here!
                rowCount = ROWS_TINY;
                colCount = COLS_TINY;
                mineTotalCount = MINES_TINY;
        }
        
        //create an empty board of the needed size
        board = genEmptyBoard(rowCount, colCount);
        
        //place mines, and initialize cells
        initBoard(seed);
    }

    /**
     * Constructor: should only be used for customized games.
     * @param seed for rand ints.
     * @param level difficulty.
     * @param rowCount number of rows.
     * @param colCount number of cols.
     * @param mineCount number of mines.
     */
    public MineSweeper(int seed, Level level, int rowCount, int colCount, int mineCount){
        
        if (level != Level.CUSTOM)
            throw new IllegalArgumentException("Only customized games need more parameters!");
        
        //set number of rows/columns/mines
        //assume all numbers are valid (check MineGUI for additional checking code)	
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.mineTotalCount = mineCount;
        
        
        //create an empty board of the needed size: you implement this method
        board = genEmptyBoard(rowCount, colCount);
        
        //place mines, and initialize cells: you implement part of this method
        initBoard(seed);
        
    }

    /**
     * Method to initialize the game, including placing mines.
     * assume it is invoked only after an empty board (rowCount x colCount) has been created and set.
     * @param seed for random numbers.
     */
    public void initBoard(int seed){
        
        //use seed to initialize a random number sequence
        Random random = new Random(seed);
        
        //randomly place mines on board
        int mineNum = 0;
        for ( ;mineNum<mineTotalCount;){
        
            //generate next (row, col)
            int row = random.nextInt(rowCount);
            int col = random.nextInt(colCount);
            
                
            //cell already has a mine: try again
            if (hasMine(row, col)){
                continue;
            }
            
            //place mine
            board.get(row,col).setMine();
            mineNum++;
        }
        //System.out.println(board);
        
        //calculate nbr counts for each cell
        for (int row=0; row<rowCount; row++){
            for (int col=0; col<colCount; col++){
                int count = countNbrMines(row, col);
                board.get(row,col).setCount(count);
            }
        }
        
        //initialize other game settings   
        status = Status.INIT;
            
        flaggedCount = 0;
        clickedCount = 0;

    }
        
    /**
     * Report number of rows.
     * @return number of rows.
     */
    public int rowCount() { return rowCount; }

    /**
     * Report number of columns.
     * @return number of columns.
     */
    public int colCount() { return colCount; }

    /**
     * Report whether board is solved.
     * @return boolean value.
     */
    public boolean isSolved(){ return status == Status.SOLVED;    }

    /**
     * Report whether a mine has exploded.
     * @return boolean value.
     */
    public boolean isExploded(){ return status == Status.EXPLODED; }

    /**
     * Display board with this method.
     * @return String representation of board.
     */
    public String boardToString(){
        StringBuilder sb = new StringBuilder();
        
        //header of column indexes
        sb.append("- |");
        for (int j=0; j<board.getNumCol(); j++){
            sb.append(j +"|");
        }
        sb.append("\n");
        
        for(int i=0; i<board.getNumRow(); i++){
            sb.append(i+" |");
            for (int j=0;j<board.getNumCol(); j++){
                sb.append(board.get(i,j).toString());
                sb.append("|");
            }
            sb.append("\n");
        }
        return sb.toString().trim();

    }

    /**
     * Displays game status and board.
     * @return String representation of board and game status.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Board Size: " + rowCount() + " x " + colCount() + "\n");
        sb.append("Total mines: " + mineTotalCount + "\n");
        sb.append("Remaining mines: " + mineLeft() + "\n");
        sb.append("Game status: " + getStatus() + "\n");
        
        sb.append(boardToString());
        return sb.toString().trim();
    }

    //******************************************************
    //*******      Methods to report cell details    *******
    //*******     These are used by GUI for display  *******
    //******* Check Cell class for helpful operations*******
    //******************************************************

    /**
     * Return true if cell at (row,col) is flagged, false otherwise.
     * @param row row being referenced.
     * @param col column being referenced.
     * @return boolean value.
     */
    public boolean isFlagged(int row, int col){
    	
        if (!board.isValidCell(row,col)){ // return false for invalid cell indices.
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.isFlagged());
    }
    
    /**
     * Return true if cell at (row,col) is not hidden, false otherwise.
     * @param row row being referenced.
     * @param col column being referenced.
     * @return boolean value.
     */
    public boolean isVisible(int row, int col){
        if (!board.isValidCell(row,col)){ // return false for invalid cell indices.
            return false;
        }
        Cell cell = board.get(row, col);
        return (cell.visible());               
    }
    
    /**
     * Return true if cell at (row,col) has a mine, regardless whether it has been flagged or not; false otherwise.
     * @param row row being referenced.
     * @param col column being referenced.
     * @return boolean value.
     */
    public boolean hasMine(int row, int col){
        if (!board.isValidCell(row,col)){ // return false for invalid cell indexes.
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.hasMine());               
    }
    
    /**
     * Return the count associated with cell at (row,col) has a mine.
     * @param row row being referenced.
     * @param col column being referenced.
     * @return boolean value.
     */
    public int getCount(int row, int col){
        if (!board.isValidCell(row,col)){ // return -2 for invalid cell indexes
            return -2;
        }
        Cell cell = board.get(row, col);
        return (cell.getCount());                    
    }
    
    //******************************************************
    //*******      Methods to report game status     *******
    //*******     These are used by GUI for display  *******
    //******************************************************

    /**
     * Report how many mines have not be flagged.
     * @return integer value.
     */
    public int mineLeft() {
    	return mineTotalCount-flaggedCount; 
    	
    }
    
    /**
     * Report current game status.
     * @return String value.
     */
    public String getStatus() { 
    	return Status_STRINGS[status.ordinal()]; 
    	
    }


    //******************************************************
    //*******  Methods reserved for testing/grading  *******
    //******************************************************

    /**
     * Return the game board.
     * @return DynGrid.
     */
    public DynGrid310<Cell> getBoard(){ return board;}

    /**
     * Set game board.
     * @param newBoard new game board.
     * @param mineCount number of mines.
     */
    public void setBoard(DynGrid310<Cell> newBoard, int mineCount) {
        //set board
        this.board = newBoard;
        
        //set size
        rowCount = board.getNumRow();
        colCount = board.getNumCol();
        
        
        //set other features
        status = Status.INIT;
            
        flaggedCount = 0;
        clickedCount = 0;
        mineTotalCount = mineCount;
    }

    //******************************************************
    //*******       END of PROVIDED code             *******
    //******************************************************


    //******************************************************
    //*******        Code you need to implement      *******
    //*******		   Remember to add JavaDoc		 *******
    //******************************************************

    // ADD MORE PRIVATE MEMBERS HERE IF NEEDED!


    //*******************************************************
    //******* Methods to support board initialization *******
    //*******************************************************

    /**
     * Create and return a grid with rowNum x colNum individual cells in it.
     * All cells are default cell objects (no mines), amortized O(rowCount x colCount).
     * @param rowNum number of rows.
     * @param colNum number of cols.
     * @return game board.
     */
    public static DynGrid310<Cell> genEmptyBoard(int rowNum, int colNum){
        if(rowNum <= 0 || colNum <= 0){ // Return null for invalid counts
            return null;
        }
        DynGrid310<Cell> dg = new DynGrid310<>(); // New board.
        for(int i = 0; i < rowNum; ++i){ // Iterate through number of rows.
            DynArr310<Cell> da = new DynArr310<>(colNum); // Create the new row based on colNum.
            for(int j = 0; j < colNum; ++j){
                da.add(new Cell()); // Add cells to the row.
            }
            dg.addRow(i,da); // Add the row to the board.
        }
        return dg;
    }

    /**
     * Employs countAdjMines() helper method.
     * Count the number of mines in the neighbor cells of cell (row, col).
     * @param row row being referenced.
     * @param col col being referenced.
     * @return -2 for invalid row / col indexes, -1 if cell at (row, col) has a mine underneath it. Otherwise return number of mines adj. to cell.
     */
    public int countNbrMines(int row, int col){
        if(!board.isValidCell(row, col)){ // Return -2 for invalid indices
            return -2;
        }
        if(board.get(row, col).hasMine()){ // Return -1 if cell is a mine
            return -1;
        } else {
            return countAdjMines(row, col); // Return number of mines adjacent to cell.
        }
    }

    /**
     * Helper method to count adjacent mines.
     * @param row row being referenced.
     * @param col col being referenced.
     * @return number of adjacent mines.
     */
    private int countAdjMines(int row, int col) {
        int count = 0; // the number of mines around the cell
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){ // Nested for loops iterate over each cell aroud the cell being referenced
                // First checks if the cell isValid, then checks if there is a mine at the cell
                int r = row + i;
                int c = col + j;
                if (board.isValidCell(r, c) && hasMine(r, c)){ 
                    count++; // If there is a mine, count is incremented
                }
            }
        }
        return count;
    }


    //******************************************************
    //*******   Methods to support game operations   *******
    //******************************************************

    /**
     * Function for when a cell is clicked.
     * If invalid or already visible cell, return -2.
     * If cell has a mine, return -1 and update game status.
     * Otherwise, open the cell and return number of mines adjacent to the cell.
     * If the cell has 0 mines adjacent to it, open all 0-count cells which are connected to this cell.
     * Also open all cells that are othogonally or diagonally adjacent to those 0-count cells.
     * Update game status and features if applicable.
     * @param row row being referenced.
     * @param col col being referenced.
     * @return integer value.
     */
    public int clickAt(int row, int col){
        if(clickedCount == 0){ // If no cells have been clicked, set the status to InGame since first call of the function is the first click
            status = Status.INGAME;
        }
        if(!board.isValidCell(row, col) || board.get(row, col).isFlagged() || board.get(row, col).visible()){ // If invalid cell, visible cell, or flagged cell
            return -2;
        } else if(board.get(row, col).hasMine()){ // If cell is a mine
            clickedCount++; // Increment clickedCount
            board.get(row, col).setVisible(); // Set the cell visible
            status = Status.EXPLODED; // Update status
            return -1;
        } else if(countAdjMines(row, col) == 0){ // Conditional for a 0-count cell.
            openAdjCells(row, col);
            if(clickedCount+mineTotalCount == board.getNumCol() * board.getNumRow()){ // If all cells have been clicked, set game status to solved.
                status = Status.SOLVED;
            }
            return 0;
        } else { // Conditional for cells with mines adjacent to them.
            clickedCount++;
            int adjMines = countAdjMines(row, col);
            board.get(row, col).setVisible();
            if(clickedCount+mineTotalCount == board.getNumCol() * board.getNumRow()){
                status = Status.SOLVED;
            }
            return adjMines;
        }
        
    }

    /**
     * Recursive helper method to open adjacent cells.
     * @param row row being referenced.
     * @param col col being referenced.
     */
    private void openAdjCells(int row, int col){
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){ // The same nested for loop as countAdjMines is employed
                int r = row + i; // Current iteration of row
                int c = col + j; // Current iteration of column
                if (board.isValidCell(r, c) && !board.get(r, c).visible()){ // Checks if the cell is valid, and if the cell is invisible
                    int adjMines = countAdjMines(r, c); // If cell is valid and invisivble, count number of mines around that cell
                    board.get(r, c).setVisible(); // Then the cell is set as visible
                    clickedCount++;
                    if (adjMines == 0){   
                        openAdjCells(r, c);  // If there are no mines around iteration of cell, open recurse over the function
                    }
                }
            }
        }
    }

    /**
     * Flag at cell located at (row,col) and update game features.
     * Update game status as needed.
     * @param row row being referenced.
     * @param col col being referenced.
     * @return whether the cell is flagged or not.
     */
    public boolean flagAt(int row, int col){
        if(clickedCount == 0){
            status = Status.INGAME;
        }
        if(!board.isValidCell(row, col) || board.get(row, col).visible()){return false;} // Return false for invalid or visible cell
        
        board.get(row, col).setFlagged();
        flaggedCount++; // Update number of flagges cells
        return true;
            
    }

    /**
     * Un-flag at cell located at (row,col) and update game features.
     * @param row row being referenced.
     * @param col col being referenced.
     * @return whether the cell is updated from flagged to unflagged.
     */
    public boolean unFlagAt(int row, int col){
        if(!board.isValidCell(row, col) || board.get(row, col).visible() || !isFlagged(row, col)){
            return false; // Return false for invalid cell, visible cell or if cell was not flagged before.
        } 
        board.get(row, col).unFlagged();
        flaggedCount--; // Update flagged count.
        return true;
    }

    //******************************************************
    //*******     BELOW THIS LINE IS TESTING CODE    *******
    //*******      Edit it as much as you'd like!    *******
    //*******		Remember to add JavaDoc			 *******
    //******************************************************

    /**
     * Main method.
     * @param args arguments.
     */
    public static void main(String args[]){
        //basic: get an empty board with no mines
        DynGrid310<Cell> myBoard = MineSweeper.genEmptyBoard(3,4);
        
        //board size, all 12 cells should be in the default state, no mines
        if (myBoard.getNumRow() == 3 && myBoard.getNumCol()==4 &&
            !myBoard.get(0,0).hasMine() && !myBoard.get(1,3).visible() &&
            !myBoard.get(2,2).isFlagged() && myBoard.get(2,1).getCount()==-1){
            System.out.println("Yay 0");
        }

        //init a game at TINY level
        //use the same random number sequence as GUI  - 
        //	this will create the same board as Table 2 of p1 spec PDF.
        // you can change this for your own testing.

        Random random = new Random(10);
        MineSweeper game = new MineSweeper(random.nextInt(),Level.TINY);
        
        //print out the initial board and verify game setting
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 |?|?|?|?|?|
        //1 |?|?|?|?|?|
        //2 |?|?|?|?|?|
        //3 |?|?|?|?|?|
        //4 |?|?|?|?|?|    
            
        //countNbrMines 
        if (game.countNbrMines(0,0) == 0 && game.countNbrMines(4,2) == 1 &&
            game.countNbrMines(3,3) == 3 &&	game.countNbrMines(2,3) == -1 &&
            game.countNbrMines(5,5) == -2){
            System.out.println("Yay 1");
        }
        
        //first click at (3,3)
        if (game.clickAt(-1,0) == -2 && game.clickAt(3,3) == 3 &&
            game.isVisible(3,3) && !game.isVisible(0,0) && 
            game.getStatus().equals("IN_GAME") && game.mineLeft() == 3){
            System.out.println("Yay 2");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 |?|?|?|?|?|
        //1 |?|?|?|?|?|
        //2 |?|?|?|?|?|
        //3 |?|?|?|3|?|
        //4 |?|?|?|?|?|
        
        //click at a mine cell
        if (game.clickAt(2,3) == -1 && game.isVisible(2,3) &&
            game.getStatus().equals("EXPLODED") ){
            System.out.println("Yay 3");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 |?|?|?|?|?|
        //1 |?|?|?|?|?|
        //2 |?|?|?|X|?|
        //3 |?|?|?|3|?|
        //4 |?|?|?|?|?|

        //start over with the same board
        random = new Random(10);
        game = new MineSweeper(random.nextInt(),Level.TINY);
        game.clickAt(3,3);
        
        //flag and unflag
        if (game.flagAt(2,3) && !game.isVisible(2,3)  &&
            game.isFlagged(2,3) && game.flagAt(2,4) && 
            game.mineLeft() == 1 && game.unFlagAt(2,3) &&
            !game.isFlagged(2,3) && game.mineLeft() == 2){
            System.out.println("Yay 4");
        }
        
        //cell state & operations
        // - a flagged cell can not be clicked
        // - flag a cell already flagged does not change anything but still returns true
        // - an opened cell cannot be flagged or unflagged
        // - a hidden cell not flagged cannot be unflagged
        if (game.clickAt(2,4) == -2 && game.flagAt(2,4) &&
            !game.flagAt(3,3) && !game.unFlagAt(3,3) &&
            !game.unFlagAt(2,3)){
            System.out.println("Yay 5");
        }

        //clicking on a zero-count cell
        if (game.clickAt(0,0) == 0 && game.isVisible(0,0) && game.isVisible(4,0) &&
            game.isVisible(0,4) && game.isVisible(3,2) && !game.isVisible(3,4) &&
            !game.isVisible(4,3)){
            System.out.println("Yay 6");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 | | | | | |
        //1 | | |1|2|2|
        //2 | | |1|?|F|
        //3 | | |2|3|?|
        //4 | | |1|?|?|
        
        //open all none-mine cells without any explosion solve the game!
        if (game.clickAt(4,4) == 1 && game.clickAt(3,4) == 3 && 
            game.getStatus().equals("SOLVED")){
            System.out.println("Yay 7");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 | | | | | |
        //1 | | |1|2|2|
        //2 | | |1|?|F|
        //3 | | |2|3|3|
        //4 | | |1|?|1|
    } 

}