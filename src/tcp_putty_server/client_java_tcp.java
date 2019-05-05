package tcp_putty_server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client_java_tcp {

	static String path="/home1/s/r/rm147279";
	static String path1="/home1/s/r/rm147279";
	/* 	https://gist.github.com/CarlEkerot/2693246
		https://coderanch.com/t/617847/java/File-Transfer-sockets
	http://www.java2s.com/Code/Java/Network-Protocol/TransferafileviaSocket.htm
	https://coderanch.com/t/591112/java/File-transfer-Client-Server-side
	https://github.com/Msan1995/TCP-UDP-protocol
	*/
	//refrence link:    http://silveiraneto.net/2008/10/07/example-of-unix-commands-implemented-in-java/
	public final static int FILE_SIZE = 6022386;
		public static void main(String[] args) throws UnknownHostException, IOException {
			int option=0;
			Scanner s= new Scanner(System.in);
			//take ip from user
			System.out.println("Enter server name or IP address: ");
			String d=s.nextLine();
			if(d.equals("127.0.0.1") || d.equalsIgnoreCase("localhost") || d.equalsIgnoreCase("128.163.7.19") || d.equals("169.226.22.9") )
				{	   
				//take port from user
				    System.out.println("Enter port: ");
				  	int p=Integer.parseInt(s.nextLine());
				  	if(p>=0 && p<=655535)
				  		{		
				  	//take command from user
				  			
				  			Socket soc1=new Socket(InetAddress.getByName("csi516-fa18.arcc.albany.edu"),12006);
				  		    OutputStream output = soc1.getOutputStream();
				  		    PrintWriter writer = new PrintWriter(output, true);
				  		    System.out.println("Enter Command");
				  			String in="";
				  		    Scanner sc= new Scanner(System.in);
				  		      Scanner sc1 = new Scanner(System.in);
				  		       while(option!=5)
				  		       {
				  		       in=sc.nextLine();
				  		       String[] spliting=in.split(" ",2);
				  		       //send command to server
				  			    if(spliting[0].equals("cd"))
				  			    {
				  			    writer.println(1);
				  			    }
				  			    else if(spliting[0].equals("ls"))
				  			    {
				  			    writer.println(2);
				  			    }	    
				  			    else if(spliting[0].equals("put"))
				  			    {
				  			    writer.println(3);
				  			    }
				  			    else
				  			    {
				  			    	writer.println(4);
				  			    }
				  		       switch(spliting[0])
				  		       {
				  		       case "cd":
				  		    	   tcpcd(12007,spliting[1]);  break;      
				  		       case "ls": 	 
				  		    	   if(spliting.length<2)
				  		    	   tcpls(12009,"");
				  		    	   else
				  		    		   tcpls(12009,spliting[1]);
				  		    	   break;
				  		       case "put":   	 
				  		    	   tcpput(12022,spliting[1]);		     break; 	  
				  		       case "get":
				  		    	   tcpget(12023,spliting[1]);  break;   	
				  		     case "exit":
				  		    	   System.exit(0);  break; 
				  		       }
				  		      }
				  		       sc1.close();
				  		       soc1.close();
				  		       sc.close();
				  		}
				  	else
				  		System.out.println("Invalid port number. Terminating.");
				}
			else {
				System.out.println("Could not connect to server. Terminating.");
				} 
		}	
		  public static  void tcpcd(int port, String pa) throws UnknownHostException, IOException   	
	      {
			  //create client socket 
			   Socket s1 = new Socket(InetAddress.getByName("csi516-fa18.arcc.albany.edu"),port);
		       OutputStream output = s1.getOutputStream();
		       PrintWriter writer = new PrintWriter(output, true);    
		       path=pa;
		       //send path to server
		       writer.println(path);
		       InputStream input = s1.getInputStream();
		       BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		       //receive directory change or not(acknowledgement)
		       String s2=reader.readLine();
		       System.out.println(s2);
		       s1.close();
		       s1.close();
	      }
		  public static  void tcpls(int port,String s10) throws UnknownHostException, IOException
		  {
			//create client socket 
			  Socket s2 = new Socket(InetAddress.getByName("csi516-fa18.arcc.albany.edu"),port);
	   	   	  OutputStream output2 = s2.getOutputStream();
	          PrintWriter writer2 = new PrintWriter(output2, true);
	          //send path
	          if(s10.equalsIgnoreCase("")|| s10.isEmpty() || s10.equalsIgnoreCase(null))
	        	  s10=path;
	          writer2.println(s10);
	          InputStream input2 = s2.getInputStream();
	          BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
	          //recieve output of ls from server
	          String ls=reader2.readLine();
	          //format the output according to need
	          String[] l=ls.split("#");
	          for(String l1 :l)
	          {
	       	   System.out.println(l1);
	          }  
	          s2.close();
		  }
		  public static  void tcpput(int port,String pat) throws UnknownHostException, IOException
		  {    
			//create client socket 
	          Socket s3 = new Socket(InetAddress.getByName("csi516-fa18.arcc.albany.edu"), port);  
			  DataOutputStream dos = new DataOutputStream(s3.getOutputStream());
	                     try{          
	                    	 //open file which is to be send
	                  File f = new File(pat);
	                  String s1 = f.getName();
	                  System.out.println(s1);
	                  //send file to server
	                  dos.writeUTF(s1);                                                                               
	                  DataInputStream bis = new DataInputStream(new FileInputStream(f));
	                  int bs = 10000;
	                  byte[] a = new byte[bs];
	                  int j=0;
	                  while ((j = bis.read(a, 0, bs))!= -1){                              
	                      dos.write(a, 0, j);                              
	                  }    
	                 s3.close();
	                 bis.close();            
	          } catch (Exception ex) {
	              System.out.println(ex);
	          }      
		}
		  public static  void tcpget(int port,String f) throws IOException
	      {
			//create client socket 
			    int read_bytes;
			    int current = 0;
			    FileOutputStream fos = null;
			    BufferedOutputStream bos = null;
			    Socket s4 = null;
			    try {
			  
			      s4 = new Socket(InetAddress.getByName("csi516-fa18.arcc.albany.edu"), port);
			      OutputStream output = s4.getOutputStream();
			      PrintWriter writer = new PrintWriter(output, true);
			      writer.println(f);	
			      //path where file is going to send
			      String path_savedpath="/home1/s/r/rm147279/"+f;
			     
			      byte [] mybytearray  = new byte [FILE_SIZE];
			      InputStream is4 = s4.getInputStream();
			      //open file name in which content will be copied
			      fos = new FileOutputStream(path_savedpath);
			      bos = new BufferedOutputStream(fos);
			      //copy file content to open file which is open in write mode
			      read_bytes = is4.read(mybytearray,0,mybytearray.length);
			      current = read_bytes;
			      do {
			    	  read_bytes =is4.read(mybytearray, current, (mybytearray.length-current));
			         if(read_bytes >= 0)
			        	 current += read_bytes;
			      } while(read_bytes > -1);
			      bos.write(mybytearray, 0 , current);
			      bos.flush();
			      System.out.println("File " + path_savedpath  + " downloaded (" + current + " bytes read)");
			      fos.close();
			      bos.close();
			      s4.close();
			    }
			    finally {        
			    }
	      }
	}

