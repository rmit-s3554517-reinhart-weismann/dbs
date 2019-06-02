import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;

/**
 *  Database Systems - HEAP IMPLEMENTATION
 */
 
public class dbload implements dbimpl
{
	
    // initialize
   public static void main(String args[])
   {
      dbload load = new dbload();

      // calculate load time
      long startTime = System.currentTimeMil0lis();
      load.readArguments(args);
      long endTime = System.currentTimeMillis();

      System.out.println("Load time: " + (endTime - startTime) + "ms");
	  
	  
	  
	  BPlusTree bpt = new BPlusTree();
   }

   // reading command line arguments
   public void readArguments(String args[])
   {
      if (args.length == 3)
      {
         if (args[0].equals("-p") && isInteger(args[1]))
         {
            readFile(args[2], Integer.parseInt(args[1]));
         }
      }
      else
      {
         System.out.println("Error: only pass in three arguments");
      }
   }

   // check if pagesize is a valid integer
   public boolean isInteger(String s)
   {
      boolean isValidInt = false;
      try
      {
         Integer.parseInt(s);
         isValidInt = true;
      }
      catch (NumberFormatException e)
      {
         e.printStackTrace();
      }
      return isValidInt;
   }
   
   
   

   // read .csv file using buffered reader
   public void readFile(String filename, int pagesize)
   {
      dbload load = new dbload();
      File heapfile = new File(HEAP_FNAME + pagesize);
      BufferedReader br = null;
      FileOutputStream fos = null;
      String line = "";
      String nextLine = "";
      String stringDelimeter = ",";
      byte[] RECORD = new byte[RECORD_SIZE];
      int outCount, pageCount, recCount;
      outCount = pageCount = recCount = 0;
	  BPlusTree tree = new BPlusTree(5000000);         

      try
      {
		int id = 0;
	  		  
		  		  
         // create stream to write bytes to according page size
         fos = new FileOutputStream(heapfile);
         br = new BufferedReader(new FileReader(filename));
         // read line by line
         while ((line = br.readLine()) != null)
         {
            String[] entry = line.split(stringDelimeter, -1);
            RECORD = createRecord(RECORD, entry, outCount);
            // outCount is to count record and reset everytime
            // the number of bytes has exceed the pagesize
			tree.insertIntoTree(new DataNode(Integer.parseInt(entry[0])));
            
			
			outCount++;

            fos.write(RECORD);
            if ((outCount+1)*RECORD_SIZE > pagesize)
            {
               eofByteAddOn(fos, pagesize, outCount, pageCount);
               //reset counter to start newpage
               outCount = 0;
               pageCount++;
            }
            recCount++;
         }
      }
      catch (FileNotFoundException e)
      {
         System.out.println("File: " + filename + " not found.");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (br != null)
         {
            try
            {
               // final add on at end of file
               if ((nextLine = br.readLine()) == null)
               {
                  eofByteAddOn(fos, pagesize, outCount, pageCount);
                  pageCount++;
               }
               fos.close();
               br.close();
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         }
      }
      System.out.println("Page total: " + pageCount);
      System.out.println("Record total: " + recCount);
   }

   // create byte array for a field and append to record array at correct 
   // offset using array copy
   public void copy(String entry, int SIZE, int DATA_OFFSET, byte[] rec)
          throws UnsupportedEncodingException
   {
      byte[] DATA = new byte[SIZE];
      byte[] DATA_SRC = entry.trim().getBytes(ENCODING);
      if (entry != "")
      {
         System.arraycopy(DATA_SRC, 0,
                DATA, 0, DATA_SRC.length);
      }
      System.arraycopy(DATA, 0, rec, DATA_OFFSET, DATA.length);
   }

   // creates record by appending using array copy and then applying offset
   // where neccessary
   public byte[] createRecord(byte[] rec, String[] entry, int out)
          throws UnsupportedEncodingException 
   {
      byte[] RID = intToByteArray(out);
      System.arraycopy(RID, 0, rec, 0, RID.length);

      copy(entry[0], DEVICE_ID_SIZE, RID_SIZE, rec);
      copy(entry[1], ARRIVAL_TIME_SIZE, ARRIVAL_TIME_OFFSET, rec);
      copy(entry[2], DEPART_TIME_SIZE, DEPART_TIME_OFFSET, rec);
      copy(entry[3], DURATION_SIZE, DURATION_OFFSET, rec);
      copy(entry[4], STREET_MARKER_SIZE, STREET_MARKER_OFFSET, rec);
      copy(entry[5], SIGN_SIZE, SIGN_OFFSET, rec);
      copy(entry[6], AREA_SIZE, AREA_OFFSET, rec);
      copy(entry[7], STREET_ID_SIZE, STREET_ID_OFFSET, rec);
      copy(entry[8], STREET_NAME_SIZE, STREET_NAME_OFFSET, rec);
      copy(entry[9], BETWEEN_STREET_1_SIZE, BETWEEN_STREET_1_OFFSET, rec);
      copy(entry[10], BETWEEN_STREET_2_SIZE, BETWEEN_STREET_2_OFFSET, rec);
      copy(entry[11], SIDE_OF_STREET_SIZE, SIDE_OF_STREET_OFFSET, rec);
      copy(entry[12], VIOLATION_SIZE, VIOLATION_OFFSET, rec);
      // copy(entry[13], PRESENT_SIZE, PRESENT_OFFSET, rec);

      return rec;
   }

   // EOF padding to fill up remaining pagesize
   // * minus 4 bytes to add page number at end of file
   public void eofByteAddOn(FileOutputStream fos, int pSize, int out, int pCount) 
          throws IOException
   {
      byte[] fPadding = new byte[pSize-(RECORD_SIZE*out)-4];
      byte[] bPageNum = intToByteArray(pCount);
      fos.write(fPadding);
      fos.write(bPageNum);
   }

   // converts ints to a byte array of allocated size using bytebuffer
   public byte[] intToByteArray(int i)
   {
      ByteBuffer bBuffer = ByteBuffer.allocate(4);
      bBuffer.putInt(i);
      return bBuffer.array();
   }

	
}

// create a B Plus Tree class
class BPlusTree {
	private static Node tree;
	private static int degree;

