package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.stream.Stream;

public class SimulatedAnnealing 
{
	private final int NUMCHARTOREAD = 500;
	private char[][] digraph;
	private int[] rowIndices;
	private int[] columnIndices;
	private PlayfairCypher cypher;
	private String txt="";
	FourGramDictionary dic;
	
	public SimulatedAnnealing(String txtPath, String dicPath)
	{
		txt=readText(txtPath);
		dic= new FourGramDictionary(dicPath);
		//System.out.println("Finish parsing "+dic.NGramScore("AACX"));
	}
	
	private String ShuffleKey()
	{
		int a= (int) (Math.random() * 100);
		if(a<91)
		{
			SwapRows();
		}
		else if(a<93)
		{
			SwapColumns();
		}
		else if(a<95)
		{
			FlipAllRows();
		}
		else if(a<97)
		{
			FlipAllColumns();
		}
		else if(a<99)
		{
			ReverseKey();
		}
		else
		{
			SwapSingleLetters(1);
		}
		
		return cypher.toString();
	}
	
	public void Execute()
	{
		double score = 0;
		double delta = 0;
		String decryptedText="";
		String key = KeyGenerator.GenerateKey();
		cypher=new PlayfairCypher(key);
		
		this.rowIndices=cypher.getRowIndexes();
		this.columnIndices=cypher.getColumnIndices();
		this.digraph=cypher.getDigraph();
		
	
		decryptedText=cypher.Decrypt(txt);
		score=Score(decryptedText);
		
		PlayfairCypher aux;
		for (int temp = 10; temp >0 ; temp--) 
		{
			for (int i = 50000; i > 0; i--) 
			{
				String newKey=ShuffleKey();
				aux=new PlayfairCypher(newKey);
				double newScore = Score(aux.Decrypt(txt));
				delta = newScore - score;
				
				if(delta > 0)
				{
					cypher=aux;
					score=newScore;
					System.out.println(newKey);
					System.out.println(score);
				}
				else 
				{
					double prob = Math.pow(Math.E, (-delta/temp));
					if(prob>0.5)
					{
						cypher=aux;
						score=newScore;
					}
				}
			}
		}
		
		System.out.println(cypher.toString());
		System.out.println(cypher.Decrypt(txt));
	}
	
	private String readText(String path)
	{
		try 
		{
			BufferedReader reader=new BufferedReader(new FileReader(path));
			String line="";
			
			while((line=reader.readLine())!=null&&txt.length()<600)
			{
				if(line.charAt(0)=='*')
				{
					for (int i = 0; i < 16; i++) //advancing the buffer to the start of the book
					{
						line=reader.readLine();
					}
					continue;
					
				}
				txt+=line.substring(0,600);
				System.out.println(txt.length());
			}
			
			reader.close();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return txt;
	}
	
	private double Score(String txt)
	{
		double score = 0.0;
		String aux="";
		for (int i = 0; i < txt.length()-3; i++) 
		{
			aux=""+txt.charAt(i)+txt.charAt(i+1)+txt.charAt(i+2)+txt.charAt(i+3);
			score+=Math.log10(dic.NGramScore(aux));
		}
		return score;
	}

	private void SwapSingleLetters(int numberOfChanges)
	{
		Random r=null;
		try {
		r = new Random();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
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
			
			rowIndices[a-65] ^= rowIndices[b-65];
			rowIndices[b-65] ^= rowIndices[a-65];
			rowIndices[a-65] ^= rowIndices[b-65];

			columnIndices[a-65] ^= columnIndices[b-65];
			columnIndices[b-65] ^= columnIndices[a-65];
			columnIndices[a-65] ^= columnIndices[b-65];
			
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
			
			rowIndices[a-65] ^= rowIndices[b-65];
			rowIndices[b-65] ^= rowIndices[a-65];
			rowIndices[a-65] ^= rowIndices[b-65];

			columnIndices[a-65] ^= columnIndices[b-65];
			columnIndices[b-65] ^= columnIndices[a-65];
			columnIndices[a-65] ^= columnIndices[b-65];
			
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

	private void ReverseKey() //Skips changing middle char in a row since it will stay at the same place
	{
		int backi=4;
		int backj=4;
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 2; j++) 
			{
				char a,b;
				digraph[i][j]^=digraph[backi][backj];
				b=digraph[backi][backj]^=digraph[i][j];
				a=digraph[i][j]^=digraph[backi][backj];
				
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[b-65]^=rowIndices[a-65];
				rowIndices[a-65]^=rowIndices[b-65];
				
				columnIndices[a-65]^=columnIndices[b-65];
				columnIndices[b-65]^=columnIndices[a-65];
				columnIndices[a-65]^=columnIndices[b-65];
				
				backj--;
			}
			backj=4;
			backi--;
		}
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);
	}

	private void FlipAllRows()
	{
		int backj=4;
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 3; j++) 
			{
				char a,b;
				digraph[i][j]^=digraph[i][backj];
				digraph[i][backj]^=digraph[i][j];
				digraph[i][j]^=digraph[i][backj];
				
				System.out.println(digraph[i][j]);
				b=digraph[i][j];
				System.out.println(digraph[i][backj]);
				a=digraph[i][backj];
				
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[b-65]^=rowIndices[a-65];
				rowIndices[a-65]^=rowIndices[b-65];
				
				columnIndices[a-65]^=columnIndices[b-65];
				columnIndices[b-65]^=columnIndices[a-65];
				columnIndices[a-65]^=columnIndices[b-65];
				backj--;
			}
			backj=4;
		}
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);
	}

	private void FlipAllColumns()
	{
		int backi=4;
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 3; j++) 
			{
				char a,b;
				digraph[j][i]^=digraph[backi][i];
				digraph[backi][i]^=digraph[j][i];
				digraph[j][i]^=digraph[backi][i];
				try 
				{
					System.out.println("FlipAllColumns " +digraph[i][j]);
					System.out.println("FlipAllColumns "+digraph[i][backi]);
					
					a=digraph[i][backi];
					b=digraph[i][j];
					
					rowIndices[a-65]^=rowIndices[b-65];
					rowIndices[b-65]^=rowIndices[a-65];
					rowIndices[a-65]^=rowIndices[b-65];
					
					columnIndices[a-65]^=columnIndices[b-65];
					columnIndices[b-65]^=columnIndices[a-65];
					columnIndices[a-65]^=columnIndices[b-65];
				} catch (Exception e) 
				{
					cypher.printMatrix();
					System.out.println(e.getMessage());
					
				}
				/*a=digraph[i][backi];
				b=digraph[i][j];
				
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[b-65]^=rowIndices[a-65];
				rowIndices[a-65]^=rowIndices[b-65];
				
				columnIndices[a-65]^=columnIndices[b-65];
				columnIndices[b-65]^=columnIndices[a-65];
				columnIndices[a-65]^=columnIndices[b-65];*/
				backi--;
			}
			backi=4;
		}
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);
	}

	public static void main(String[] args)
	{
		SimulatedAnnealing sm = new SimulatedAnnealing("D:\\Hobbit.txt","D:\\4grams.txt");
		sm.Execute();
	}
}
