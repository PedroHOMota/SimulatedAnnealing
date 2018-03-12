package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.stream.Stream;

public class SimulatedAnnealing 
{
	private final int NUMCHARTOREAD = 600;
	private char[][] digraph;
	private int[] rowIndices;
	private int[] columnIndices;
	private PlayfairCypher cypher;
	private String txt;
	private float score=0;
	
	public SimulatedAnnealing(char[][] digraph,int[] rowIndices,int[] columnIndices,String path)
	{
		this.rowIndices=rowIndices;
		this.columnIndices=columnIndices;
		this.digraph=digraph;
	}
	
	private String ShuffleKey()
	{
		int a= (int) (Math.random() * 100);
		if(a<91)
		{
			SwapSingleLetters(1);
		}
		else if(a<93)
		{
			SwapRows();
		}
		else if(a<95)
		{}
		else if(a<97)
		{}
		else if(a<99)
		{}
		else
		{}
		return "";
	}
	
	public void Execute()
	{
		String decryptedText="";
		char[] key = KeyGenerator.GenerateKey();
		cypher=new PlayfairCypher(key.toString());
		decryptedText=cypher.Decrypt(txt);
		score=Score(decryptedText);
		
		for (int temp = 10; temp >0 ; temp--) 
		{
			for (int i = 50000; i > 0; i--) 
			{
				String newKey=ShuffleKey();
				
			}
		}
	}
	
	private String readText(String path)
	{
		try 
		{
			BufferedReader reader=new BufferedReader(new FileReader(path));
			String line="";
			
			while((line=reader.readLine())!=null||txt.length()<NUMCHARTOREAD)
			{
				txt+=line;
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	private float Score(String txt)
	{
		
		return 0.0f;
	}

	private void SwapSingleLetters(int numberOfChanges)
	{
		Random r = new Random();

		for (int i = 0; i < numberOfChanges; i++) 
		{
			int n1= r.nextInt(25);
			int n2= r.nextInt(25);
			
			digraph[rowIndices[n1]][columnIndices[n1]]^=digraph[rowIndices[n2]][columnIndices[n2]];
			digraph[rowIndices[n2]][columnIndices[n2]]^=digraph[rowIndices[n1]][columnIndices[n1]];
			digraph[rowIndices[n1]][columnIndices[n1]]^=digraph[rowIndices[n2]][columnIndices[n2]];
			
			rowIndices[n1]^=rowIndices[n2];
			rowIndices[n2]^=rowIndices[n1];
			rowIndices[n1]^=rowIndices[n2];
			
			columnIndices[n1]^=columnIndices[n2];
			columnIndices[n2]^=columnIndices[n1];
			columnIndices[n1]^=columnIndices[n2];
		}
		
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);
	}

	private void SwapRows()
	{
		Random r = new Random();

		int r1 = r.nextInt(5);
		int r2 = r.nextInt(5);
		char a,b;
		/*char[] temp = digraph[r1];
		digraph[r1]=digraph[r2];
		digraph[r2]=temp;*/
		
		for (int i = 0; i < 5; i++) 
		{
			a=digraph[r1][i];
			b=digraph[r2][i];
			
			rowIndices[a] ^= rowIndices[b];
			rowIndices[b] ^= rowIndices[a];
			rowIndices[a] ^= rowIndices[b];

			columnIndices[a] ^= columnIndices[b];
			columnIndices[b] ^= columnIndices[a];
			columnIndices[a] ^= columnIndices[b];
			
			digraph[r1][i]=b;
			digraph[r2][i]=a;
			//digraph[r1][i]
		}
		/*digraph[rowIndices[n1]][columnIndices[n1]] ^= digraph[rowIndices[n2]][columnIndices[n2]];
		digraph[rowIndices[n2]][columnIndices[n2]] ^= digraph[rowIndices[n1]][columnIndices[n1]];
		digraph[rowIndices[n1]][columnIndices[n1]] ^= digraph[rowIndices[n2]][columnIndices[n2]];

		rowIndices[n1] ^= rowIndices[n2];
		rowIndices[n2] ^= rowIndices[n1];
		rowIndices[n1] ^= rowIndices[n2];

		columnIndices[n1] ^= columnIndices[n2];
		columnIndices[n2] ^= columnIndices[n1];
		columnIndices[n1] ^= columnIndices[n2];*/
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);
	}

	private void SwapColumns()
	{
		Random r = new Random();

		int r1 = r.nextInt(5);
		int r2 = r.nextInt(5);
		char a,b;
		/*char[] temp = digraph[r1];
		digraph[r1]=digraph[r2];
		digraph[r2]=temp;*/
		
		for (int i = 0; i < 5; i++) 
		{
			a=digraph[i][r1];
			b=digraph[i][r2];
			
			rowIndices[a] ^= rowIndices[b];
			rowIndices[b] ^= rowIndices[a];
			rowIndices[a] ^= rowIndices[b];

			columnIndices[a] ^= columnIndices[b];
			columnIndices[b] ^= columnIndices[a];
			columnIndices[a] ^= columnIndices[b];
			
			digraph[i][r1]=b;
			digraph[i][r2]=a;
			//digraph[r1][i]
		}
		/*digraph[rowIndices[n1]][columnIndices[n1]] ^= digraph[rowIndices[n2]][columnIndices[n2]];
		digraph[rowIndices[n2]][columnIndices[n2]] ^= digraph[rowIndices[n1]][columnIndices[n1]];
		digraph[rowIndices[n1]][columnIndices[n1]] ^= digraph[rowIndices[n2]][columnIndices[n2]];

		rowIndices[n1] ^= rowIndices[n2];
		rowIndices[n2] ^= rowIndices[n1];
		rowIndices[n1] ^= rowIndices[n2];

		columnIndices[n1] ^= columnIndices[n2];
		columnIndices[n2] ^= columnIndices[n1];
		columnIndices[n1] ^= columnIndices[n2];*/
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);
	}
}