	public BPlusTree(){}
	public BPlusTree(int x) {
	  // a B+ Tree must have an initial degree
		degree = x;
		
		// The initial type of Node for a B+Tree is a leaf
		tree = new LeafNode(degree);
	}
	
	protected void insertIntoTree(DataNode dnode) {
		tree = tree.insert(dnode);
	}
	
}





// The node class is an abstract class
// its subclasses are LeafNode and TreeNode
abstract class Node {
	// all nodes need data, parent, and a capacity
	protected Vector<DataNode> data;
	protected Node parent;
	protected int maxsize;

	public boolean isLeafNode() {
		// determine if a node is a leafnode
		return this.getClass().getName().trim().equals("LeafNode");
	}

	// both types of node need to insert and search
	abstract Node insert(DataNode dnode);
	abstract boolean search(DataNode x);

	protected boolean isFull() {
		return data.size() == maxsize-1;
	}
	
	public DataNode getDataAt(int index) {
		return (DataNode) data.elementAt(index);
	}
	
	protected void propagate(DataNode dnode, Node right) {
		// propogate takes a piece of data and two pointers left(this) and right and pushes the data up the tree

		// if there was no parent
		if(parent == null) {
			
			// create a new parent
			TreeNode newparent = new TreeNode(maxsize);
			
			// add the necessary data and pointers
			newparent.data.add(dnode);
			newparent.pointer.add(this);
			newparent.pointer.add(right);
			
			// update the parent information for right and left
			this.setParent(newparent);
			right.setParent(newparent);
		}
		else {
			// if the parent is not full
			if( ! parent.isFull() ) {
				// add the necessary data and pointers to existing parent
				boolean dnodeinserted = false;
				for(int i = 0; !dnodeinserted && i < parent.data.size(); i++) {
					if( ((DataNode)parent.data.elementAt(i)).inOrder(dnode) ) {
						parent.data.add(i,dnode);
						((TreeNode)parent).pointer.add(i+1, right);
						dnodeinserted = true;
					}
				}
				if(!dnodeinserted) {
					parent.data.add(dnode);
					((TreeNode)parent).pointer.add(right);
				}
				
				// set the necessary parent on the right node, left.parent is already set
				right.setParent(this.parent);
			}
			// the parent is full
			else {
				// split will take car of setting the parent of both nodes, because 
				// there are 3 different ways the parents need to be set
				((TreeNode)parent).split(dnode, this, right);

			}
		}
	}
	
