package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.stream.Stream;

public class SimulatedAnnealing 
{
	private final int NUMCHARTOREAD = 300;
	private char[][] digraph;
	private String txt="";
	FourGramDictionary dic;
	
	long startTime = System.currentTimeMillis();
	
	public SimulatedAnnealing(String txtPath, String dicPath)
	{
		txt=readText(txtPath);
		dic= new FourGramDictionary(dicPath);
	}
	
	private String ShuffleKey(String key)
	{
		int a= (int) (Math.random() * 100);
		String aux;
		//System.out.println("key before shuffle: "+key);
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
		
		//System.out.println("key after shuffle: "+aux);
		return aux;
	}
	
	public void Execute()
	{
		double score = 0;
		double delta = 0;
		String decryptedText="";
		String key = KeyGenerator.GenerateKey();
		PlayfairCypher cypher=new PlayfairCypher(key);
		
		this.digraph=cypher.getDigraph();
		
	
		decryptedText=cypher.Decrypt(txt);
		score=Score(decryptedText);
		int c=0;
		PlayfairCypher aux;
		for (int temp = 10; temp >0 ; temp--) 
		{
			for (int i = 30000; i > 0; i--) 
			{
				String newKey=ShuffleKey(key);
				aux=new PlayfairCypher(newKey);
				double newScore = Score(aux.Decrypt(txt));
				delta = newScore - score;
				if(delta > 0)
				{
					//System.out.println(score);
					//System.out.println(newScore);
					key=newKey;
					score=newScore;
					/*System.out.println("Old: "+key);
					System.out.println("New: "+newKey);*/
				}
				else 
				{
					Random r = new Random();
					double prob = Math.pow(Math.E,delta/temp);
					//System.out.println("prob: "+prob);
					if(prob>0.5)
					{
						c++;
						key=newKey;
						score=newScore;
					}
				}
			}
			System.out.println("TEMP: "+temp+" Worst: "+c+"\n\n");
			c=0;
		}
		
		cypher=new PlayfairCypher(key);
		System.out.println(cypher.toString());
		System.out.println(cypher.Decrypt(txt));
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime/10);
	}
	
	private String readText(String path)
	{
		try 
		{
			BufferedReader reader=new BufferedReader(new FileReader(path));
			String line="";
			
			while((line=reader.readLine())!=null&&txt.length()<NUMCHARTOREAD)
			{
				if(line.charAt(0)=='*')
				{
					for (int i = 0; i < 16; i++) //advancing the buffer to the start of the book
					{
						line=reader.readLine();
					}
					continue;
					
				}
				txt+=line.substring(0,NUMCHARTOREAD);
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
		
		return String.valueOf(key);
	}

	private String SwapRows(char[] key)
	{
		//cypher.printMatrix();
		Random r = new Random();

		int i = r.nextInt(5);
		int j = r.nextInt(5);
		
		while(i==j) j = r.nextInt(5);
		char temp;
		for(int k=0;k<5;k++)
		{
	        temp = key[i*5 + k];
	        key[i*5 + k] = key[j*5 + k];
	        key[j*5 + k] = temp;
	    }
		
		return String.valueOf(key);
	}

	private String SwapColumns(String key)
	{
		//cypher.printMatrix();
		Random r = new Random();

		int i = r.nextInt(5);
		int j = r.nextInt(5);
		
		while(i==j) j = r.nextInt(5);
		
		
		char[] tempKey = key.toCharArray();
		char temp;
		for(int k=0;k<5;k++)
		{
			temp = tempKey[k*5 + i];
			tempKey[k*5 + i] = tempKey[k*5 + j];
			tempKey[k*5 + j] = temp;
	    }
		//PlayfairCypher a = new PlayfairCypher(key);
		//a=new PlayfairCypher(String.valueOf(tempKey));
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
		
		return aux;
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
		
		temp.setDigraph(digraph);
		
		return temp.toString();
	}

	public static void main(String[] args)
	{
		SimulatedAnnealing sm = new SimulatedAnnealing("D:\\Hobbit.txt","D:\\4grams.txt");
		sm.Execute();
	}
}
