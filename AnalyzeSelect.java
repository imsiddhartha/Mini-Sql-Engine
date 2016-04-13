import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;


public class AnalyzeSelect {

	protected static void analyzeSelectStmt(TSelectSqlStatement pStmt){
//        System.out.println("\nSelect:");
//        if (pStmt.isCombinedQuery()){
//            String setstr="";
//            switch (pStmt.getSetOperator()){
//                case 1: setstr = "union";break;
//                case 2: setstr = "union all";break;
//                case 3: setstr = "intersect";break;
//                case 4: setstr = "intersect all";break;
//                case 5: setstr = "minus";break;
//                case 6: setstr = "minus all";break;
//                case 7: setstr = "except";break;
//                case 8: setstr = "except all";break;
//            }
//            System.out.printf("set type: %s\n",setstr);
//            System.out.println("left select:");
//            analyzeSelectStmt(pStmt.getLeftStmt());
//            System.out.println("right select:");
//            analyzeSelectStmt(pStmt.getRightStmt());
//            if (pStmt.getOrderbyClause() != null){
//                System.out.printf("order by clause %s\n",pStmt.getOrderbyClause().toString());
//            }
//        }else
		{
            //select list
			//boolean single_col=false;
			boolean aggr=false;boolean dis_flag=false;
			boolean []aggrflags={false,false,false,false};
			boolean whereflag=false;
			ArrayList<String> tables=new ArrayList<String>();
			ArrayList<String> cols=new ArrayList<String>();
            for(int i=0; i < pStmt.getResultColumnList().size();i++)
            {
                TResultColumn resultColumn = pStmt.getResultColumnList().getResultColumn(i);
             
               // System.out.println("Column: "+resultColumn.getExpr().toString());
                cols.add(resultColumn.getExpr().toString());
            }
            
            //from clause, check this document for detailed information
            //http://www.sqlparser.com/sql-parser-query-join-table.php
           
            for(int i=0;i<pStmt.joins.size();i++)
            {
                TJoin join = pStmt.joins.getJoin(i);
                switch (join.getKind()){
                    case TBaseType.join_source_fake:
//                    	System.out.println("TYpe 1 join");
                    	tables.add(join.getTable().toString());
//                    	System.out.println("Table:"+join.getTable().toString() );
                        //System.out.printf("table: %s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                        break;
                    case TBaseType.join_source_table:
//                    	System.out.println("TYpe 2 join");
//                        System.out.printf("table: %s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                        for(int j=0;j<join.getJoinItems().size();j++){
                            TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
//                            System.out.printf("Join type: %s\n",joinItem.getJoinType().toString());
//                            System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                            if (joinItem.getOnCondition() != null){
                               // System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                            }else  if (joinItem.getUsingColumns() != null){
                                //System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                            }
                        }
                        break;
                    case TBaseType.join_source_join:
                        TJoin source_join = join.getJoin();
//                        System.out.println("TYpe 3 join");
//                        System.out.printf("table: %s, alias: %s\n",source_join.getTable().toString(),(source_join.getTable().getAliasClause() !=null)?source_join.getTable().getAliasClause().toString():"");

                        for(int j=0;j<source_join.getJoinItems().size();j++){
                            TJoinItem joinItem = source_join.getJoinItems().getJoinItem(j);
//                            System.out.printf("source_join type: %s\n",joinItem.getJoinType().toString());
//                            System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                            if (joinItem.getOnCondition() != null){
//                                System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                            }else  if (joinItem.getUsingColumns() != null){
//                                System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                            }
                        }

                        for(int j=0;j<join.getJoinItems().size();j++){
                            TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
//                            System.out.printf("Join type: %s\n",joinItem.getJoinType().toString());
//                            System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                            if (joinItem.getOnCondition() != null){
//                                System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                            }else  if (joinItem.getUsingColumns() != null){
//                                System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                            }
                        }

                        break;
                    default:
                        System.out.println("unknown type in join!");
                        break;
                }
            }

            //where clause
            if (pStmt.getWhereClause() != null){
            	whereflag=true;
            	boolean and=false,or=false;
                System.out.printf("where clause: \n%s\n", pStmt.getWhereClause().toString());
                String wh=pStmt.getWhereClause().toString();
                String [] a=wh.split(" ");
                System.out.println("Lenght 0f: "+a.length);
                if(a.length>2)
                {
                	List<String> val=new ArrayList<String>();
                	List<String> cname=new ArrayList<String>();
                	if(a[2].equalsIgnoreCase("or"))
                		or=true;
                	else if(a[2].equals("and"))
                		and=true;
                	if(wh.indexOf('=')!=-1)
                	{
                		cname.add(a[1].substring(0,a[1].indexOf('=')));
                    	cname.add(a[2].substring(0,a[2].indexOf('=')));
                    	val.add(a[1].substring(a[1].indexOf('=')));
                    	val.add(a[2].substring(a[1].indexOf('=')));
                	}
                	else
                	{
                		System.out.println("Error!!!");
                		return;
                	}
                	if(checkforcollist(cname, tables))
                	{
                		
                	}
                	else
                	{
                		System.out.println("Error!!!");
                		return;
                	}
                		
                }
                else
                {
                	
                }
            }

            if(tables.size()==1 && cols.size()==1)
            {
            	aggr=checkAggr(cols.get(0),aggrflags);
            	if(aggr)
            	{
            		StringBuilder cl=new StringBuilder(cols.get(0));
            		StringBuilder c=new StringBuilder();
            		for(int i=cols.get(0).indexOf('(')+1;i<cols.get(0).indexOf(')');i++)
            		{
            			c.append(cl.charAt(i));
            		}
            		cols.remove(0);
            		cols.add(0, c.toString());
            		//System.out.println("Col name "+cols.get(0));
            		String table_name=tables.get(0);
            		if(checkfortable(tables) && checkforcols(table_name, cols))
            		{
            			if(aggrflags[0])
            				printtable(table_name, cols,'a');
            			else if(aggrflags[1])
            				printtable(table_name, cols,'M');
            			else if(aggrflags[2])
            				printtable(table_name, cols,'m');
            			else if(aggrflags[3])
            				printtable(table_name, cols,'s');
            		}
            		else
            			System.out.println("ERROR!!!");
            	}
            }
            
            if(!aggr && tables.size()==1 && cols.size()==1 )
            {
            	if(cols.get(0).contains("("))
            	{
//            		System.out.println("Index of (= "+cols.get(0).indexOf('('));
//            		System.out.println("Index of )= "+cols.get(0).indexOf(')'));
            		StringBuilder cl=new StringBuilder(cols.get(0));
            		StringBuilder c=new StringBuilder();
            		for(int i=cols.get(0).indexOf('(')+1;i<cols.get(0).indexOf(')');i++)
            		{
            			c.append(cl.charAt(i));
            		}
            		cols.remove(0);
            		cols.add(0, c.toString());
            		//System.out.println("Col name "+cols.get(0));
            		String table_name=tables.get(0);
            		if(checkfortable(tables) && checkforcols(table_name, cols))
            		{
            			printtable(table_name, cols,'d');
            		}
            		else
            			System.out.println("ERROR!!!");
            		dis_flag=true;
            	}
            	
            }
          if(!dis_flag&&!aggr &&tables.size()==1)
          { 
        	  if(checkfortable(tables))
	          {
        		  String table_name=tables.get(0);
                  if(pStmt.getResultColumnList().size()==1)
                  {
               	   	String single_col_name=cols.get(0);
               	   
    	           	 if(single_col_name.equalsIgnoreCase("*"))  
    	           	 {
	              		 printtable(table_name);
    	           	 }
    	           	 else
    	           	 {
    	           		 if(checkforcols(table_name,cols))
    	           			 printtable(table_name,cols);
    	           		 else
    	           			System.out.println("Error!!! :Column not exist");
    	           	 }
                  }
                  else
                  {
                	  if(checkforcols(table_name,cols))
 	           			 printtable(table_name,cols);
 	           		 else
 	           			System.out.println("Error!!! :Column not exist");
                  }
	          } 
	               	 else
	               		 System.out.println("Error!!! :Table not exist");
          }
          //if table size >1
          
          if(tables.size()>1 && !whereflag)
          {
        	  List<List<Integer>> all_data = new ArrayList<List<Integer>>();
        	 
        	  int tab_count=0;
        	  if(checkfortable(tables))
        	  {
        		  for(String s:tables)
            	  {  
        			  List<String> c=Main_file.table.get(s);
            		  if(tab_count==0)
            		  {
            			  for(int i=0;i<Main_file.data.get(s).get(c.get(0)).size();i++)//for no of entries size of arryaList
            			  {
            				  List<Integer> one=new ArrayList<Integer>();
            				  for(int j=0;j<c.size();j++)	//for no of cols
            				  {
            					  one.add(Main_file.data.get(s).get(c.get(j)).get(i));
            				  }
            				  all_data.add(one);
            			  }
            		  }
            		  if(tab_count>0)
            		  {  
            			
            			  int len=all_data.size();
            			  for(int i=0;i<Main_file.data.get(s).get(c.get(0)).size();i++)//for no of entries
            			  {
            				  List<Integer> test=new ArrayList<Integer>();
	            			  for(int j=0;j<c.size();j++)	//for no of cols
	            			  {
	            					  test.add(Main_file.data.get(s).get(c.get(j)).get(i));
	            			  }
	            			  for(int k=0;k<len;k++)
	            			  {
	            				  List<Integer> tt=new ArrayList<Integer>(all_data.get(k));
	            				  for(Integer p:test)
	            				  {
	            					  tt.add(p);
	            				  }
	            				
	            				  all_data.add(tt);
	            			  }
            		    }
            			  
            			  for(int k=0;k<len;k++)
            				  all_data.remove(0);
            			  
            		  }
            		  tab_count++;
            		
            	  }
        		  if(checkforcollist(cols,tables))
        		  {
        			  int[] arr= new int[cols.size()];
        			  int [] tab=new int [tables.size()];
        			  String []append=new String[cols.size()]; //2-d array le lo
        			  for(int i=0;i<cols.size();i++)
        			  {
        				  int ind=indexof(cols.get(i),tables,tab);
        				  arr[i]=0;
        				  for(int j=0;j<tab[ind];j++)
    					  {
    						  arr[i]=arr[i]+Main_file.table.get(tables.get(j)).size();
    					  }
        				  arr[i]=arr[i]+ind;
        				  append[i]="";
        				  if(tab[ind]==0)
        				  {
        					  for(int j=0;j<all_data.size();j++)
        					  {
        						  List<Integer> l=all_data.get(j);
        						  //System.out.println(l.get(arr[i]));
        						  
        						  append[i]=append[i]+l.get(arr[i])+'\n';
        					  }
        				  }
        				  else
        				  {     					 
        					 // System.out.println("ind="+arr[i]);
        					  for(int j=0;j<all_data.size();j++)
        					  {
        						  List<Integer> l=all_data.get(j);
        						  //System.out.println("l="+l);
        						  //System.out.println("data="+l.get(arr[i]));
        						  //append[i]="";
        						  append[i]=append[i]+l.get(arr[i])+'\n';
        					  }
        				  }
        				  
        			  }
        			  //System.out.println("Appen array");
        			  for(int i=0;i<cols.size();i++)
        			  {
        				  System.out.println(cols.get(i));
        				  System.out.println(append[i]);
        			  }
        				  //System.out.println("Shi h");
        		  }
        		  else
        			  System.out.println("ERROR!!!");
        		
        		  //System.out.println(all_data.size());
        		  System.out.println(all_data);
        	  }
        	  else
        		  System.out.print("ERROR!!!Table not Exist");
          }
           

            // group by
            if (pStmt.getGroupByClause() != null){
                System.out.printf("group by: \n%s\n",pStmt.getGroupByClause().toString());
            }

            // order by
            if (pStmt.getOrderbyClause() != null){
              System.out.printf("order by: \n%s\n",pStmt.getOrderbyClause().toString());
            }

            // for update
            if (pStmt.getForUpdateClause() != null){
                System.out.printf("for update: \n%s\n",pStmt.getForUpdateClause().toString());
            }

            // top clause
            if (pStmt.getTopClause() != null){
                System.out.printf("top clause: \n%s\n",pStmt.getTopClause().toString());
            }

            // limit clause
            if (pStmt.getLimitClause() != null){
                System.out.printf("top clause: \n%s\n",pStmt.getLimitClause().toString());
            }
        }
    }
	static int indexof(String col,List<String>tables,int tab_count[])
	{
		//System.out.println("---------"+col);
		int pos=0;int count=0;
		String table_name="";
		for(String s:tables)
		{
			table_name=s;
			if(Main_file.table.containsKey(s))
			{
				if(col.contains("."))
				{
					//String [] arr=col.split(".");
					table_name=col.substring(0, col.indexOf('.'));
					System.out.println("-Table---"+table_name);
					col=col.substring(col.indexOf('.')+1);
					pos=Main_file.table.get(table_name).indexOf(col);//pos+ccount*size of table
					if(pos!=-1)
					{
						tab_count[pos]=count;
						System.out.println("---index no---"+pos);
						return pos;
					}
					//System.out.println("---------"+col);
					//col=arr[1];
				}	
				pos=Main_file.table.get(table_name).indexOf(col);//pos+ccount*size of table
				if(pos!=-1)
				{
					tab_count[pos]=count;
					//System.out.println("---index no---"+pos);
					return pos;
				}
			}
			count++;
		}
		return pos;
	}
	static boolean checkforcollist(List<String>cols,List<String>tables)
	{
		String table_name="";String col_name="";boolean flag=false;
		int count=0;
		for(String col:cols)
		{
			count=0;
			col_name=col;
			//System.out.println("---------"+col_name);
			if(col_name.contains("."))
			{
//				System.out.println("---------"+col_name);
//				System.out.println("contaons"+".");
				table_name=col_name.substring(0, col_name.indexOf('.'));
				col_name=col.substring(col.indexOf('.')+1);
			System.out.println("-Table---"+table_name);
//				System.out.println("---------"+col_name);
////				table_name=arr[0];
//				col_name=arr[1];
				if(Main_file.table.containsKey(table_name))
				{
					int ind=Main_file.table.get(table_name).indexOf(col_name);
					if(ind==-1)
						return false;
					flag=true;
				}
				else
					return false;
			}
			else
			{
				for(String t: tables)
				{
					table_name=t;
					if(Main_file.table.containsKey(t))
					{
						int ind=Main_file.table.get(table_name).indexOf(col_name);
						if(ind!=-1)
							count++;
					}
					else
						return false;
				}
				if(count==1)
					flag=true;
				else
					return false;
				
			}
		}
		return flag;
	}
	static boolean checkfortable(List<String> tables)
	{
		for(String table_name:tables)
		{
			if(!Main_file.table.containsKey(table_name))
				return false;
		}
		return true;
	}
	static boolean checkforcols(String table_name,List<String> cols)
	{
		for(String col:cols)
		{
			if(!Main_file.data.get(table_name).containsKey(col))
				return false;
		}
		return true;
	}
	static boolean checkAggr(String col,boolean []aggrflags)
	{
		if(col.contains("AVG(")||col.contains("avg(")||col.contains("Avg("))
		{
			aggrflags[0]=true;
			return true;
		}
		else if(col.contains("MAX(")||col.contains("max(")||col.contains("Max("))
		{
			aggrflags[1]=true;
			return true;
		}
		else if(col.contains("MIN(")||col.contains("min(")||col.contains("Min("))
		{
			aggrflags[2]=true;
			return true;
		}
		else if(col.contains("SUM(")||col.contains("sum(")||col.contains("Sum("))
		{
			aggrflags[3]=true;
			return true;
		}
		return false;
	}
	static void printtable(String table_name)
	{
		List<String> s=Main_file.table.get(table_name);
		 int len=Main_file.data.get(table_name).get(s.get(0)).size();
		for (String d:s)
			System.out.print(d+"\t");
		System.out.println("");
		//int len=s.size();  
		String append="";String token="\t";
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
		  System.out.println(append);
	}
	static void printtable(String table_name,List<String>s)
	{
//		List<String> s=Main_file.table.get(table_name);
		 int len=Main_file.data.get(table_name).get(s.get(0)).size();
		for (String d:s)
			System.out.print(d+"\t");
		System.out.println("");
		//int len=s.size();  
		String append="";String token="\t";
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
		  System.out.println(append);
	}
	static void printtable(String table_name,List<String>s,char chk)
	{
		ArrayList<Integer> l=Main_file.data.get(table_name).get(s.get(0));
		TreeSet<Integer> ss1=new TreeSet<Integer>();
		Set<Integer> ss=new LinkedHashSet<Integer>();
		for(Integer i:l)
		{
			ss.add(i);
			ss1.add(i);
//			m.put(i, i);
		}
		if(chk=='d')
		{
			System.out.println(s.get(0));
			for(Integer i:ss)
			{
				System.out.println(i);
			}
			//System.out.println(count);
			
		}
		else if(chk=='a')
		{
			double sum=0.0;int count=0;
			for(Integer i :l)
			{
				sum=sum+i;
				count++;
			}
			System.out.println("Avg:"+sum/count);
			
		}
		else if(chk=='M')
		{
			System.out.println("Max: "+ss1.last());
		}
		else if(chk=='m')
		{
			System.out.println("Min: "+ss1.first());
		}
		else if(chk=='s')
		{
			int sum=0,count=0;
			for(Integer i :l)
			{
				sum=sum+i;
				count++;
			}
			System.out.println("Sum: "+sum);
		}
	}
}
