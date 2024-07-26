package com.minesweeper;
/**
 * Generic dynamic grid class.
 * @param <T> generic type item.
 */
public class DynGrid310<T> {

	/**
	 * 2D Grid storage.
	 */
	private DynArr310<DynArr310<T>> storage;
	/**
	 * Number of rows in grid.
	 */	
	private int rows;
	/**
	 * Number of cols in grid.
	 */
	private int cols;
	
	/**
	 * Parameterless construtor for DynGrid310 generic class.
	 */
	public DynGrid310(){		
		storage = new DynArr310<DynArr310<T>>();
	}

	/**
	 * Number of rows in the grid, O(1).
	 * @return integer value.
	 */
	public int getNumRow() {
		return rows;
	}
	
	/**
	 * Number of cols in the grid, O(1).
	 * @return integer value.
	 */
	public int getNumCol() { 
		return cols;
	}

	/**
	 * Checks if there is a value in the location, O(1).
	 * @param row of the value.
	 * @param col of the value.
	 * @return boolean whether the value is null or not.
	 */
	public boolean isValidCell(int row, int col){
		if(row < 0 || row >= rows || col < 0 || col >= cols){ // Checks invalid indices
			return false;
		}
		
		return storage.get(row).get(col) != null;
	}

	/**
	 * get the cell value in the grid, O(1).
	 * @param row the row of the value.
	 * @param col the col of the value.
	 * @return the value in that location.
	 */
	public T get(int row, int col){
		if(row > rows || col > cols || row < 0 || col < 0){ // Checks for invalid indices
			throw new IndexOutOfBoundsException("Index("+row+","+col+") out of bounds!");
		}
		return storage.get(row).get(col);
	}
	
	/**
	 * Change a cell value in the grid and return the old cell value, O(1).
	 * @param row the row where the value is.
	 * @param col the col where the value is.
	 * @param value the new value.
	 * @return the old value.
	 */
	public T set(int row, int col, T value){
		if(row >= rows || col >= cols || row < 0 || col < 0){ // Checks invalid indices
			throw new IndexOutOfBoundsException("Index("+row+","+col+") out of bounds!");
		} else if(value == null){ // Checks if the value is null
			throw new IllegalArgumentException("Null values not accepted!");
		}
		T ret = storage.get(row).get(col);
		storage.get(row).set(col, value);
		return ret;

	}

	/**
	 * Insert newRow into the grid at index shifting rows if needed, O(R).
	 * @param index the index where the row is inserted.
	 * @param newRow the row being inserted.
	 * @return boolean value based on whether newRow was successfully added or not.
	 */
	public boolean addRow(int index, DynArr310<T> newRow){
		// Checks if: 
		//  - the index is out of bounds,
		//  - the newRow is null
		//  - the newRow is empty
		//  - if the length of the newRow is different from length of other rows
		if(index < 0 || index > rows || newRow == null || newRow.size() <= 0 || (rows != 0 && newRow.size() != storage.get(0).size())){
			return false;
		}
		if(storage.size() == 0){  // If newRow is the first row in the grid
			cols = newRow.size(); // cols is based on the number of values in the row 
		}
		storage.insert(index, newRow); // Insert the row
		rows++; // Increment rows
		return true;
	}
	
	/**
	 * Insert newCol into the grid at an index shifting cols if needed.
	 * O(CR) where R is the number of rows and C is the number of columns of the grid.
	 * @param index the index where newCol should be inserted.
	 * @param newCol the col which is being inserted.
	 * @return boolean value based on whether newCol was successfully added or not.
	 */
	public boolean addCol(int index, DynArr310<T> newCol){
		// Checks if: 
		//  - the index is out of bounds,
		//  - newCol is null
		//  - newCol is empty
		//  - if the length of the newCol is different from length of other cols
		if(index < 0 || index > cols || newCol == null || newCol.size() <= 0 || newCol.size() != rows){
			return false;
		}
		
		for(int i = 0; i < rows; i++){ // Iterate through rows adding values at the proper index
			storage.get(i).insert(index, newCol.get(i));
		}
		cols++; // Increment cols
		
		return true;

	}

	/**
	 * Remove and return a row at an index then shift the rows to remove the gap.
	 * Returns null in case of an invalid index.
	 * O(R) where R is the number of rows of the grid.
	 * @param index the index of the row which needs removal.
	 * @return the row which is removed.
	 */
	public DynArr310<T> removeRow(int index){	
		if(index < 0 || index >= rows){ // If invalid index return null
			return null;
		}
		DynArr310<T> ret = storage.get(index); // Store the row
		storage.remove(index); // Remove the row
		rows--; // Decrement rows
		return ret;
	}

