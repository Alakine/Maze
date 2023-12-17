import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class JavaMaze extends JFrame{
	//Variables
	Stack<JButton> button = new Stack<>();
	private int SIZE = 30;
	private int percentOfWalls = (30 * (20 * 20)) / 100;
	int clicked = 0;
	int undo = 0;
	
	private JMenuBar mainBar;
	private JMenu game, settings, size, walls;
	private JMenuItem newGame, restartGame, showPath, exit;
	private JRadioButton size10, size20, size30, wall10, wall20, wall30;
	private JPanel panel;
	private JButton[][] Btn = new JButton[SIZE][SIZE];
	
	
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 600;
	
	public JavaMaze()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Maze Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		SIZE = 20;//Making the size 20 so the default can be 20X20
		
		panel = new JPanel();
		
		mainBar = new JMenuBar();
		game = new JMenu("Game");
		settings = new JMenu("settings");
		newGame = new JMenuItem("New Game");
		restartGame = new JMenuItem("Restart Game");
		showPath = new JMenuItem("Show Path");
		exit = new JMenuItem("Exit");
		size = new JMenu("Size");
		walls = new JMenu("walls");
		size10 = new JRadioButton("10 X 10");
		size20 = new JRadioButton("20 X 20");
		size30 = new JRadioButton("30 X 30");
		wall10 = new JRadioButton("10%");
		wall20 = new JRadioButton("20%");
		wall30 = new JRadioButton("30%");
		//Create the button groups
		ButtonGroup group = new ButtonGroup();
		ButtonGroup group2 = new ButtonGroup();
		group.add(size10);
		group.add(size20);
		group.add(size30);
		
		group2.add(wall10);
		group2.add(wall20);
		group2.add(wall30);
		
		size20.doClick();
		wall30.doClick();
		//Add the buttons to the menu
		size.add(size10);
		size.add(size20);
		size.add(size30);
		
		walls.add(wall10);
		walls.add(wall20);
		walls.add(wall30);
		
		game.add(newGame);
		game.add(restartGame);
		game.add(showPath);
		game.add(exit);
		
		settings.add(size);
		settings.add(walls);
		
		mainBar.add(game);
		mainBar.add(settings);
		
		add(panel);
		
		setJMenuBar(mainBar);
		
		newGame.addActionListener(new MenuListener());
		restartGame.addActionListener(new MenuListener());
		showPath.addActionListener(new MenuListener());
		exit.addActionListener(new MenuListener());
		size10.addActionListener(new RadioButtonListener());
		size20.addActionListener(new RadioButtonListener());
		size30.addActionListener(new RadioButtonListener());
		wall10.addActionListener(new RadioButtonListener());
		wall20.addActionListener(new RadioButtonListener());
		wall30.addActionListener(new RadioButtonListener());
		
		setVisible(true);
	}
	
	public void createMaze(int newSize, int amountOfWalls)
	{
		SIZE = newSize;//sets SIZE to the new inputed size when newGame is clicked
		int min = 0, max = SIZE - 1;//find the min and the max
		//max is SIZE - 1 so it can adjust to any size
		panel.setLayout(new GridLayout(SIZE, SIZE));//panel grid will change with size
		
		for(int row = 0; row < SIZE; row++) //creating all the buttons as white
		{
			for(int col = 0; col < SIZE; col++)
			{
				Btn[row][col] = new JButton();
				Btn[row][col].setBackground(Color.white);
				panel.add(Btn[row][col]);
				
				Btn[row][col].addActionListener(new ButtonListener1());
			}
		}
		
		for(int wall = 0; wall < percentOfWalls; wall++)//randomly add walls
		{
			Random random = new Random();
			Random random2 = new Random();
			
			int makeWalls = random.nextInt(max - min + 1) + min;
			int makeWalls2 = random2.nextInt(max - min + 1) + min;
			
			for(int walls = 0; walls < percentOfWalls; walls++)
			{	
				Btn[makeWalls][makeWalls2].setBackground(Color.LIGHT_GRAY);
				Btn[makeWalls][makeWalls2].setEnabled(false);//walls can not be pressed
			}
			
		}
		
		Random enter = new Random();
		Random exit = new Random();
		
		int start = enter.nextInt(max - min + 1) + min;
		int end = enter.nextInt(max - min + 1) + min;
		
		Btn[start][0].setBackground(Color.blue);//randomly place the start within the first column
		Btn[end][SIZE - 1].setBackground(Color.green);//randomly place the end in the last column
		
		
		if(showPath(start, 0, end, SIZE - 1) == false)
		{
			createMaze(SIZE, percentOfWalls);
		}
		for(int row = 0; row < SIZE; row++)
		{
			for(int col = 0; col < SIZE; col++)
			{
				if(Btn[row][col].getBackground() == Color.cyan)
				{
					Btn[row][col].setBackground(Color.white);
				}
			}
		}
		
	}
	
	public Boolean showPath(int enterX, int enterY, int exitX, int exitY)
	{
		//Stacks to hold the coordinates
		Stack<Integer> x = new Stack<>();//X coordinate
		Stack<Integer> y = new Stack<>();//Y coordinate
		Boolean repeat[][] = new Boolean[SIZE][SIZE];//Keep track of repeated spots
		int[] locationX = {0, 1, 0, -1};//Moves the x coordinate up, down, left, and right
		int[] locationY = {1, 0, -1, 0};//Moves the y coordinate up, down, left, and right
		Boolean foundEnd, moved;//Check if destination is found
		int positionX, positionY, moveNextX, moveNextY;
		//pushing the starting positions
		x.push(enterX);
		y.push(enterY);
		Btn[enterX][enterY].setBackground(Color.blue);//keep the start blue
				
		for (int i = 0; i< SIZE; i++)
		{//initializes repeat
			Arrays.fill(repeat[i], false);
		}
		//Mark the start as visited		
		repeat[enterX][enterY] = true;
				
		foundEnd = false;
		//continue until the stacks are empty
		while (!x.isEmpty() && !y.isEmpty())
		{//Get the position from the top of the stack
			positionX = x.peek();
			positionY = y.peek();
			//Check if the position is the end
			if (positionX == exitX && positionY == exitY)
			{
				foundEnd = true;
				//mark the exit as green
				Btn[exitX][exitY].setBackground(Color.green);
				return true;
			}
			//Change moved to false		
			moved = false;
			//check all possible moves	
			for (int i = 0; i < 4; i++)
			{
				moveNextX = positionX + locationX[i];
				moveNextY = positionY + locationY[i];
				//System.out.println("dir" + moveNextX + ", " + moveNextY);
				//Check if the place has been visited and if the move is valid
				if (moveNextX >= 0 && moveNextX < SIZE && moveNextY >= 0 && moveNextY < SIZE && !repeat[moveNextX][moveNextY] && Btn[moveNextX][moveNextY].getBackground() != Color.LIGHT_GRAY)
				{
					if(path(positionX, positionY, moveNextX, moveNextY))
					x.push(moveNextX);//push new x location
					y.push(moveNextY);//push new y location
					//System.out.println("push" + moveNextX + ", " + moveNextY);
					repeat[moveNextX][moveNextY] = true;//mark the spot as visited
					Btn[moveNextX][moveNextY].setBackground(Color.cyan);//change the color of the spot
					moved = true;
					break;
				}
			}	
			//if no moves were made then backtrack		
			if(!moved)
			{
				//System.out.println("pop" + x.peek() + ", " + y.peek());
				x.pop();
				y.pop();
				//make the current spot white
				Btn[positionX][positionY].setBackground(Color.white);
			}
					
			
		}
				
		return false;
		
	}
	
	
	
	private Boolean path(int enterX, int enterY, int exitX, int exitY)
	{
		return Math.abs(enterX - exitX) + Math.abs(enterY - exitY) == 1;
	}
	
	
	private class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == newGame)//if New Game is clicked
			{
				panel.removeAll();//Removes all the contents from the panel
				createMaze(SIZE, percentOfWalls);//Creates the maze again
				panel.revalidate();//Tells the program to change the panel contents
			}
			else if(e.getSource() == restartGame)//if Restart Game is clicked
			{
				undo = 0;
				clicked = 0;
				for(int row = 0; row < SIZE; row++)
				{
					for(int col = 0; col < SIZE; col++)
					{
						if(Btn[row][col].getBackground() == Color.cyan)//if Btn is cyan
						{
							Btn[row][col].setBackground(Color.white);//turn it white
						}
					}
				}
				
				for(int i = 0; i < SIZE; i++)
				{
					for(int j = 0; j < SIZE; j++)
					{
						if(Btn[i][j].getBackground() != Color.LIGHT_GRAY)//reset the locked maze
						{
							Btn[i][j].setEnabled(true);
						}
					}
				}
				
			}
			else if(e.getSource() == showPath)//if Show Path is clicked
			{
				int enterX = 0, enterY = 0, exitX = 0, exitY = 0;
				
				for(int row = 0; row < SIZE; row++)
				{
					for(int col = 0; col < SIZE; col++)
					{
						if(Btn[row][col].getBackground() == Color.blue)//if the Btn is the start
						{
							enterX = row;
							enterY = col;
							break;
						}
					}
				}
				
				for(int row = 0; row < SIZE; row++)
				{
					for(int col = 0; col < SIZE; col++)
					{
						if(Btn[row][col].getBackground() == Color.green)//if the Btn is the end
						{
							exitX = row;
							exitY = col;
							break;
						}
					}
				}
				
				showPath(enterX, enterY, exitX, exitY);
			}
			else if(e.getSource() == exit)//if Exit is clicked
			{
				System.exit(0);//exit the program
			}
		}
	}
	
	private class RadioButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == size10)//if 10X10 is clicked
			{
				SIZE = 10;//change the size of the grid to 10
			}
			else if(e.getSource() == size20)//if 20X20 is clicked
			{
				SIZE = 20;//change the size of the grid to 20
			}
			else if(e.getSource() == size30)//if 30X30 is clicked
			{
				SIZE = 30;//change the size of the grid to 30
			}
			
			if(e.getSource() == wall10)//if 10% is clicked
			{
				percentOfWalls = (10 * SIZE * SIZE) / 100;//find 10% of the size
			}
			else if(e.getSource() == wall20)//if 20% is clicked
			{
				percentOfWalls = (20 * SIZE * SIZE) / 100;//find 20% of the walls
			}
			else if(e.getSource() == wall30)//if 30% is clicked
			{
				percentOfWalls = (30 * SIZE * SIZE) / 100;//find 30% of the walls
			}
			
		}
	}
	
	public class ButtonListener1 implements ActionListener
	{
		JButton lastClickedButton;
		
		public void actionPerformed(ActionEvent e)
		{
			JButton buttonClicked = (JButton)e.getSource();
			int positionX = 0, positionY = 0, newPositionX, newPositionY;
			
			
				if(buttonClicked.getBackground() == Color.white)//if the button clicked is white
				{
					
					if(button.isEmpty())
					{
						buttonClicked.setBackground(Color.cyan);//set it to cyan
					}
					
						clicked++;//incrememnt the amount of clicks
						button.push(buttonClicked);
						buttonClicked.setBackground(Color.cyan);
				
						for(int i = 0; i < SIZE - 1; i++)
						{
							for(int j = 0; j < SIZE - 1; j++)
							{
								if(Btn[i][j].getBackground() == Color.cyan)//if a Btn is cyan
								{
									if(validInput(i, j + 1) || validInput(i + 1, j) || validInput(i - 1, j) || validInput(i, j - 1))
									{
										//checks if the surrounding buttons are the start or also cyan
									
									}//end valid if
									else
									{
										buttonClicked.setBackground(Color.white);
										clicked--;
									}//end valid else
								}//end cyan if
							}//end j for
						}//end i for
					
						for(int i = 0; i < SIZE; i++)
						{
							for(int j = 0; j <SIZE; j++)
							{
								if(Btn[i][j] == button.peek())
								{
									positionX = i;
									positionY = j;
								
								}
								else
								{
									Btn[i][j].setEnabled(false);
								}
							}
						}
	
						for(int i = 0; i < 4; i++)
						{
							int[] locationX = {0, 1, 0, -1};//Moves the x coordinate up, down, left, and right
							int[] locationY = {1, 0, -1, 0};//Moves the y coordinate up, down, left, and right
						
							newPositionX = positionX + locationX[i];
							newPositionY = positionY + locationY[i];
						
							if(newPositionX >= 0 && newPositionX < SIZE && newPositionY >= 0 && newPositionY < SIZE && Btn[newPositionX][newPositionY].getBackground() != Color.LIGHT_GRAY)
							{
								Btn[newPositionX][newPositionY].setEnabled(true);
							}
						}
				}//end button white if
				else if(buttonClicked.getBackground() == Color.cyan)//undo end of button
				{
					if(!button.isEmpty() && clicked > 0)
					{
						button.pop();//pop from stack
						clicked -= 1;//delete aclick
						buttonClicked.setBackground(Color.white);//set button back to white
						button.peek().setEnabled(true);
						undo++;//incrememnt undo
						
					}
					
				}
			
				
			
						if(buttonClicked.getBackground() == Color.green)//if the end is found you won
						{
							for(int row = 0; row < SIZE; row++)
							{
								for(int col = 0; col < SIZE; col++)
								{
									if(Btn[row][col].getBackground() == Color.green)
									{
										if(validInput(row - 1, col) || validInput(row + 1, col) || validInput(row, col + 1) || validInput(row, col - 1))
										{
											JOptionPane.showMessageDialog(null, "You Won!\n You clicked : " + clicked + " times.\n" + "Number of undo: " + undo);
										}
									}
								}
							}
							//JOptionPane.showMessageDialog(null, "You Won!\n You clicked : " + clicked + " times.\n" + "Number of undo: " + undo);
						}//end button green if
				
		
		}//end of function
}//end of class
	
  
	public Boolean validInput(int row, int col)
	{
		//checks if the row and col are within range of the array
		if(row >= 0 && row < SIZE && col >= 0 && col < SIZE)
		{
			if(Btn[row][col].getBackground() == Color.cyan || Btn[row][col].getBackground() == Color.blue)
			{
				return true;
			}
			
		}
		return false;
	}
	


	public static void main(String[] args) {
		JavaMaze maze = new JavaMaze();

	}

}
