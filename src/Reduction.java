import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Reduction {

	public static void deleteData(String method, int d) throws IOException{
		Files.deleteIfExists(Paths.get(Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+d ));
	}
	
	//===Subspace===//
	public static ArrayList<String> subspace(String method, int d, ArrayList<Term> sort){
		//Create array of terms only
		ArrayList<String> SortedTerms = new ArrayList<String>();
		for(Term e : sort){
			SortedTerms.add(e.text);
		}
		
		ArrayList<String> reduced = new ArrayList<String>();
		int D = 100*2^d;
		
		if(method.equals("frequent")){
			for(int i=0; i<D; i++)
				reduced.add(SortedTerms.get(i));
		}
		else if(method.equals("infrequent")){
			for(int i =0; i<D; i++)
				reduced.add(SortedTerms.get(sort.size()-i-1));
		}
		else if(method.equals("random")){
			ArrayList<Integer> randIntList = new ArrayList<Integer>();
			while(randIntList.size()<D){ 
				int rand=Tools.randInt(0, SortedTerms.size()-1);
				if(!randIntList.contains(rand)){
					randIntList.add(rand);
				}
			}
			for(int i=0;i<D;i++){
				reduced.add(SortedTerms.get(randIntList.get(i)));
			}
		}
		else throw new RuntimeException();
		return reduced;
	}
	
	// ===== Writer for Files ===== //
	public static BufferedWriter getBufferWriter(String method, int d) throws IOException{
		return new BufferedWriter(new FileWriter(Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+ d));
	}

	// ===== Create Reduced Files =====//
	public static void dataReduction(String method, int d, ArrayList<Term> sort) throws IOException{
		System.out.println("Data Reduction initiated using Method " + method + " and d=" + d);
		DataBuff reader = new DataBuff(Main.DataFile);
		String stringLine;
		ArrayList<String>  subspace = subspace(method, d, sort);
		BufferedWriter outputWriter = getBufferWriter(method, d);
		int line = 0;
		stringLine = reader.buffer.readLine();

		while(line<Main.DataSetSize && stringLine != null){
			String[]  terms = stringLine.split("\\s+");
			
			String listOfTermsSTRING = new String();
			
			for(int i=0;i<terms.length;i++){
				if(line<Main.QuerrySize){
					listOfTermsSTRING += " " + terms[i];
				}
				else if(line>= Main.QuerrySize && subspace.contains(terms[i])){
					listOfTermsSTRING += " " + terms[i];
				}
				else{
					//listOfTermsSTRING += " ";
				}
			}

			stringLine = reader.buffer.readLine();

			if(listOfTermsSTRING.length()!=0 && stringLine!=null){
				outputWriter.write(line + " " + terms.length + " " + listOfTermsSTRING);
				outputWriter.newLine();
			}
			else if(listOfTermsSTRING.length()!=0 && stringLine==null){
				outputWriter.write(line + " " + terms.length + " " + listOfTermsSTRING);
			}

			line+=1;
		}
		outputWriter.flush();
		outputWriter.close();
		reader.buffer.close();
	}
}