	public int size() {
		return data.size();
	}

	@SuppressWarnings("unchecked") Node(int degree) {
		// initially the parent node is null
		parent = null;
		
		data = new Vector();
		maxsize = degree;
	}
	
	// Convert a node to a string
	public String toString() {
		String s = "";
		for(int i=0; i < data.size(); i++) {
			s += ((DataNode)data.elementAt(i)).toString() + " ";
		}
		return s + "#";
	}

	// this operation traverses the tree using the parent nodes until the parent is null and returns the node
	protected Node findRoot() {
		Node node = this;
		
		while(node.parent != null) {
			node = node.parent;
		}
		
		return node;
	}

	protected void setParent(Node newparent) {
		this.parent = newparent;
	}
} 

class LeafNode extends Node {
	private LeafNode nextNode;
	
	LeafNode(int degree) {
		super(degree);
		
		// initially the nextnode is null
		nextNode = null;
	}
	
	private void setNextNode(LeafNode next) {
		nextNode = next;
	}
	
	protected LeafNode getNextNode() {
		return nextNode;
	}

	public boolean search(DataNode x) {
		// search through the data sequentially until x is found, or there are no more entries
		for(int i=0; i < data.size(); i++) {
			if( ((DataNode)data.elementAt(i)).getData() == x.getData() ) {
				return true;
			}
		}
		return false;
	}

	protected void split(DataNode dnode) {
		// insert dnode into the vector (it will now be overpacked)
		boolean dnodeinserted = false;
		for(int i=0; !dnodeinserted && i < data.size(); i++) {
			if( ((DataNode)data.elementAt(i)).inOrder(dnode) ) {
				data.add(i,dnode);
				dnodeinserted = true;
			}
		}
		if(!dnodeinserted) {
			data.add(data.size(), dnode);
		}
		
		// calculate the split point; ceiling(maxsize/2)
		int splitlocation;
		if(maxsize%2 == 0) {
			splitlocation = maxsize/2;
		}
		else {
			splitlocation = (maxsize+1)/2;
		}
				
		// create new LeafNode
		LeafNode right = new LeafNode(maxsize);
		
		for(int i = data.size()-splitlocation; i > 0; i--) {
			right.data.add(data.remove(splitlocation));
		}
		
		// link the two together
		right.setNextNode(this.getNextNode());
		this.setNextNode(right);
		
		// get the middle item's data
		DataNode mid =  (DataNode) data.elementAt(data.size()-1);

		// propagate the data and pointers into the parent node
		this.propagate(mid, right);
	}

	public Node insert(DataNode dnode) {
		// if the leaf isn't full insert it at the proper place
		if(data.size() < maxsize-1) {
			boolean dnodeinserted = false;
			int i = 0;
			while(!dnodeinserted && i < data.size()) {
				if( ((DataNode)data.elementAt(i)).inOrder(dnode) ) {
					data.add(i,dnode);
					dnodeinserted = true;
				}
				i++;
			}
			if(!dnodeinserted) {
				data.add(data.size(), dnode);
			}
		}
		
		// if the leaf is full split
		else {
			this.split(dnode);
		}
		
		// return the root of the tree
		return this.findRoot();
	}
}

class TreeNode extends Node {
	protected Vector<Node> pointer;
	
