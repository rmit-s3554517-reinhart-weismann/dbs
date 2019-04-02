import java.io.*;
import java.util.*;

public class dbload {

    public static void main(String[] args) 
    {
        File file;
        int pagesize;
        dbload dbl;
        String path;
        
        //if argument is wrong
        if(!args[0].equals("-p") || args.length != 3)
        {
            System.out.println("wrong arguments");
            return;
        }
        
        file = new File(args[2]);
        if(!file.exists()) //if file is missing
        {
            System.out.println("file does not exist");
            return;
        }
        
        dbl = new dbload();
        pagesize = Integer.parseInt(args[1]);
        path = args[2];
        
    }
    
    private void loadData(int pagesize, String path)
    {
        DataOutputStream out = null;
        BufferedReader read = null;
        String line;
        int count = 0, pageCount = 0, totalCount;
        ArrayList<String> pageData = new ArrayList<String>();
        byte[] pageByte = new byte[pagesize];
        
        try
        {
            read = new BufferedReader(new FileReader(path));
            //the file name is heap.pagesize
            out = new DataOutputStream(new FileOutputStream("heap." + pagesize));
            line = read.readLine();
            //convert the line length and line into bytes
            out.writeInt(line.getBytes().length);
            out.writeBytes(line);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
