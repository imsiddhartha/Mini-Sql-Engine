This is a mini sql Engine (in java) used for running some set of queries using command line interface.
In this project,I have used General SQL Parser(Gsqlpaerser) http://www.sqlparser.com/kb/javadoc/gudusoft/gsqlparser/TGSqlParser.html

Here are the details of the project:

A.Dataset​:
	
	1.Csv files for tables. 
		a.If a file is :File1.csv then,the table name would be File1.
		b.There will be no tab­separation or space­separation 
	
	2.All the elements in files would be ​only INTEGERS​
	3.  A file named: metadata.txt​ would be given to you which will have the following structure for each table: 
	<begin_table>
	<table_name> 
	<attribute1> 
	.... 
	 
	<attributeN> 
	<end_table>

B.Type of Queries:

	1.Select all records​ : 
		Example:Select * from table_name;
	
	2.Aggregate functions:​Simple aggregate functions on a single column. Sum, average, max and min.
		Example: select max(col1) from table1;
		
	3.Project Columns​ from one or more tables :
		Example: Select col1, col2 from table_name; 
		
	4.Project with distinct from one table:
		Example:select distinct(col1) from table_name;
		
	5.Select with where from one or more tables:​ 
		Example:select col1,col2 from table1,table2 where col1 = 10 AND col2 = 20;
		 
	6.Create command:​This will required to modify the ​"metadata.txt" and create a new csv file in the working directory
		Example:CREATE TABLE table_name(column1 datatype,column2 datatype, columnN datatype);
		
	7.Insert Command: 
		​Example: Insert into <table­name> values(v​,..v​);
		
	8.Delete Command:​Delete a single record from a given table. Only one where condition would be given.
		Example:Delete from <table­name> where <attribute> = <some­value>
	
	9.​Truncate Command:​Delete all records from a table.
		Example:  TRUNCATE TABLE  table_name;
	
	10.​Drop Table:​Delete an empty table.
		Example:DROP TABLE table_name; 
	
C.Error Handling:

	Almost all major types of errors are handled.