	// constructor for TreeNode
	// x-1 is the maximum # of DataNodes a single node can store 
	@SuppressWarnings("unchecked") TreeNode(int x) {
		super(x);
		pointer = new Vector();
	}

	// this will find the correct pointer to the next lowest level of the tree where x should be found
	public Node getPointerTo(DataNode x) {
		// find the index i where x would be located
		int i = 0;
		boolean xptrfound = false;
		while(!xptrfound && i < data.size()) {
			if( ((DataNode)data.elementAt(i)).inOrder(x ) ) {
				xptrfound = true;
			}
			else {
				i++;				
			}

		}
		
		
		// return the Node in pointer(i)
		return (Node) pointer.elementAt(i);
		
	}

	// returns the pointer at a specific index in the pointer stack
	public Node getPointerAt(int index) {
		return (Node) pointer.elementAt(index);
	}

	boolean search(DataNode dnode) {
		// get a pointer to where dnode.data should be found
		Node next = this.getPointerTo(dnode);
	
		// recursive call to find dnode.data if it is present
		return next.search(dnode);
	}

	protected void split(DataNode dnode, Node left, Node right) {
		// calculate the split point ( floor(maxsize/2)
		int splitlocation, insertlocation = 0; 
		if(maxsize%2 == 0) {
			splitlocation = maxsize/2;
		}
		else {
			splitlocation = (maxsize+1)/2 -1;
		}
		
		// insert dnode into the vector (it will now be overpacked)
		boolean dnodeinserted = false;
		for(int i=0; !dnodeinserted && i < data.size(); i++) {
			if( ((DataNode)data.elementAt(i)).inOrder(dnode) ) {
				data.add(i,dnode);
				((TreeNode)this).pointer.remove(i);
				((TreeNode)this).pointer.add(i, left);
				((TreeNode)this).pointer.add(i+1, right);
				dnodeinserted = true;
				
				// set the location of the insert this will be used to set the parent
				insertlocation = i;
			}
		}
		if(!dnodeinserted) {
			// set the location of the insert this will be used to set the parent
			insertlocation = data.size();
			data.add(dnode);
			((TreeNode)this).pointer.remove(((TreeNode)this).pointer.size()-1);
			((TreeNode)this).pointer.add(left);
			((TreeNode)this).pointer.add(right);
			
		}
		
		// get the middle dataNode
		DataNode mid = (DataNode) data.remove(splitlocation);
		
		// create a new tree node to accomodate the split 
		TreeNode newright = new TreeNode(maxsize);
		
		// populate the data and pointers of the new right node
		for(int i=data.size()-splitlocation; i > 0; i--) {
			newright.data.add(this.data.remove(splitlocation));
			newright.pointer.add(this.pointer.remove(splitlocation+1));
		}
		newright.pointer.add(this.pointer.remove(splitlocation+1));		

		// set the parents of right and left
		// if the item was inserted before the split point both nodes are children of left
		if(insertlocation < splitlocation) {
			left.setParent(this);
			right.setParent(this);
		}
		// if the item was inserted at the splitpoint the nodes have different parents this and right
		else if(insertlocation == splitlocation) {
			left.setParent(this);
			right.setParent(newright);
		}
		// if the item was was inserted past the splitpoint the nodes are children of right
		else {
			left.setParent(newright);
			right.setParent(newright);
		}
		
		// propogate the node up
		this.propagate(mid, newright);
	}

	Node insert(DataNode dnode) {
		Node next = this.getPointerTo(dnode);
		
		return next.insert(dnode);
	}
}

class DataNode {
	// I chose Integer because it allows a null value, unlike int
	private Integer data;
	
	DataNode() {
		data = null;
	}   
	public String toString() {
		return data.toString();
	}
	public DataNode(int x) {
		data = x;
	}
	public int getData() {
		return data.intValue();
	}   
	public boolean inOrder(DataNode dnode) {
		return (dnode.getData() <= this.data.intValue());
	}
}




