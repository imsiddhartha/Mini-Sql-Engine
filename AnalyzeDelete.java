
import gudusoft.gsqlparser.stmt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeDelete {

	protected static void analyzeDeleteStmt(TDeleteSqlStatement pStmt){
        String table_name="";
        String where="";String col_name="";
        String val="";int value;
		if (pStmt.getTargetTable() != null)
		{
			table_name=pStmt.getTargetTable().toString();
        	//System.out.println("Table name:"+table_name);
        }
          if (pStmt.getWhereClause() != null)
          {
        	  	where=pStmt.getWhereClause().toString();
              //  System.out.println("where clause:"+where);
                String s []=where.split("=");
                col_name=s[0];
                val=s[1];
                col_name=col_name.replace("where ", "");
                col_name=col_name.trim();
                val=val.trim();
                //System.out.println("Col name: "+col_name);
                //System.out.println("value : "+val);
          }
          if(Main_file.table.containsKey(table_name))
      	{
        	  List<String> s=Main_file.table.get(table_name);
        	  int pos=0;
        	  if(Main_file.data.get(table_name).containsKey(col_name))
        	  {
//        		  System.out.println("Col "+col_name+" found in table");
        		  value=Integer.parseInt(val);
        		  pos=Main_file.data.get(table_name).get(col_name).indexOf(value);
//        		  System.out.println(Main_file.data.get(table_name).get(col_name));
//        		  System.out.println("Index of "+val+" is "+pos);
        		  if(pos!=-1)
        		  {
             		  for(String d:s)
            		  {
            			  Main_file.data.get(table_name).get(d).remove(pos);
            		  }
        		  }
//        		  System.out.println("New table after deletion");
//        		  System.out.println(Main_file.data.get("t2"));
        		  int len=Main_file.data.get(table_name).get(col_name).size();
        		  String append="";String token=",";
        		  for(int i=0;i<len;i++)
        		  {
        			  for(int j=0;j<s.size();j++)
        			  {
        				  if(j!=s.size()-1)
        					  append=append+Main_file.data.get(table_name).get(s.get(j)).get(i)+token;
        				  else
        					  append=append+Main_file.data.get(table_name).get(s.get(j)).get(i);
        			  }
        			  append=append+"\n";
        		  }
        		 // System.out.println(append);
        		  try
        		  {
        			  File file = new File(table_name+".csv");
              	    
            	      FileWriter writer = new FileWriter(file); 
            	      // Writes the content to the file
            	      writer.write(append); 
            	      writer.flush();
            	      writer.close();
        		  }
        		  catch(Exception e)
        		  {
        			  e.printStackTrace();
        		  }
        	  }
        	  else
        		  System.out.println("ERROR:Colunm NOT EXIST");
      	}
          else
        	  System.out.println("ERROR:TABLE NOT EXIST");
	}
}	
