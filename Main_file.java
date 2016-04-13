import java.util.*; 
import java.io.*;
import java.util.Map.Entry;
import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.TTruncateTableSqlNode;
import gudusoft.gsqlparser.stmt.*;

public class Main_file {

	/**
	 * @param args
	 */
	public static Map<String,List<String>> table=new HashMap<String,List<String>>();
	public static Map<String,Map<String,ArrayList<Integer>>> data=new HashMap<String,Map<String,ArrayList<Integer>>>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			String curr="",next="",table_name;
			//int table_count=0,col_count=0;
			BufferedReader br = new BufferedReader(new FileReader("metadata.txt"));
			curr = br.readLine();
			while (curr != null) 
			{
				if(curr.equals("<begin_table>"))
				{
					//col_count=0;
					//table_count++;
					table_name=br.readLine();
					//System.out.println("TAble name= "+table_name);
					next=br.readLine();
					ArrayList<String> cols=new ArrayList<String>();
					//open file table.csv
					//read line split on , agr split hone k bd m list m daal du string ko
					while(!next.equals("<end_table>"))
					{
						
						//col_count++;
						//System.out.println("col name: "+next);
						cols.add(next);
						Map<String,ArrayList<Integer>> lmap=new HashMap<String,ArrayList<Integer>>();
						ArrayList<Integer> l=new ArrayList<Integer>();
			        	lmap.put(next, l);
			        	if(data.get(table_name)==null)
			        	{

			        		data.put(table_name,lmap);	
			        	}
			        	if(!data.get(table_name).containsKey(next))
			        	{
			        		data.get(table_name).put(next,l);
			        	}
			        	
						//System.out.println(data);
						next=br.readLine();
						
						
					}
					table.put(table_name,cols);
				}
				curr=br.readLine();
			}
			br.close();
			//System.out.println(data);
			BufferedReader br1;
			
			int count=0;
			
			//System.out.println("----NNum of tables---------------"+data.size());
			
			//for every tables in map 
			for(Entry<String,List<String>> entry:table.entrySet())
          	{
	         	ArrayList<Integer> l;   
	           br1 = new BufferedReader(new FileReader(entry.getKey()+".csv"));
	           List<String> s = entry.getValue();	//list of cols of table
	           Map<String,ArrayList<Integer>> tmphash=new HashMap<String,ArrayList<Integer>>();
	           tmphash=data.get(entry.getKey());
	           
	           curr=br1.readLine();
	           while(curr!=null)
	            {
	            	//System.out.println("Curr line "+curr);
	            	StringTokenizer st = new StringTokenizer(curr,",");
	            	count=0;
	            	while(st.hasMoreElements())
					{
						String token = st.nextToken();
						//System.out.println("Token == "+token);
						l=tmphash.get(s.get(count));
						if(l==null)
							l=new ArrayList<Integer>();
						
						if(token.startsWith("\""))
						{
							//System.out.println("Starts with double quotes");
							token = token.substring(1);
							token=token.substring(0, token.length()-1);
							int num = Integer.parseInt(token);
							l.add(num);
							data.get(entry.getKey()).put(s.get(count),l);
						}
						else
						{
							int num = Integer.parseInt(token);
							l.add(num);
							data.get(entry.getKey()).put(s.get(count),l);	
							//System.out.println(l.size()+" size of list \n List Added to col "+s.get(count));
						}
						count++;
					}
					 curr=br1.readLine();
	            }
	            br1.close();
           }
         //  System.out.println("Size ==="+table.size());
//           System.out.println(data.get("table1"));
          // System.out.println(data.get("t2"));
           
           TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
			
			//String query = "Select * from table1";
           //String query = "create TABLE t1(A  number ,C number)";
           //String query = "insert into t1 values (1,2)";
           //String query = "delete from t1 where A=1";
//           String query ="TRUNCATE TABLE t1";
           //String query = "DROP TABLE t1";
          //String query = "select A,C from table1";           
           //String query = "select * from t2";
           //String query = "select avg(A) from table1";
           //String query = "select col1,table1.col2 from table1,table2 where col3=20";
           String query = "select col1,table1.col2 from table1,table2";
          while(true)
          {
        	  Scanner sc=new Scanner(System.in);
              System.out.print("MySql>");
              query=sc.nextLine();
              if(query.equalsIgnoreCase("exit"))
            	  System.exit(0);
              //System.out.println(query);
              sqlparser.sqltext = query;
   			//sqlparser.sqltext=""+args[0]+"";
   			int ret = sqlparser.parse();
   			if(ret == 0)
   			{
   				for(int i=0;i<sqlparser.sqlstatements.size();i++)
   				{
   					analyzeStmt(sqlparser.sqlstatements.get(i));
   					//System.out.println("");
   				}
   			}
   			else
   			{
   				System.out.println("Syntax ERROR: In Query");
   				System.out.println(sqlparser.getErrormessage());
   			}
          }
    		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected static void analyzeStmt(TCustomSqlStatement stmt){
		switch(stmt.sqlstatementtype){
		
		case sstselect:
			//AnalyzeSelect s=new AnalyzeSelect();
			//System.out.println("Select Statement");
			AnalyzeSelect.analyzeSelectStmt((TSelectSqlStatement)stmt);		
			break;
		
		case sstupdate:
		break;
		case sstTruncate:
			//System.out.println("Truncate Statement");
			AnalyzeTruncate.analyzeTruncateStmt((TTruncateStatement)stmt);
			break;
		case sstdroptable:
			//System.out.println("Drop Statement");
			AnalyzeDrop.analyzeDropStmt((TDropTableSqlStatement)stmt);
			break;
		case sstcreatetable:
			//System.out.println("Create Statement");
			AnalyzeCreate.analyzeCreateTableStmt((TCreateTableSqlStatement)stmt);	
			break;
		case sstdelete:
			//System.out.println("Delete Statement");
			AnalyzeDelete.analyzeDeleteStmt((TDeleteSqlStatement)stmt);
			break;
		 case sstinsert:
			// System.out.println("Insert Statement");
             AnalyzeInsert.analyzeInsertStmt((TInsertSqlStatement)stmt);
             break;
		case sstaltertable:
		break;
		case sstcreateview:
		break;
		default:
		System.out.println(stmt.sqlstatementtype.toString());
		}
		}
}
