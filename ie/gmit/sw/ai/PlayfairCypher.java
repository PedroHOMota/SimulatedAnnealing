package ie.gmit.sw.ai;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlayfairCypher 
{
	private int[] rowIndices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,};
	private int[] columnIndices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,};
	
	private char[][] digraph= new char[5][5];
	
	public PlayfairCypher() 
	{
		String key = "THEQUICKBROWNFXMPDVLAZYGS";
		for (int i = 0; i < columnIndices.length; i++) 
		{
			for (int j = 0; j < columnIndices.length; j++) 
			{
				columnIndices[key.charAt(i)-65]=i;
				rowIndices[key.charAt(i)-65]=j;
				digraph[i][j]=key.charAt(i);
			}
		}
	}
	
	public void encryptTST(String txt)
	{
		int counter=0;
		String enc="";
		int ri=0 , ci=0;
		txt = txt.toUpperCase().replaceAll("[^A-Za-z0-9]", "");
		Pattern p = Pattern.compile("(.)\\1");
		Matcher m = p.matcher(txt);

		while (m.find())
		{
			txt=txt.replace(m.group(0), m.group(0).charAt(0) + "X");
		}
		
		for(int i=0;counter<25;i++)
		{
			if(rowIndices[txt.charAt(i)-65]==-1)
			{
				if(ri==25)
				{
					ci++;
					ri=0;
				}
				
				i++;
				rowIndices[txt.charAt(i)-65]=ri;
				columnIndices[txt.charAt(i)-65]=ci;
				digraph[ci][ri]=txt.charAt(i);
				ri++;
			}
		}
		
	}
	
	public void encrypt(String txt)
	{
		String[] pairs;
		
		txt = txt.toUpperCase().replaceAll("[^A-Za-z0-9]", "");
		Pattern p = Pattern.compile("(.)\\1");
		Matcher m = p.matcher(txt);

		while (m.find())
		{
			txt=txt.replace(m.group(0), m.group(0).charAt(0) + "X");
		}
		
		txt += (txt.length() % 2 != 0) ? "X" : ""; //If the txt is odd add a X to the end to amke it even
	
		pairs = txt.split("(..)");
		
		
	}
	public String blabla(String[] pairs)
	{
		int b='a'-65;
		String aux="";
		
		for (int i = 0; i < pairs.length; i++) 
		{
			char[] letters=pairs[i].toCharArray();
			
			if(rowIndices[letters[0]-65]==rowIndices[letters[1]-65])
			{
				//Pegar a letra da direita imediata
				//[coluna][linha]
				int auxColuna = 0;
				auxColuna = columnIndices[letters[0]-65];
				if(auxColuna+1>4)
					auxColuna=0;
				
				aux+=digraph[auxColuna][rowIndices[letters[0]-65]];
				
				auxColuna = columnIndices[letters[1]-65];
				if(auxColuna+1>4)
					auxColuna=0;
				
				aux+=digraph[auxColuna][rowIndices[letters[1]-65]];
			}
			else if(columnIndices[letters[0]-65]==columnIndices[letters[1]-65])
			{
				//Pegar a letra imediata abaixo
				//[coluna][linha]
				int auxLinha = 0;
				auxLinha = rowIndices[letters[0]-65];
				if(auxLinha+1>4)
					auxLinha=0;
				
				aux+=digraph[rowIndices[letters[0]-65]][auxLinha];
				
				auxLinha = rowIndices[letters[1]-65];
				if(auxLinha+1>4)
					auxLinha=0;
				
				aux+=digraph[rowIndices[letters[1]-65]][auxLinha];
			}
			else
			{
				//minha linha coluna da outra letra
				//[coluna][linha]
				
				aux+=digraph[columnIndices[letters[0]-65]][rowIndices[letters[1]-65]]
						+digraph[columnIndices[letters[1]-65]][rowIndices[letters[0]-65]];
			}
		}
		return aux;
	}
	
}
