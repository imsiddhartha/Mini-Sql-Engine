import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import gudusoft.gsqlparser.stmt.TTruncateStatement;


public class AnalyzeTruncate {
	protected static void analyzeTruncateStmt(TTruncateStatement pStmt)
	{
		//System.out.println("Truncate Statement");
		String table_name="";
		String[] q=pStmt.toString().split(" ", 3);
		table_name=q[2].trim();
		//System.out.println("table name: "+table_name);
		
		if(Main_file.table.containsKey(table_name))
		{
			List<String> s=Main_file.table.get(table_name);
			for(String d:s)
			{
				Main_file.data.get(table_name).put(d, new ArrayList<Integer>());		//updating DATA of main file
			}
			try
			{
				PrintWriter writer = new PrintWriter(table_name+".csv");
				writer.print("");
				writer.close();
			}
			catch(Exception e)
			{
				System.out.println("Error in Truncate");
				e.printStackTrace();
			}
		}
		else
			System.out.println("ERROR:Table Does not exist");
		//System.out.println(pStmt.getTargetTable());
//		if (pStmt.getTargetTable() != null)
//			{
//	            table_name=pStmt.getTargetTable().toString();
//	            System.out.println("Table name:"+table_name);
//	        }
	}
}
