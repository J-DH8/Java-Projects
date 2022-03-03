/**
 * 
 * Percolation
 * 
 * @author Ana Paula Centeno
 * @author Haolin (Daniel) Jin
 */

public class Percolation {

	private boolean[][] grid;          // gridSize by gridSize grid of sites; 
	                                   // true = open site, false = closed or blocked site
	private WeightedQuickUnionFind wquFind; // 
	private int 		gridSize;      // gridSize by gridSize is the size of the grid/system 
	private int         gridSquared;
	private int         virtualTop;    // virtual top    index on WeightedQuckUnionFind arrays
	private int         virtualBottom; // virtual bottom index on WeightedQuckUnionFind arrays

	/**
	* Constructor.
	* Initializes all instance variables
	*/
	public Percolation ( int n ){
		gridSize 	  = n;
		gridSquared   = gridSize * gridSize;   //nxn how many total sites
		wquFind       = new WeightedQuickUnionFind(gridSquared + 2); //(nxn+2) two more than the row&col
		grid          = new boolean[gridSize][gridSize];   // every site is initialized to closed/blocked  to false
		virtualTop    = gridSquared;  //equals nxn
		virtualBottom = gridSquared + 1;   //equals nxn+1 
		
	} 

	/**
	* Getter method for GridSize 
	* @return integer representing the size of the grid.
	*/
	public int getGridSize () {
		return gridSize;   //
	}

	/**
	 * Returns the grid array
	 * @return grid array
	 */
	public boolean[][] getGridArray () {
		return grid;    //the nxn with all values false
	}

	private int currentSite(int row, int col){
		// parent has 0 through n^2-1
		
													//goes 0,1,2... so (row,col)=row*n+col
													//(0,0)=0 (1,0)= 1*n+col  
													//ex. if n=5 (3,2)=(3*5)+2=  15+2=17, or for (4,0)=(4*5)+0=20
		int 	currentSite= (gridSize*row)+col;
		return currentSite; 
	}
	private boolean inBounds(int a, int b){
		if(0<=a && a<=(gridSize-1) && 0<=b && b<=(gridSize-1)){
			return true;
		} else{
			return false;
		}
	}

	/**
	* Open the site at postion (x,y) on the grid to true and add an edge
	* to any open neighbor (left, right, top, bottom) and/or top/bottom virtual sites
	* Note: diagonal sites are not neighbors
	*
	* @param row grid row
	* @param col grid column
	* @return void
	*/
	public void openSite (int row, int col) {

		if (grid[row][col] == false){   //if (x,y) is false
			grid[row][col]= true;  //  set (x,y) to true to open
		}
		// use wquFind.union to "connect" ---no to make one a root of the other
	
		if(row==0){    //if top 
			wquFind.union(virtualTop,currentSite(row,col)); //current site = row*n +col 
		}
		if(row==gridSize-1){  //if bottom  need -1 cuz its 0 to n-1 length
			wquFind.union(virtualBottom,currentSite(row,col)); //current site = row*n +col 
		}


		// check 4 directions 
		if(inBounds(row-1,col) && grid[row-1][col]==true){	//if site is in bounds and open  up
			wquFind.union(currentSite(row,col),currentSite(row-1,col));
		}
		if(inBounds(row,col-1) && grid[row][col-1]==true){	//if site is in bounds and open   left
			wquFind.union(currentSite(row,col),currentSite(row,col-1));
		}
		if(inBounds(row+1,col) && grid[row+1][col]==true){	//if site is in bounds and open   down
			wquFind.union(currentSite(row,col),currentSite(row+1,col));
		}
		if(inBounds(row,col+1) && grid[row][col+1]==true){	//if site is in bounds and open   right
			wquFind.union(currentSite(row,col),currentSite(row,col+1));
		}
				return;  // back into openAllSites
	}

	/**
	* Check if the system percolates (any top and bottom sites are connected by open sites)
	* @return true if system percolates, false otherwise
	*/
	public boolean percolationCheck () {
			// checks if top site meets bottom site using (wquFind.find)
			if(wquFind.find(virtualTop)==wquFind.find(virtualBottom)){
				return true;
			} else{ //does not meet
				return false;
			}
	}

	/**
	 * Iterates over the grid array openning every site. 
	 * Starts at [0][0] and moves row wise 
	 * @param probability
	 * @param seed
	 */
	public void openAllSites (double probability, long seed) {

		// Setting the same seed before generating random numbers ensures that
		// the same numbers are generated between different runs
		StdRandom.setSeed(seed); // DO NOT remove this line
			
		for(int i=0; i< gridSize;i++){	
			for(int j=0;j< gridSize;j++){
					// test probability, if probability passes open it, if not do nothing
					double test= StdRandom.uniform(); //makes the test's double
					if(test<probability){
						openSite(i, j);  	
					}
			}
		}
		
	}

	/**
	* Open up a new window and display the current grid using StdDraw library.
	* The output will be colored based on the grid array. Blue for open site, black for closed site.
	* @return: void 
	*/
	public void displayGrid () {
		double blockSize = 0.9 / gridSize;
		double zeroPt =  0.05+(blockSize/2), x = zeroPt, y = zeroPt;

		for ( int i = gridSize-1; i >= 0; i-- ) {
			x = zeroPt;
			for ( int j = 0; j < gridSize; j++) {
				if ( grid[i][j] ) {
					StdDraw.setPenColor( StdDraw.BOOK_LIGHT_BLUE );
					StdDraw.filledSquare( x, y ,blockSize/2);
					StdDraw.setPenColor( StdDraw.BLACK);
					StdDraw.square( x, y ,blockSize/2);		
				} else {
					StdDraw.filledSquare( x, y ,blockSize/2);
				}
				x += blockSize; 
			}
			y += blockSize;
		}
	} 

	/**
	* Main method, for testing only, feel free to change it.
	*/
	public static void main ( String[] args ) {

		double p = 0.47;
		Percolation pl = new Percolation(9);  //test here

		/* 
		 * Setting a seed before generating random numbers ensure that
		 * the same numbers are generated between runs.
		 *
		 * If you would like to reproduce Autolab's output, update
		 * the seed variable to the value Autolab has used.
		 */
		long seed = System.currentTimeMillis();
		pl.openAllSites(p, seed);
	
		System.out.println("The system percolates: " + pl.percolationCheck());
		pl.displayGrid();
	}
}