import java.io.*;
import java.util.*;

public class dbload {

    public static void main(String[] args) 
    {
        File file;
        int pagesize, datasize;
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
        datasize = pagesize - 4;
        //4 is the length of the data counter
        
    }
    
    private Record initialiseRecord(String path)
    {
        Record rec = new Record();
        String line, maxStr = "";
        BufferedReader read = null;
        int temp = 0;
        //read the data
        try
        {
            read = new BufferedReader(new FileReader(path));
            line = read.readLine();
            rec.setColumn(line);
            //get the longest length of the record size
            //so that it could accommodate all the records
            while((line = read.readLine()) != null)
            {
                if (line.length() > temp)
                {
                    temp = line.length();
                    maxStr = line;
                }
            }
            rec.setRecordSize(maxStr.getBytes().length);
            rec.setRecordLength(maxStr.length());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {
                read.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        return rec;
    }
    
    private void loadData(int pagesize, int datasize, String path)
    {
        DataOutputStream out = null;
        BufferedReader read = null;
        String line;
        StringBuilder page = new StringBuilder();
        int count = 0, pageCount = 0, totalCount = 0;
        ArrayList<String> pageData = new ArrayList<String>();
        byte[] pageByte = new byte[datasize];
        //create file
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
