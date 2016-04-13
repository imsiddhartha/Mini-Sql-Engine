import java.io.*;
import java.util.*;
import gudusoft.gsqlparser.nodes.TMultiTarget;
import gudusoft.gsqlparser.stmt.*;

public class AnalyzeInsert {

	protected static void analyzeInsertStmt(TInsertSqlStatement pStmt){
        String table_name="";
        // out;
        String append="";
        String token=",";
        TMultiTarget mt = null;
		if (pStmt.getTargetTable() != null){
            
            table_name=pStmt.getTargetTable().toString();
            //System.out.println("Table name:"+table_name);
        }

        //System.out.println("insert value type:"+pStmt.getValueType());
		/* not used as no of col are fixed and must be equal to actual in table else error
        if (pStmt.getColumnList() != null){
            System.out.println("columns:");
            for(int i=0;i<pStmt.getColumnList().size();i++){
                System.out.println("\t"+pStmt.getColumnList().getObjectName(i).toString());
            }
        }
		*/
		//int []arr=new Integer();
		ArrayList<Integer> v=new ArrayList<Integer>();
        if (pStmt.getValues() != null){
           // System.out.println("values:");
            for(int i=0;i<pStmt.getValues().size();i++){
                mt = pStmt.getValues().getMultiTarget(i);
                for(int j=0;j<mt.getColumnList().size();j++){
                   // System.out.println("\t"+mt.getColumnList().getResultColumn(j).toString());
                    append=append+mt.getColumnList().getResultColumn(j).toString();
                    v.add(Integer.parseInt(mt.getColumnList().getResultColumn(j).toString()));
                    if(j!=mt.getColumnList().size()-1)
                    		append=append+token;
                }
            }
        }
      //  System.out.println("String to append "+append);
        
        if(Main_file.table.containsKey(table_name))
    	{
        	//checking cols should be n
        	List<String> s=Main_file.table.get(table_name);
        	int i=0;
        	for(String d:s)			//updating DATA of main file
        	{
        		Main_file.data.get(table_name).get(d).add(v.get(i));
        		i++;
        	}
        	if(mt!=null && s!=null && ((s.size()-1)==mt.getColumnList().size()-1))
        	{
        		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(table_name+".csv", true)))) 
            	{
            		out.println(append);
            		out.close();
            		//System.out.println("String added");
            	}catch (IOException e) {
            	   
            		System.out.println("ERROR!!!");
            		//System.out.println(e.printStackTrace());
            	}
        	}
        	else
        		System.out.println("ERROR:No. of columns mismatched!!!");
    	}
        else
        	System.out.println("ERROR:Table Does not exist");
      
    }
}
