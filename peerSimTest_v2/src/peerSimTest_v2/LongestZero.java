package peerSimTest_v2;

public class LongestZero {

	private BFP2P bf;
	private int sizeOfFragment;
	private int longestLength;
	private String longestPrefix = new String();
	private String remainPrefix = new String();
	
	public LongestZero(BFP2P bf, int sizeOfFragment)
	{		
		this.bf = bf;
		this.sizeOfFragment = sizeOfFragment;
		
		int long0 = 0;
		String prefix0 = new String();

		for (int i = 0; i < bf.size()/sizeOfFragment; i++)
		{
			if (bf.getFragment(i, sizeOfFragment).toInt() != 0)
				break;
			
			long0++;
			prefix0 += "/" + bf.getFragment(i, sizeOfFragment).toInt();
		}
		
		longestLength = long0;
		
		if (long0 == 0)
		{
			longestPrefix = null;
		}
		else
		{
			longestPrefix = prefix0;
		}
		
		if (long0 == bf.size())
		{
			remainPrefix = null;
		}
		else
		{
			for (int i = long0; i < bf.size()/sizeOfFragment; i++)
			{
				remainPrefix += "/" + bf.getFragment(i, sizeOfFragment).toInt();
			}
		}
	}
	
	public int getLongestLength(int stop)
	{
		if (stop >= bf.size()/sizeOfFragment)
			return this.longestLength;
		
		int long0 = 0;
		for (int i = 0; i < stop; i++)
		{
			if (bf.getFragment(i, sizeOfFragment).toInt() != 0)
				break;
			
			long0++;
		}
		
		return long0;
	}
	
	public int getLongestLength()
	{
		return longestLength;
	}
	
	public String getLongestPrefix()
	{
		return this.longestPrefix;
	}
	
	public String getRemainPrefix()
	{
		return this.remainPrefix;
	}
	
	public String getLongestPrefix(int stop)
	{
		if (stop >= bf.size()/sizeOfFragment)
			return this.longestPrefix;
		
		int long0 = 0;
		String prefix0 = new String();
		
		for (int i = 0; i < stop; i++)
		{
			if (bf.getFragment(i, sizeOfFragment).toInt() != 0)
				break;
			
			long0++;
			prefix0 += "/" + bf.getFragment(i, sizeOfFragment).toInt();
		}
				
		if (long0 == 0)
		{
			prefix0 = null;
		}
		
		return prefix0;
	}
	
	public String getRemainPrefix(int start)
	{
		if (start < 0)
			return null;
		
		String remain = new String();
		
		for (int i = start; i < bf.size()/sizeOfFragment; i++)
		{
			remain += "/" + bf.getFragment(i, sizeOfFragment).toInt();
		}
		
		return remain;
	}
}