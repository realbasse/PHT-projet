package peerSimTest_v4;

/**Type de message :
 * <ul>
 * 	<p>
 * 	<li> createIndex
 * 	<li> removeIndex
 * 	<li> add
 * 	<li> remove
 * 	<li> search
 * 	</p>
 * </ul>
 * <ul>
 * 	<p> 
 * 	<li> createNode
 * 	<li> removeNode
 * 	</p>
 * </ul>
 * <ul>
 * 	<li> OK
 * </ul>
 * 
 **/
public class Message {
	
	private String indexName;
	private String type = "";
	private String path;
	private Object data = null;
	private BF key;
	private BF bf;
	private int src;
	private int dest;
	private int requestID;
	private boolean isLeafNode;
	private Object option = null;
	
	/**Type de message :
	 * <ul>
	 * 	<p>
	 * 	<li> createIndex
	 * 	<li> removeIndex
	 * 	<li> add
	 * 	<li> remove
	 * 	<li> search
	 * 	</p>
	 * </ul>
	 * <ul>
	 * 	<p> 
	 * 	<li> createNode
	 * 	<li> removeNode
	 * 	</p>
	 * </ul>
	 * <ul>
	 * 	<li> OK
	 * </ul>
	 * 
	 **/
	public Message()
	{
	}
	
	public String getIndexName()
	{
		return this.indexName;
	}
	
	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}
	
	public BF getBF()
	{
		return this.bf;
	}
	
	public void setBF(BF bf)
	{
		this.bf = bf;
	}
	
	public BF getKey()
	{
		return this.key;
	}
	
	public void setKey(BF key)
	{
		this.key = key;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public Object getData()
	{
		return this.data;
	}
	
	public void setData(Object data)
	{
		this.data = data;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public int getSource()
	{
		return this.src;
	}
	
	public void setSource(int src)
	{
		this.src = src;
	}
	
	public int getDestinataire()
	{
		return this.dest;
	}
	
	public void setDestinataire(int dest)
	{
		this.dest = dest;
	}
	
	public int getRequestID()
	{
		return this.requestID;
	}
	
	public void setRequestID(int requestID)
	{
		this.requestID = requestID;
	}
	
	public Object getOption()
	{
		return this.option;
	}
	
	public void setOption(Object option)
	{
		this.option = option;
	}
	
	public void setIsLeafNode(boolean value)
	{
		this.isLeafNode = value;
	}
	
	public boolean getIsLeafNode()
	{
		return this.isLeafNode;
	}
	
	public String toString()
	{
		return "Message \n  "
				+ "Type         : " + this.getType()			+ "\n  "
				+ "Index        : " + this.indexName 			+ "\n  "
				+ "BF           : " + this.bf 					+ "\n  "
				+ "Key          : " + this.key      			+ "\n  "
				+ "Path         : " + this.path      			+ "\n  "
		//		+ "Data         : " + this.data 				+ "\n  "
				+ "IsLeafNode   : " + this.isLeafNode 			+ "\n  "
				+ "Source       : " + this.getSource() 			+ "\n  "
				+ "Destinataire : " + this.getDestinataire() 	+ "\n  "
				+ "RequestID    : " + this.requestID 			+ "\n  "
				+ "Option       : " + this.option 				+ "\n";
	}
	
}
