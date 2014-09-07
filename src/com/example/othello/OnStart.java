package com.example.othello;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class OnStart extends Activity {
	int currentPlayer = 0;//0 for player 1(white) and 1 for player2(black)
	int row;
	int column;
	boolean name1edited = false;
	boolean name2edited = false;
	boolean done = false;//to check is game is over or not
	Map<Integer,Integer> valid = new HashMap<Integer,Integer>();//map to store valid positions
	private static int[][] board = new int[10][10]; // matrix of 10 x 10 
	
	public void genOthelloBoard(){
		//generating Othello Board
		for(int i = 0;i<10;i++){
			for(int j = 0;j<10;j++ ){
				board[i][j] = -1;
			}
		}
		board[4][4] = 0;
		board[4][5] = 1;
		board[5][4] = 1;
		board[5][5] = 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_start);
		Toast t = Toast.makeText(this, "Touch to Continue", Toast.LENGTH_SHORT);
		t.show();
	}
	
	public void PlayerInfoPage(View view){
		setContentView(R.layout.activity_player_info);
	}
	
	
	EditText name1;//Player 1 name field
	EditText name2;//Player 2 name field
	public void EditNameClick(View view){
		name1 = (EditText) findViewById(R.id.playername1);
		name2 = (EditText) findViewById(R.id.playername2);
		Button start = (Button) findViewById(R.id.startbutt);
		if(view.getTag() == name1.getTag()){
			// deleting the default text field
			if(!name1edited){
				name1.setText("");
			}
			name1edited = true;
		}
		else if(view.getTag() == name2.getTag()){
			// deleting the default text field
			if(!name2edited){
				name2.setText("");
			}
			name2edited = true;
		}
		if(name1edited == true && name2edited == true);
		start.setEnabled(true);
	}
	
	public void game(View view){
		setContentView(R.layout.activity_game);
		start();
			
	}
	RadioButton player1;
	RadioButton player2;
	private void start() {
		//update the scores and player info for the first time
		genOthelloBoard();
		player1 = (RadioButton) findViewById(R.id.player1radio);
		player2 = (RadioButton) findViewById(R.id.player2radio);
		player1.setText(name1.getText().toString());
		player2.setText(name2.getText().toString());
		checkValidMoves(currentPlayer);
		updateBoard();		
	}
	
	public void onButtonClick(View view){
		// using tag to get row and column example using tag == 11 for image button at (1,1) 
		int tagiId = Integer.parseInt(view.getTag().toString());
		row = tagiId/10;
		column = tagiId%10;
		//make changes
		makeChanges(currentPlayer, row, column);
		updateBoard();
		//changes the current player
		currentPlayer = currentPlayer == 0?1:0;
		//get valid moves for new player
		checkValidMoves(currentPlayer);
		updateBoard();
		updateData();
		checkWinners();
		
	}
	TextView score1;
	TextView score2;
	private void updateData() {
		score1 = (TextView) findViewById(R.id.player1score);
		score2 = (TextView) findViewById(R.id.player2score);
		//checks the current player
		if(currentPlayer == 0){
			player1.setChecked(true);
			player2.setChecked(false);
		}
		else{
			player2.setChecked(true);
			player1.setChecked(false);
		}
		// updates score
		Integer white = countValue(0);
		score1.setText(white.toString());
		Integer black = countValue(1);
		score2.setText(black.toString());
	}
	public void checkWinners(){
		//check winner by checking if none of the players have valid moves  
		if(valid.isEmpty()){
			if(done == true){
				if(countValue(0) >  countValue(1)){
					Toast t = Toast.makeText(this, name1.getText().toString() + " Wins", Toast.LENGTH_SHORT);
					t.show();
				}
				else if(countValue(0) <  countValue(1)){
					Toast t = Toast.makeText(this, name2.getText().toString() + " Wins", Toast.LENGTH_SHORT);
					t.show();
				}
				else {
					Toast t = Toast.makeText(this, "Draw Match", Toast.LENGTH_SHORT);
					t.show();
				}
				Toast t1 = Toast.makeText(this, "Developed by Rohan Arora with love", Toast.LENGTH_LONG);
				t1.show();
			}
			else{
				String s;
				s = currentPlayer == 0?name1.getText().toString():name2.getText().toString();
				Toast t = Toast.makeText(this, s+" Pass", Toast.LENGTH_SHORT);
				t.show();
				done = true;
				currentPlayer = currentPlayer == 0?1:0;
				checkValidMoves(currentPlayer);
				updateBoard();
				updateData();
				checkWinners();
			}
		}
		else{
			done = false;
		}
	}

	private void updateBoard() {
		//updates the board
		// -1 for empty tile
		//0 for white
		//1 for black
		//2 for valid moves
		GridLayout gl = (GridLayout) findViewById(R.id.grid);
		for(int i = 1;i<=8;i++){
			for(int j = 1;j<=8;j++){
				ImageButton ib = (ImageButton) gl.findViewWithTag(((Integer)(10*i+j)).toString());
				
				if(valid.containsKey(10*i+j)){
					ib.setEnabled(true);
					ib.setAlpha(1F);
				}
				else{
					if(board[i][j] == 2){
						board[i][j] = -1;
					}
					ib.setEnabled(false);
					ib.setAlpha(0.6F);
				}
				if(board[i][j] == -1){
					ib.setImageResource(R.drawable.green_dark);
				}
				else if(board[i][j] == 0){
					ib.setImageResource(R.drawable.white_gree_dark);
				}
				else if(board[i][j] == 1){
					ib.setImageResource(R.drawable.black_green_dark);
				}
				else
					ib.setImageResource(R.drawable.green_dark_valid);
			}
		}
		
	}

	public void checkValidMoves(int value){
		valid.clear();
		int[] x = {1,-1,1,-1,1,-1,0,0};
		int[] y = {0,0,1,-1,-1,1,1,-1};
		for(int m = 1;m<=8;m++){
			for(int n = 1;n<=8;n++){
				if(board[m][n] == -1 || board[m][n] == 2){
					for(int i = 0;i<8;i++){
						int j = m;
						int k = n;
						boolean flipped = false;
						while ((j<9&&j>0) && (k>0&&k <9)){
							if(j > 8 && x[i] == 1)
								break;
							if(k > 8 && y[i] == 1)
								break;
							if(j < 1 && x[i] == -1)
								break;
							if(k < 1 && y[i] == -1)
								break;
							j += x[i];
							k += y[i];
							if(board[j][k] == -1 || board[j][k] == 2){
								break;
							}
							else if(board[j][k] == value){
								if(flipped){
									valid.put(10*m+n, 10*n+m);
									board[m][n] = 2;
								}
								break;
							}
							else {
								flipped = true;
							}
						}
					}				
				}
			}
		}
	}

	public void makeChanges(int value,int row,int column){
		board[row][column] = value;
		int[] x = {1,-1,1,-1,1,-1,0,0};
		int[] y = {0,0,1,-1,-1,1,1,-1};
		int temp = value == 0?1:0;
		for(int i = 0;i<8;i++){
			int j = row;
			int k = column;
			boolean flipped = false;
			try{
				while ((j<9&&j>0) && (k>0&&k <9)){
					if(j > 8 && x[i] == 1)
						break;
					if(k > 8 && y[i] == 1)
						break;
					if(j < 1 && x[i] == -1)
						break;
					if(k < 1 && y[i] == -1)
						break;
					j += x[i];
					k += y[i];
					if(board[j][k] == -1 || board[j][k] == 2){
						throw new InvalidOthelloException();
					}
					else if(board[j][k] == value){
						if(!flipped)
							throw new InvalidOthelloException();
						break;
					}
					else{
						board[j][k] = value;
						flipped = true;
					}
				}
			}
			catch (InvalidOthelloException ex){
				j -= x[i];
				k -= y[i];
				while(j!=row || k!= column){
					board[j][k] = temp;
					j -= x[i];
					k -= y[i];
				}
			}
		}
	}
	public int countValue(int value){
		int temp = 0;
		for(int i = 1;i<9;i++){
			for(int j = 1;j<9;j++){
				if(board[i][j] == value)
					temp++;
			}
		}
		return temp;
	}
}
