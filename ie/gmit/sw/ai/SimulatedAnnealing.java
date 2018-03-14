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
	
	private String ShuffleKey(String key)
	{
		int a= (int) (Math.random() * 100);
		String aux;
		switch(a)
		{
		case 1:
		case 2:
			aux=ReverseKey(key);
			break;
		case 3:
		case 4:
			aux=SwapColumns(key);
			break;
		case 5:
		case 6:
			aux=SwapRows(key.toCharArray());
			break;
		case 7:
		case 8:
			aux=FlipAllColumns(key);
			break;
		case 9:
		case 10:
			aux=FlipAllRows(key);
			break;
			
		default:
			aux=SwapSingleLetters(key.toCharArray());
			break;
		}
		/*if(a<89)
		{
			SwapSingleLetters(1);
		}
		else if(a<91)
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
		else //if(a<99)
		{
			ReverseKey();
		}*/
		
		return aux;
	}
	
	public void Execute()
	{
		double score = 0;
		double delta = 0;
		String decryptedText="";
		String key = KeyGenerator.GenerateKey();
		cypher=new PlayfairCypher(key);
		
		//cypher.printMatrix();
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
				String newKey=ShuffleKey(key);
				aux=new PlayfairCypher(newKey);
				double newScore = Score(aux.Decrypt(txt));
				delta = newScore - score;
				if(delta > 0)
				{
					System.out.println(score);
					System.out.println(newScore);
					//cypher=aux;
					key=newKey;
					score=newScore;
					System.out.println(newKey);
					
				}
				else 
				{
					Random r = new Random();
					double prob = Math.pow(Math.E,-delta/temp);
					if(prob>r.nextInt(100)/100)
					{
						key=newKey;
						//cypher=aux;
						score=newScore;
					}
					
				}
				
				//System.out.println(score);
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
			score+=dic.NGramScore(aux);
		}
		return score;
	}

	private String SwapSingleLetters(char[] key)
	{
		Random r=  new Random();
		int n1= r.nextInt(25);
		int n2= r.nextInt(25);
		char temp = key[n1];
	    key[n1]= key[n2];
	    key[n2] = temp;
		
		//cypher.printMatrix();
		
		/*digraph=cypher.getDigraph();
		rowIndices=cypher.getRowIndexes();
		columnIndices=cypher.getColumnIndices();
		char [] aux = cypher.toString().toCharArray();
		//System.out.println("FromSwapLetter");
		for (int i = 0; i < numberOfChanges; i++) 
		{
			int n1= r.nextInt(25);
			int n2= r.nextInt(25);
			while(n1==n2) n2=r.nextInt(25);
			
			aux[n1]^=aux[n2];
			aux[n2]^=aux[n1];
			aux[n1]^=aux[n2];
			/*while(n1==9) n1= r.nextInt(25);
			
			while(n2==9) n2= r.nextInt(25);
			
			while(n2==n1||n2!=9) n2= r.nextInt(25);
			System.out.println(n1);
			System.out.println(n2);
			digraph[rowIndices[n1]][columnIndices[n1]]^=digraph[rowIndices[n2]][columnIndices[n2]];
			digraph[rowIndices[n2]][columnIndices[n2]]^=digraph[rowIndices[n1]][columnIndices[n1]];
			digraph[rowIndices[n1]][columnIndices[n1]]^=digraph[rowIndices[n2]][columnIndices[n2]];
			*/
			/*rowIndices[n1]^=rowIndices[n2];
			rowIndices[n2]^=rowIndices[n1];
			rowIndices[n1]^=rowIndices[n2];
			
			columnIndices[n1]^=columnIndices[n2];
			columnIndices[n2]^=columnIndices[n1];
			columnIndices[n1]^=columnIndices[n2];*/
		
		
		
		/*cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);*/
		//System.out.println(cypher.toString());
		
		//cypher= new PlayfairCypher(String.valueOf(key));
		
		//cypher.printMatrix();
		//System.out.println("FromSwapLetter");
		//rowIndices = cypher.getRowIndexes();
		//columnIndices = cypher.getColumnIndices();
	    
	    return String.valueOf(key);
	}

	private String SwapRows(char[] key)
	{
		//cypher.printMatrix();
		Random r = new Random();

		int i = r.nextInt(5);
		int j = r.nextInt(5);
		
		char temp;
		for(int k=0;k<5;k++)
		{
	        temp = key[i*5 + k];
	        key[i*5 + k] = key[j*5 + k];
	        key[j*5 + k] = temp;
	    }
		/*char a,b;
		
		while(r1==r2) r2 = r.nextInt(5);
		
		digraph=cypher.getDigraph();
		//System.out.println("Key before row swap: "+cypher.toString());
		//for (int i = 0; i < 5; i++) 
		//{
			char[] temp = digraph[r1];
			digraph[r1]=digraph[r2];
			digraph[r2]=temp;
		//}
		cypher.setDigraph(digraph);
		//System.out.println("Key after row swap: "+cypher.toString());
		
		cypher = new PlayfairCypher(cypher.toString());
		
		rowIndices = cypher.getRowIndexes();
		columnIndices = cypher.getColumnIndices();
		/*char[] temp = digraph[r1];
		digraph[r1]=digraph[r2];
		digraph[r2]=temp;*/
		
		/*for (int i = 0; i < 5; i++) 
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
		
		
		cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);*/
		
		//cypher=new PlayfairCypher(String.valueOf(key));
		
		return String.valueOf(key);
	}

	private String SwapColumns(String key)
	{
		//cypher.printMatrix();
		Random r = new Random();

		int i = r.nextInt(5);
		int j = r.nextInt(5);
		char[] tempKey = key.toCharArray();
		char temp;
		for(int k=0;k<5;k++)
		{
	        temp = tempKey[i*5 + k];
	        tempKey[i*5 + k] = tempKey[j*5 + k];
	        tempKey[j*5 + k] = temp;
	    }
		
		
		/*char[] temp = digraph[r1];
		digraph[r1]=digraph[r2];
		digraph[r2]=temp;*/
		/*digraph=cypher.getDigraph();
		rowIndices=cypher.getRowIndexes();
		columnIndices=cypher.getColumnIndices();
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
		}
		
		/*cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);*/
		
		//cypher = new PlayfairCypher(String.valueOf(tempKey));
		
		return String.valueOf(tempKey);
	}

	private String ReverseKey(String key) //Skips changing middle char in a row since it will stay at the same place
	{
		String aux="";
		//String key = cypher.toString();
		//System.out.println("key :"+key);
		for (int i = key.length()-1; i > -1; i--) 
		{
			aux+=key.charAt(i);
		}
		//FlipAllRows();
		//FlipAllColumns();
		/*cypher.printMatrix();
		int backi=4;
		int backj=4;
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 2; j++) 
			{
				char a,b,tsta,tstb;
				
				tsta=digraph[i][j];
				tstb=digraph[backi][backj];
				
				digraph[i][j]^=digraph[backi][backj];
				digraph[backi][backj]^=digraph[i][j];
				digraph[i][j]^=digraph[backi][backj];
				
				b=digraph[backi][backj];
				a=digraph[i][j];
				
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
		cypher.setRowIndexes(rowIndices);*/
		
		//cypher=new PlayfairCypher(aux);
		//rowIndices = cypher.getRowIndexes();
		//columnIndices = cypher.getColumnIndices();
		
		return String.valueOf(aux);
	}

	private String FlipAllRows(String key)
	{
		PlayfairCypher aux = new PlayfairCypher(key);
		digraph = aux.getDigraph();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 2; j++) 
			{
				char a,b;
				digraph[i][j]^=digraph[i][4-j];
				digraph[i][4-j]^=digraph[i][j];
				digraph[i][j]^=digraph[i][4-j];
				
				/*System.out.println(digraph[i][j]);
				b=digraph[i][j];
				System.out.println(digraph[i][4-j]);
				a=digraph[i][4-j];
				
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[b-65]^=rowIndices[a-65];
				
				
				columnIndices[a-65]^=columnIndices[b-65];
				columnIndices[b-65]^=columnIndices[a-65];
				columnIndices[a-65]^=columnIndices[b-65];*/
			}
		}
		
		/*cypher.setColumnIndices(columnIndices);
		cypher.setDigraph(digraph);
		cypher.setRowIndexes(rowIndices);*/
		/*cypher= new PlayfairCypher(cypher.toString());
		rowIndices = cypher.getRowIndexes();
		columnIndices = cypher.getColumnIndices();*/
		aux.setDigraph(digraph);
		return aux.toString();
	}

	private String FlipAllColumns(String key)
	{
		PlayfairCypher temp = new PlayfairCypher(key);
		digraph = temp.getDigraph();
		//cypher.printMatrix();
		char[] aux;
		for (int i = 0; i < 3; i++)
		{
			aux=digraph[4-i];
			digraph[4-i]=digraph[i];
			digraph[i]=aux;
		}
		//cypher.printMatrix();
		/*int backi=4;
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 3; j++) 
			{
				char a,b;
				
				digraph[j][i]^=digraph[backi][i];
				digraph[backi][i]^=digraph[j][i];
				digraph[j][i]^=digraph[backi][i];
				
				a=digraph[i][backi];
				b=digraph[i][j];

				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[b-65]^=rowIndices[a-65];
				rowIndices[a-65]^=rowIndices[b-65];

				columnIndices[a-65]^=columnIndices[b-65];
				columnIndices[b-65]^=columnIndices[a-65];
				columnIndices[a-65]^=columnIndices[b-65];
				
				/*a=digraph[i][backi];
				b=digraph[i][j];
				
				rowIndices[a-65]^=rowIndices[b-65];
				rowIndices[b-65]^=rowIndices[a-65];
				rowIndices[a-65]^=rowIndices[b-65];
				
				columnIndices[a-65]^=columnIndices[b-65];
				columnIndices[b-65]^=columnIndices[a-65];
				columnIndices[a-65]^=columnIndices[b-65];*/
				/*backi--;
			}
			backi=4;
		}*/
		
		//cypher.setColumnIndices(columnIndices);
		//cypher.setDigraph(digraph);
		//cypher.setRowIndexes(rowIndices);
		/*cypher= new PlayfairCypher(cypher.toString());
		rowIndices = cypher.getRowIndexes();
		columnIndices = cypher.getColumnIndices();*/
		
		temp.setDigraph(digraph);
		
		return temp.toString();
	}

	public static void main(String[] args)
	{
		SimulatedAnnealing sm = new SimulatedAnnealing("D:\\Hobbit.txt","D:\\4grams.txt");
		sm.Execute();
	}
}
