package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;


public class CipherBreaker 
{
	private final int NUMCHARTOREAD = 400;
	private char[][] digraph;
	private String txt="";
	FourGramDictionary dic;
	
	private double scoreT1=0;
	private double scoreT2=0;
	
	private String keyT1="";
	private String keyT2="";
	
	long startTime = System.currentTimeMillis();
	
	public CipherBreaker(String txtPath, String dicPath)
	{
		txt=readText(txtPath);
		dic= new FourGramDictionary(dicPath);
		SimulatedAnnealing sa = new SimulatedAnnealing();
		sa.setFourGramDictionary(dic);
	}
	
	/*private String ShuffleKey(String key)
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
		
		return aux;
	}*/
	
	public void Execute()
	{
		//double score = 0;
		double delta = 0;
		String decryptedText="";
		String key = KeyGenerator.GenerateKey();
		PlayfairCypher cypher=new PlayfairCypher(key);
		int temp = 11;
		
		this.digraph=cypher.getDigraph();
		
	
		decryptedText=cypher.Decrypt(txt);
		double score=Score(decryptedText);

		PlayfairCypher aux;
		Thread t1;
		Thread t2;
		for (; temp >0 ; temp--) 
		{
			System.out.println("TEMP "+temp);
			//Runnable r = new AneallingRunnable(key,score,temp,keyT1,scoreT1,txt);
			Runnable r = new AneallingRunnable(key,score,temp,txt,1);
			Runnable r2 = new AneallingRunnable(key,score,temp,txt,2);
			t1 = new Thread(r);
			t2=new Thread(r2);
			t1.start();
			t2.start();
			
			try
			{
				t1.join();
				t2.join();
			}catch (Exception e) {
				// TODO: handle exception
			}
			if(SharedDataBetweenThreads.scoreT1>SharedDataBetweenThreads.scoreT2)
				{
					score=SharedDataBetweenThreads.scoreT1;
					key=SharedDataBetweenThreads.keyT1;
				}
			else
			{
				score=SharedDataBetweenThreads.scoreT2;
				key=SharedDataBetweenThreads.keyT2;
			}
			System.out.println("TEMP: "+temp+" Score: "+score+"\n");
			SharedDataBetweenThreads.scoreT1=0;
			SharedDataBetweenThreads.scoreT2=0;
			
			if(score>190) temp=-1;
			/*for (int i = 40000; i > 0; i--) 
			{
				String newKey=ShuffleKey(key);
				aux=new PlayfairCypher(newKey);
				double newScore = Score(aux.Decrypt(txt));
				delta = newScore - score;
				if(delta > 0)
				{
					key=newKey;
					score=newScore;
				}
				else 
				{
					Random r = new Random();
					double prob = Math.pow(Math.E,delta/temp);
					if(prob>0.6)
					{
						key=newKey;
						score=newScore;
					}
				}
			}
			System.out.println("TEMP: "+temp+" Score: "+score+"\n");
			if(score>190) temp=-1;*/
	
		}
		
		cypher=new PlayfairCypher(key);
		System.out.println(cypher.toString());
		System.out.println(cypher.Decrypt(txt));
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("\nTime taken: "+elapsedTime/10);
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

	/*private String SwapSingleLetters(char[] key)
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
			/*}
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
	}*/

	public static void main(String[] args)
	{
		/*Scanner scan = new Scanner(System.in);
		System.out.println("Enter book's path");
		String book = scan.nextLine();
		System.out.println("Enter 4gram's path");
		String fourGram = scan.nextLine();
		
		scan.close();*/
		//CipherBreaker sm = new CipherBreaker(book,fourGram);
		CipherBreaker sm = new CipherBreaker("Hobbit.txt","4grams.txt");
		sm.Execute();
	}
}
