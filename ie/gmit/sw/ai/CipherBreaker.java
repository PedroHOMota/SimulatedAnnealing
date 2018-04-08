package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;


public class CipherBreaker 
{
	private final int NUMCHARTOREAD = 400;
	//private char[][] digraph;
	private String txt="";
	FourGramDictionary dic;

	long startTime = System.currentTimeMillis();
	
	public CipherBreaker(String txtPath, String dicPath)
	{
		txt=readText(txtPath);
		dic= new FourGramDictionary(dicPath);
		SimulatedAnnealing sa = new SimulatedAnnealing();
		sa.setFourGramDictionary(dic);
	}
	
	
	public void Execute()
	{
		//double score = 0;
		//double delta = 0;
		String decryptedText="";
		String key = KeyGenerator.GenerateKey();
		PlayfairCypher cypher=new PlayfairCypher(key);
		int temp = 11;
		
		//this.digraph=cypher.getDigraph();
		
	
		decryptedText=cypher.Decrypt(txt);
		double score=Score(decryptedText);

		//PlayfairCypher aux;
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
			
			if(score>180) temp=-1;

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

	

	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter book's path");
		String book = scan.nextLine();
		System.out.println("Enter 4gram's path");
		String fourGram = scan.nextLine();
		
		scan.close();
		CipherBreaker sm = new CipherBreaker(book,fourGram);
		//CipherBreaker sm = new CipherBreaker("Hobbit.txt","4grams.txt");
		sm.Execute();
	}
}
