import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

import gudusoft.gsqlparser.stmt.TDropTableSqlStatement;
//import gudusoft.gsqlparser.stmt.TInsertSqlStatement;


public class AnalyzeDrop {
	
	protected static void analyzeDropStmt(TDropTableSqlStatement pStmt)
	{
		//System.out.println("Analyze Drop");
		String table_name="";
		String[] q=pStmt.toString().split(" ", 3);
		table_name=q[2].trim();
//		System.out.println("table name: "+table_name);
	
		if(Main_file.table.containsKey(table_name))
		{
			Main_file.table.remove(table_name);
			Main_file.data.remove(table_name);			
			
			//to delete only empty file just check condition on size of list of each col
			//size should be zero
			
			//Code is not being reused ex- writing file make one function for writing file
			//and for checking conditionof table key exist in hashmap
			try
			{
				File file=new File("metadata.txt");
				PrintWriter out=new PrintWriter(file);
				String append="";
				for(Entry<String,List<String>> entry:Main_file.table.entrySet())
	          	{
					
					append=append+"<begin_table>"+'\n';
					append=append+entry.getKey()+'\n';
					List<String>s=entry.getValue();
					for(String d:s)
					{
						append=append+d+'\n';
					}
					append=append+"<end_table>"+'\n';
	          	}
				//System.out.println(append);
				out.println(append);
				out.close();
				file=new File(table_name+".csv");
				file.delete();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
			System.out.println("Error:Table Does not Exist");

	}
}
