import java.util.*;
import java.io.*;
import gudusoft.gsqlparser.nodes.TColumnDefinition;
import gudusoft.gsqlparser.stmt.*;

public class AnalyzeCreate {

	
	
    protected static void analyzeCreateTableStmt(TCreateTableSqlStatement pStmt)
    {
    	
    	String table_name=pStmt.getTargetTable().toString();
    	ArrayList<String> col=new ArrayList<String>();
    	if(!Main_file.table.containsKey(table_name))
    	{
    		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("metadata.txt", true)))) 
        	{
        	   out.println("<begin_table>");
        	   out.println(table_name);
//        	    System.out.println("Table Name:"+table_name);
//                System.out.println("Columns:");
                TColumnDefinition column;
                for(int i=0;i<pStmt.getColumnList().size();i++)
                {
                    column = pStmt.getColumnList().getColumn(i);
                    col.add(column.getColumnName().toString());
                    out.println(column.getColumnName().toString());
//                    System.out.println("\tname:"+column.getColumnName().toString());
//                    System.out.println("\tdatetype:"+column.getDatatype().toString());
//                    
//                    System.out.println("");
                }
                Main_file.table.put(table_name, col);
                for(String next:col)
                {
                	Map<String,ArrayList<Integer>> lmap=new HashMap<String,ArrayList<Integer>>();
    				ArrayList<Integer> l=new ArrayList<Integer>();
    	        	lmap.put(next, l);
    	        	if(Main_file.data.get(table_name)==null)
		        	{

    	        		Main_file.data.put(table_name,lmap);	
		        	}
		        	if(!Main_file.data.get(table_name).containsKey(next))
		        	{
		        		Main_file.data.get(table_name).put(next,l);
		        	}
                }
//                System.out.println("Afet create statement");
//                System.out.println(Main_file.data);
//                System.out.println(Main_file.table);
                out.println("<end_table>");
                out.close();
               File file = new File(table_name+".csv");
              if (!file.exists()) {
                file.createNewFile();
              }
//              System.out.println("File Created");
        	}catch (IOException e) {
        	    //exception handling left as an exercise for the reader
        		System.out.println("ERROR!!!");
        		//System.out.println(e.printStackTrace());
        	}
    	}
    	else
    		System.out.println("ERROR:TAble Already Exist!!!");
        
    }
}
