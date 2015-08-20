package peerSimTest_v3_1_1;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import peerSimTest_v3_1_1.Config;

/**
 * 
 * SystèmeIndexP2P gère les nœuds
 * <p>
 * Variable locale : 
 * <ul>
 * 	<li> indexName
 * 	<li> serverID
 * 	<li> listeNode
 * </ul>
 * 
 * @author dcs
 **/

public class PHT implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String indexName;
	private Hashtable<String, PHT_Node> listNodes;
	
	public PHT(String indexName) {
		// TODO Auto-generated constructor stub
		this.indexName = indexName;
		listNodes = new Hashtable<String, PHT_Node>();
		listNodes.put("/", new PHT_Node("/"));
	}
	
	public String getIndexName()
	{
		return this.indexName;
	}
	
	public Hashtable<String, PHT_Node> getListNodes()
	{	
		return this.listNodes;
	}
	
	public void serializeListNodes(String fileName)
	{
		Serializer serializer = new Serializer();
		serializer.writeObject(listNodes, fileName);
	}
	
	@SuppressWarnings("unchecked")
	public void deserializeListNodes(String fileName)
	{
		Serializer serializer = new Serializer();
		this.listNodes = (Hashtable<String, PHT_Node>) serializer.readObject(fileName);
	}
	
	/**
	 * Recherche l'identifiant du nœud correspondant.
	 * 
	 * @return {@link String}
	 * 	
	 * @author dcs
	 * */
	
	private String lookup_insert(BF key) throws ErrorException
	{
		String path = "/";
		
		while (true)
		{
			PHT_Node n = this.listNodes.get(path);

			if (n.getPath().equals("/"))
			{
				if (n.isLeafNode())
					return path;
				
				path = key.getFragment(0, Config.sizeOfElement).toString();
			}
			else // !n.getPath().equals("/")
			{				
				BF bf_tmp = new BF(n.getPath());
				if (key.equals(bf_tmp))
				{					
					if (n.isLeafNode())
					{
						return path;
					}
					else // !n.isLeafNode()
					{
						int i = 1;
						path += key.getFragment(n.getRang() + i++, Config.sizeOfElement);
					}
				}
				else // !key.equals(bf_tmp)
				{
					int rang = n.getRang();

					String s = new String();
					for (int i = 0; i <= rang; i++)
					{
						if (key.getBit(i) == bf_tmp.getBit(i))
							s += (key.getBit(i)) ? "1" : "0";
						
						if (key.getBit(i) == !bf_tmp.getBit(i))
						{
							s += (key.getBit(i)) ? "1" : "0";
							break;
						}
					}
					
					path = s;
				}
			}
		}
	}
	
	public void insert(BF key) throws ErrorException
	{				
		String path = this.lookup_insert(key);
		
		PHT_Node systemNode = this.listNodes.get(path);
		systemNode.insert(key);
		
		if (systemNode.size() > Config.gamma)
		{
			systemNode.setLeafNode(false);
			split(systemNode);
		}
	}
	
	private void split(PHT_Node n) throws ErrorException
	{		
		ArrayList<BF> listKeys = n.getListKeys();
		n.setListKey(null);
		
		if (n.getPath().equals("/"))
		{
			PHT_Node new0 = new PHT_Node("0");
			PHT_Node new1 = new PHT_Node("1");
			
			this.listNodes.put("0", new0);
			this.listNodes.put("1", new1);
		}
		else // !n.getPath().equals("/")
		{
			String path = n.getPath();
			
			PHT_Node new0 = new PHT_Node(path + "0");
			PHT_Node new1 = new PHT_Node(path + "1");
			
			String s_tmp = this.skey(path + "0");
			if (this.listNodes.containsKey(s_tmp))
				this.listNodes.remove(s_tmp);
			
			this.listNodes.put(s_tmp, new0);
			
			s_tmp = this.skey(path + "1");
			if (this.listNodes.containsKey(s_tmp))
				this.listNodes.remove(s_tmp);
			
			this.listNodes.put(s_tmp, new1);
		}
		
		for (int j = 0; j < listKeys.size(); j++)
			this.insert(listKeys.get(j));
		
	}
	
	private String skey(String path) throws ErrorException
	{		
		String rootPath = "/";
		String zeroSeq = "0*";
		String oneSeq = "1*";
		
		if (path.length() <= 0)
			return null;
		
		if (path.equals(rootPath))
			return path;
		
		if (path.matches(zeroSeq))
			return "0";
		
		if (path.matches(oneSeq))
			return "1";
		
		if (path.charAt(path.length() - 1) == '1')
			return this.lpp(path, "01");
		
		return this.lpp(path, "10");
	}
	
	private String lpp(String str, String seq) throws ErrorException
	{		
		if (str == null)
			return null;
		
		if (str.length() < seq.length())
		{
			System.out.println(str + " " + seq);
			throw new ErrorException("lpp : str.length <= seq.length");
		}
		else
		{
			int occ = str.lastIndexOf(seq);
			
			if (occ != -1)
				return str.substring(0, occ + seq.length());
			
			return null;
		}
	}
	
	private LookUpRep lookup(String path) throws ErrorException
	{
		TestSystemIndex_v3_1_1_all.config_log.addNodeVisited(1);
		PHT_Node n;
		if (path.equals("/"))
		{
			n = this.listNodes.get(path);
			if (n.isLeafNode())
				return new LookUpRep("LeafNode", "/");
			
			return new LookUpRep("InternalNode", "/");
		}
		else
		{
			n = this.listNodes.get(this.skey(path.substring(1, path.length())));
		}
		
		if (n == null)
			return new LookUpRep("ExternalNode", null);
		
		if (n.isLeafNode())
			return new LookUpRep("LeafNode", "/" + n.getPath());
		
		return new LookUpRep("InternalNode", "/" + n.getPath());
	}
	
	private int nextZero(BF key, int pos)
	{
		for (int i = pos; i < key.size(); i++)
			if (!key.getBit(i))
				return i;
		
		return -1;
	}
	
	private int nextOne(BF key, int pos)
	{
		for (int i = pos; i < key.size(); i++)
			if (key.getBit(i))
				return i;
		
		return -1;
	}
	/*
	private int nextZeroEnd(BF key, int pos)
	{
		int res = 0;
		for (int i = pos + 1; i < key.size(); i++)
		{
			if (!key.getBit(i))
				res++;
			if (res != 0 && key.getBit(i))
				break;
		}
		if (res == 0)
			return -1;
		
		return pos + res;
	}
	*/
	public ArrayList<BF> get(String path)
	{
		return this.listNodes.get(path).getListKeys();
	}

	public ArrayList<BF> ssSearch(BF key) throws ErrorException
	{		
		ArrayList<String> leafNodes = new ArrayList<String>();
		String rootName = "/";
		LookUpRep rep = this.lookup(rootName);
		
		if (rep.status.equals("LeafNode"))
		{
			leafNodes.add(rootName);
		}
		else
		{
			ArrayList<String> sbTrees = new ArrayList<String>();
			sbTrees.add(rootName);
			
			while (sbTrees.size() != 0)
			{
				ArrayDeque<String> currentStep = new ArrayDeque<String>(sbTrees);
				sbTrees.clear();
				
				while (!currentStep.isEmpty())
				{
					String sbroot = currentStep.poll();
					int nbStep = 1;
					ArrayList<String> newRoots = this.searchMatchedSubtrees(key, nbStep, sbroot, leafNodes);
					if (newRoots != null)
						sbTrees.addAll(newRoots);
				}
			}
		}
				
		ArrayList<BF> bfs = new ArrayList<BF>();
		
		if (!leafNodes.isEmpty())
		{
			Iterator<String> iterator = leafNodes.iterator();
			
			while (iterator.hasNext())
			{
				String path = iterator.next();
				String tmp;
				if (path.equals("/"))
				{
					tmp = path;
				}
				else
				{
					tmp = this.skey(path.substring(1, path.length()));
				}
				
				ArrayList<BF> storedBF = this.get(tmp);
				for (int i = 0; i < storedBF.size(); i++)
				{
					BF bf = storedBF.get(i);
					if (key.in(bf))
						bfs.add(bf);
				}
			}
		}
		
		return bfs;
	}
	
	private ArrayList<String> next1MatchedRoots(String ancestor, int nbZero, ArrayList<String> leafNodes) throws ErrorException
	{
		ArrayList<String> newRoots = new ArrayList<String>();
		ArrayDeque<String> candidates = new ArrayDeque<String>();
		candidates.add(ancestor);
		int n = ancestor.length() + nbZero;
		while (!candidates.isEmpty())
		{
			String anc = candidates.poll();
			String prefix = anc + "1";
			LookUpRep rep = this.lookup(prefix);
			String label = rep.label;
			if (label != null)
			{
				if (label.length() <= n)
				{
					leafNodes.add(label);
				}
				else
				{
					if (label.length() == n + 1)
					{
						leafNodes.add(label);
					}
					else
					{
						String sb = label.substring(0, n + 1);
						newRoots.add(sb);
					}
					label = label.substring(0, n);
				}
				
				String spx = label;
				while (spx.length() > anc.length())
				{
					spx = spx.substring(0, spx.length() - 1) + "0";
					candidates.add(spx);
					spx = spx.substring(0, spx.length() - 1);
				}
			}
			else
			{
				leafNodes.add(anc);
			}			
		}
		
		return newRoots;
	}
	
	private ArrayList<String> searchMatchedSubtrees(BF key, int nbStep, String sbroot, ArrayList<String> leafNodes) 
			throws ErrorException
	{
		int matchedFragSize = sbroot.length() - 1;
		String prefix = sbroot;
		int nextZ = this.nextZero(key, matchedFragSize);
		int nbOnes = 0;
		if (nextZ < 0)
		{
			nbOnes = key.size() - matchedFragSize;
		}
		else
		{
			nbOnes = nextZ - matchedFragSize;
		}
		if (nbOnes > 0)
		{
			prefix = prefix + "1";
			LookUpRep rep = this.lookup(prefix);
		
			if (rep.label.length() <= (sbroot.length() + nbOnes))
			{
				leafNodes.add(rep.label);
				return null;
			}
			else
			{
				prefix = sbroot;
				for (int i = 1; i < nbOnes; i++)
				{
					prefix += "1";
				}
				matchedFragSize = prefix.length() - 1;
			}	
		}
		int n1 = nextOne(key, matchedFragSize);
		int nbZero = 0;
		if (n1 > 0)
		{
			nbZero = n1 - matchedFragSize;
		}
		else
		{
			nbZero = key.size() - matchedFragSize;
		}
		return this.next1MatchedRoots(prefix, nbZero, leafNodes);
	}
	 
	/**
	 * Rechercher le filtre précise
	 * 
	 * @param bf
	 * @param path
	 * @return
	 * 	<ul>
	 * 	<li> soit {@link null}
	 * 	<li> soit {@link BF}
	 * 	</ul>
	 * 
	 * @author dcs
	 * @throws ErrorException 
	 * */
	public Object searchExact(BF key) throws ErrorException
	{		
		return null;
	}
	
	/**
	 * Supprimer le filtre dans le chemin précis.
	 * 
	 * @param bf
	 * @param path
	 * 
	 * @return
	 * 	<li> soit null
	 * 	<li> soit un message vers le serveur hébergé
	 * 		<ul>
				<li> type : remove(supprimer le filtre), removeNode(supprimer un nœud)
				<li> une chaîne de caractères
			</ul>
	 * 
	 * @author dcs
	 * */
	
	public Object remove(BF key)
	{
		return null;
	}
	
	public int size()
	{
		return this.listNodes.size();
	}
	
	public String toString()
	{
		return null;
	}
}
