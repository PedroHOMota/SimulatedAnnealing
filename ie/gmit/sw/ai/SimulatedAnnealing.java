package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class SimulatedAnnealing 
{
	//private char[][] digraph;
	private String txt="";
	private static FourGramDictionary dic;
		
	public void setFourGramDictionary(FourGramDictionary d)
	{
		this.dic =d;
	}
	
	public String ShuffleKey(String key)
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
	}
		
	public double Score(String txt)
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
		return String.valueOf(tempKey);
	}

	private String ReverseKey(String key) //Skips changing middle char in a row since it will stay at the same place
	{
		String aux="";
		for (int i = key.length()-1; i > -1; i--) 
		{
			aux+=key.charAt(i);
		}
		
		return aux;
	}

	private String FlipAllRows(String key)
	{
		PlayfairCypher aux = new PlayfairCypher(key);
		char[][] digraph = aux.getDigraph();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 2; j++) 
			{
				char a,b;
				digraph[i][j]^=digraph[i][4-j];
				digraph[i][4-j]^=digraph[i][j];
				digraph[i][j]^=digraph[i][4-j];

			}
		}
		
		aux.setDigraph(digraph);
		return aux.toString();
	}

	private String FlipAllColumns(String key)
	{
		PlayfairCypher temp = new PlayfairCypher(key);
		char[][] digraph = temp.getDigraph();

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
}
