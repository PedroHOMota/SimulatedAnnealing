package ie.gmit.sw.ai;

public class SimulatedAnnealing 
{
	private char[][] digraph;
	private int[] rowIndices;
	private int[] columnIndices;
	PlayfairCypher cypher;
	public SimulatedAnnealing(char[][] digraph,int[] rowIndices,int[] columnIndices)
	{
		this.rowIndices=rowIndices;
		this.columnIndices=columnIndices;
		this.digraph=digraph;
	}
	
	private void ShuffleKey()
	{
		double a= Math.random() * 100;
	}
	
	public void Execute()
	{
		char[] key = KeyGenerator.GenerateKey();
		cypher=new PlayfairCypher(key.toString());
	}
	
	
}
