
public class Record 
{
    private String column;
    private int recordSize;
    private int recordLength;
    public int getRecordLength() 
    {
        return recordLength;
    }
    public void setRecordLength(int recordLength) 
    {
        this.recordLength = recordLength;
    }
    public String getColumn() 
    {
        return column;
    }
    public void setColumn(String column) 
    {
        this.column = column;
    }
    public int getRecordSize() 
    {
        return recordSize;
    }
    public void setRecordSize(int recordSize) 
    {
        this.recordSize = recordSize;
    }
}
