package tcp_putty_server;
import java.io.*;
import java.net.*;
public class server extends Thread {	
 static int i=0;
   static String link="";  
//refrence link:    http://silveiraneto.net/2008/10/07/example-of-unix-commands-implemented-in-java/
   /* 	https://gist.github.com/CarlEkerot/2693246
  	https://coderanch.com/t/617847/java/File-Transfer-sockets
	http://www.java2s.com/Code/Java/Network-Protocol/TransferafileviaSocket.htm
	https://coderanch.com/t/591112/java/File-transfer-Client-Server-side
	https://github.com/Msan1995/TCP-UDP-protocol
*/
	public static void main(String[] args) throws Exception {
		ServerSocket server= new ServerSocket(12006);
        Socket s =server.accept();
        System.out.println("Server Ready to receive Input");
        InputStream input = s.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));      
	  	  int option=0;	
	       while(option!=5)
	       {
	    	   //take which command to execute from client
	    	   String s1=reader.readLine();
	    	   option=Integer.parseInt(s1);    
	    	   if(option ==1)
	    		   tcpcd();
	    	   else if(option ==2)
	    		   tcpls();
	    	   else    if(option ==3)
	    		   tcpput();
	    	   else if(option ==4)
	    		   tcpget();
	    	   else if(option ==5)
	    		   System.exit(0);
	      
}
	       server.close();
	       s.close();
	}
	
	
	
	public static  void tcpcd() throws IOException
	{
		//create server and client socket
		ServerSocket ss= new ServerSocket(12007);		
        Socket s =ss.accept();
        //create input and output stream
        OutputStream output = s.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        InputStream input =s.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        //receive path from user
        String string2=reader.readLine();
     
        File directory = new File(string2);
    //check whether file is diretory or not
      if(directory.isDirectory()==true)
      {
    	  //change path
          System.setProperty("user.dir", directory.getAbsolutePath());
          link=directory.toString();
          string2="command executed";
      }
      else 
      {
    	  //error while changing directory
    	  string2="Could not fetch result. Terminating";
          System.out.println(string2);
      } 
      writer.println(string2);
      writer.close();
      s.close();
      ss.close();
       
	}
	public static  void tcpls() throws IOException
	{
		//create server and client socket
		 ServerSocket server1= new ServerSocket(12009);
	     Socket serversocketaccept2 =server1.accept();
	     //create input and output stream
	      OutputStream output1 = serversocketaccept2.getOutputStream();
	      PrintWriter writer1 = new PrintWriter(output1, true);
	      InputStream input1 = serversocketaccept2.getInputStream();
	      BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1));
	      //get path
	      String s12=reader1.readLine();
	      File file = new File(s12);
	      //get list of files from current directory
		   String[] lists = file.list();
		   String sent =new String();
		   for(String l : lists)
		   {
		       
		           System.out.println(l);
		           sent=sent+"#"+l;     
		   } 
	   writer1.println(sent);
	   server1.close();
	   serversocketaccept2.close();
	}
	public static  void tcpput() throws Exception
	{
		//create server and client socket
			ServerSocket server3 = new ServerSocket(12022);
	        Socket socket3 = server3.accept();
	        //create input and output stream
	        DataInputStream dis = new DataInputStream(socket3.getInputStream());  
            String fn = dis.readUTF();
            //write to file content to file on server
              System.out.println("File name "+fn);
             String path12 = link+"/"+fn ;                
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(path12));
             int bs = 10000;
             byte[] b =new byte[bs];
             int j = 0;
             while ((j = dis.read(b, 0, bs))!=-1){                                                        
                System.out.println("Recieving "+ j);
                 dos.write(b, 0, j);        
             }
             if(j==-1)
             {
             	
             }
	            server3.close();
	            socket3.close(); 
	            dos.close();
	}
public static  void tcpget() throws IOException
	{
	
	FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
  //create server and client socket
    ServerSocket ss = null;
    Socket socket4 = null;
    try {
      ss = new ServerSocket(12023);
       {   
        try {
        	socket4 = ss.accept();
        	 //create input and output stream
          InputStream input = socket4.getInputStream();
          BufferedReader reader = new BufferedReader(new InputStreamReader(input));
          String read=reader.readLine();
          //create file name 
          File file = new File (link+"/"+read);
          byte [] array_byte  = new byte [(int)file.length()];
          fis = new FileInputStream(file);
          bis = new BufferedInputStream(fis);
          //send content to file
          bis.read(array_byte,0,array_byte.length);
          os = socket4.getOutputStream();
          System.out.println("Sending " + link + "(" + array_byte.length + " bytes)");
          os.write(array_byte,0,array_byte.length);
          os.flush();   
        }
        finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (socket4!=null) socket4.close();
        }
      }
    }
    finally {
      if (ss != null) ss.close();
    }
	}
	
}	


