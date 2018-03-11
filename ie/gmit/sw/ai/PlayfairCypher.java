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
	
	public PlayfairCypher(String key) 
	{
		getDistinctsChars(key);
		/*key=getDistinctsChars(key);
		char[] aux=key.toCharArray();
		int iArray=0;*/
		
		/*for (int i = 0; i < columnIndices.length; i++) 
		{
			for (int j = 0; j < columnIndices.length; j++) 
			{
				columnIndices[aux[iArray]-65]=i;
				rowIndices[aux[iArray]-65]=j;
				digraph[i][j]=aux[iArray];
			}
		}*/
	}
	
	public char[][] getDigraph()
	{
		return digraph;
	}
	public void setDigraph(char[][] digraph)
	{
		this.digraph = digraph;
	}
	
	public int[] getRowIndexes()
	{
		return rowIndices;
	}
	public void setRowIndexes(int[] rowIndices)
	{
		this.rowIndices = rowIndices;
	}
	
	public int[] getColumnIndices()
	{
		return columnIndices;
	}
	public void setColumnIndices(int[] columnIndices)
	{
		this.columnIndices = columnIndices;
	}
	
 	private String getDistinctsChars(String txt)
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
		
		return txt;
		
	}
	
	public String[] CreatePairs(String txt)
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
		
		return pairs;
		
		
	}
	
	public String Encrypt(String[] pairs)
	{
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
				auxColuna++;
				if(auxColuna>4)
					auxColuna=0;
				
				aux+=digraph[auxColuna][rowIndices[letters[0]-65]];
				
				auxColuna = columnIndices[letters[1]-65];
				auxColuna++;
				if(auxColuna+1>4)
					auxColuna=0;
				
				aux+=digraph[auxColuna][rowIndices[letters[1]-65]];
			}
			else if(columnIndices[letters[0]-65]==columnIndices[letters[1]-65])
			{
				//Pegar a letra imediata abaixo
				//[coluna][linha]
				int auxLinha = 0;
				auxLinha = columnIndices[letters[0]-65];
				auxLinha--;
				if(auxLinha<0)
					auxLinha=4;
				
				aux+=digraph[columnIndices[letters[0]-65]][auxLinha];
				
				auxLinha = columnIndices[letters[1]-65];
				auxLinha--;
				if(auxLinha<0)
					auxLinha=4;
				
				aux+=digraph[columnIndices[letters[1]-65]][auxLinha];
			}
			else
			{
				//minha linha coluna da outra letra
				//[coluna][linha]
				
				aux+=digraph[columnIndices[letters[1]-65]][rowIndices[letters[0]-65]]
						+digraph[columnIndices[letters[0]-65]][rowIndices[letters[1]-65]];
			}
		}
		return aux;
	}
	
	public String Decrypt(String txt)
	{
		String pairs[]=CreatePairs(txt);
		
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
				auxColuna--;
				if(auxColuna<0)
					auxColuna=4;

				aux+=digraph[auxColuna][rowIndices[letters[0]-65]];
				columnIndices[letters[0]-65]=auxColuna;
				
				auxColuna = columnIndices[letters[1]-65];
				auxColuna--;
				if(auxColuna<0)
					auxColuna=4;
				
				aux+=digraph[auxColuna][rowIndices[letters[1]-65]];
				columnIndices[letters[1]-65]=auxColuna;
			}
			else if(columnIndices[letters[0]-65]==columnIndices[letters[1]-65])
			{
				//Pegar a letra imediata abaixo
				//[coluna][linha]
				int auxLinha = 0;
				auxLinha = rowIndices[letters[0]-65];
				auxLinha++;
				if(auxLinha>4)
					auxLinha=0;
				
				aux+=digraph[columnIndices[letters[0]-65]][auxLinha];
				rowIndices[letters[0]-65]=auxLinha;
				
				auxLinha = rowIndices[letters[1]-65];
				auxLinha++;
				if(auxLinha>4)
					auxLinha=0;
				
				aux+=digraph[columnIndices[letters[1]-65]][auxLinha];
				rowIndices[letters[0]-65]=auxLinha;
			}
			else
			{
				//minha linha coluna da outra letra
				//[coluna][linha]
				
						
				aux+=digraph[columnIndices[letters[1]-65]][rowIndices[letters[0]-65]]
						+digraph[columnIndices[letters[0]-65]][rowIndices[letters[1]-65]];
				
				rowIndices[letters[0]-65]^=rowIndices[letters[1]-65];
				rowIndices[letters[1]-65]^=rowIndices[letters[0]-65];
				rowIndices[letters[0]-65]^=rowIndices[letters[1]-65];
			}
		}
		
		return txt;
	}
	
}
