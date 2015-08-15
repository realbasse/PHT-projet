package test;

import java.util.Enumeration;
import java.util.Hashtable;

import peerSimTest_v3_1.BF;
import peerSimTest_v3_1.ErrorException;
import peerSimTest_v3_1.PHT;
import peerSimTest_v3_1.PHT_Node;

public class TestSystemIndex_v3_1 {

	public static void main(String[] args) throws ErrorException
	{
		PHT systemIndex = new PHT("dcs");
		
		BF key1 = new BF("0001" + "0101" + "1011" + "1000");
		BF key2 = new BF("0001" + "0101" + "1111" + "1000");
		BF key3 = new BF("0001" + "0101" + "1101" + "1000");
		BF key4 = new BF("0001" + "0101" + "1001" + "1000");
		BF key5 = new BF("0100" + "0101" + "1001" + "1100");
		BF key6 = new BF("0000000000000000000000000000001");
		BF key7 = new BF("0000000001000000000000010000001");
		BF key8 = new BF("0001000010010001000001000000000");

		systemIndex.insert(key1);
		systemIndex.insert(key2);
		systemIndex.insert(key3);
		systemIndex.insert(key4);
		systemIndex.insert(key5);
		systemIndex.insert(key6);
		systemIndex.insert(key7);
		systemIndex.insert(key8);

		System.out.println();
		Hashtable<String, PHT_Node> hashtable = systemIndex.getListNodes();
		
		Enumeration<String> enumeration = hashtable.keys();
		
		while (enumeration.hasMoreElements())
		{
			String s = enumeration.nextElement();
			System.out.println(s);
		//	if (n.getListKeys() != null)
			//	System.out.println("   " + n.getListKeys().toString());
		}
		
		System.out.println();
		hashtable = systemIndex.getListNodes();
		
		enumeration = hashtable.keys();
		
		while (enumeration.hasMoreElements())
		{
			String s = enumeration.nextElement();
			PHT_Node n = hashtable.get(s);
			if (n.getListKeys() != null && n.getListKeys().size() != 0)
			{
				System.out.println(" " + s);
				System.out.println(n.getListKeys().toString());
			}
		}
	}

}