	/**
	 * Remove and return a column at an index then shift the cols to remove the gap.
	 * If there is an invalid index return null.
	 * O(RC) where R is the number of rows and C is the number of columns.
	 * @param index the index which is to be removed from each row.
	 * @return the dynamic array of the values removed.
	 */
	public DynArr310<T> removeCol(int index){
		// Check if the index is invalid
		if(index < 0 || index >= cols){
			return null;
		}
		DynArr310<T> ret = new DynArr310<T>(rows); // Create a new dynamic array based on the number of rows
		for(int i = 0; i < rows; ++i){
			ret.add(storage.get(i).remove(index)); // Iterate through and remove each value 1 by 1
		}
		cols--; // Decrement number of columns
		if(cols == 0){ // If there are no columns there cannot be any rows
			rows = 0;  // so rows must be set to 0
		}
		return ret;
		
	}


	

	//******************************************************
	//*******     BELOW THIS LINE IS PROVIDED code   *******
	//*******             Do NOT edit code!          *******
	//*******		   Remember to add JavaDoc		 *******
	//******************************************************
	
	/**
	 * Visual representation of game board.
	 * @return String value.
	 */
	@Override
	public String toString(){
		if(getNumRow() == 0 || getNumCol() == 0 ){ return "empty board"; }
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i<getNumRow(); i++){
            sb.append("|");
    		for (int j=0;j<getNumCol(); j++){
      			sb.append(get(i,j).toString());
      		    sb.append("|");
      		}
      		sb.append("\n");
    	}
    	return sb.toString().trim();

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
	public static void main(String[] args){
		//These are _sample_ tests. If you're seeing all the "yays" that's
		//an excellend first step! But it does NOT guarantee your code is 100%
		//working... You may edit this as much as you want, so you can add
		//own tests here, modify these tests, or whatever you need!

		//create a grid of strings
		DynGrid310<String> sgrid = new DynGrid310<>();
		
		//prepare one row to add
		DynArr310<String> srow = new DynArr310<>();
		srow.add("English");
		srow.add("Spanish");
		srow.add("German");
		
		//addRow and checking
		if (sgrid.getNumRow() == 0 && sgrid.getNumCol() == 0 && !sgrid.addRow(1,srow)
			&& sgrid.addRow(0,srow) && sgrid.getNumRow() == 1 && sgrid.getNumCol() == 3){
			System.out.println("Yay 1");
		}
		
		//get, set, isValidCell
		if (sgrid.get(0,0).equals("English") && sgrid.set(0,1,"Espano").equals("Spanish") 
			&& sgrid.get(0,1).equals("Espano") && sgrid.isValidCell(0,0) 
			&& !sgrid.isValidCell(-1,0) && !sgrid.isValidCell(3,2)) {
			System.out.println("Yay 2");
		}

		//a grid of integers
		DynGrid310<Integer> igrid = new DynGrid310<Integer>();
		boolean ok = true;

		//add some rows (and implicitly some columns)
		for (int i=0; i<3; i++){
			DynArr310<Integer> irow = new DynArr310<>();
			irow.add((i+1) * 10);
			irow.add((i+1) * 11);
        
			ok = ok && igrid.addRow(igrid.getNumRow(),irow);
		}
		
		//toString
		//System.out.println(igrid);
		if (ok && igrid.toString().equals("|10|11|\n|20|22|\n|30|33|")){
			System.out.println("Yay 3");		
		}
				
		//prepare a column 
		DynArr310<Integer> icol = new DynArr310<>();
		
		//add two rows
		icol.add(-10);
		icol.add(-20);
		
		//attempt to add, should fail
		ok = igrid.addCol(1,icol);
		
		//expand column to three rows
		icol.add(-30);
		
		//addCol and checking
		if (!ok && !igrid.addCol(1,null) && igrid.addCol(1,icol) && 
			igrid.getNumRow() == 3 && igrid.getNumCol() == 3){
			System.out.println("Yay 4");		
		}
		
		System.out.println(igrid);
		
		//removeRow
		if (igrid.removeRow(5) == null && 
			igrid.removeRow(1).toString().equals("[20, -20, 22]") && 
			igrid.getNumRow() == 2 && igrid.getNumCol() == 3 ){
			System.out.println("Yay 5");	
		}
		
		//removeCol
		if (igrid.removeCol(0).toString().equals("[10, 30]") && 
			igrid.removeCol(1).toString().equals("[11, 33]") &&
			igrid.removeCol(0).toString().equals("[-10, -30]") &&
			igrid.getNumRow() == 0 && igrid.getNumCol() == 0 ){
			System.out.println("Yay 6");	
		}
		
				
	}
	
}