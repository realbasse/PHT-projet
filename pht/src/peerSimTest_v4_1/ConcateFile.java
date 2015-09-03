package peerSimTest_v4_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConcateFile {

	public ConcateFile(String fileRequests, String folder)
	{
		Config config = new Config();
		try 
		{
			ReadFile rf = new ReadFile(fileRequests);
		
			System.out.println("Begin");

			for (int i = 0; i < rf.size(); i++)
			{									
				BF bf = new BF(Config.sizeOfBF);
				bf.addAll(rf.getDescription(i));

				config.getTranslate().setRange(Config.requestRange);
				long requestID = config.getTranslate().translate(bf.toString());
				
				String path_tmp = folder + requestID + "_tmp.xml";
				String path = folder + requestID + ".xml";
				
				BufferedReader reader = new BufferedReader(new FileReader(path));
				WriteFile wf = new WriteFile(folder + "_all", true);
				
				while (true)
				{
					String s = new String();
					s = reader.readLine();
					if (s == null)
						break;
					
					wf.write(s + "\n");
				}

				reader.close();
				
				reader = new BufferedReader(new FileReader(path_tmp));
								
				while (true)
				{
					String s = new String();
					s = reader.readLine();
					if (s == null)
						break;
					
					wf.write(s + "\n");
				}
				wf.write("</request>\n");
				wf.close();
				reader.close();
				

				File f = new File(path);
				if (f.exists())
					f.delete();	
				
				f = new File(path_tmp);
				if (f.exists())
					f.delete();				
			}	
			
			System.out.println("End");
		} 
		catch (IOException e )
		{
			e.printStackTrace();
		}	
	}
	
	@SuppressWarnings("unused")
	public static void main (String[] args)
	{
		String date = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
		ConcateFile c = new ConcateFile(Config.fileRequests, Config.currentDir + date + "/" + "16-02-46_peerSim_v4_1/");
	}

}
