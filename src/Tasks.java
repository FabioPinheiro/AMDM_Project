import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.text.StyledEditorKit.BoldAction;


public class Tasks {

	//=====TASK 1 =====//
	public static ArrayList<Term> task1() throws IOException{
		System.out.println("Task 1 initiated.");
		DataBuff reader = new DataBuff(Main.DataFile);
		
		HashMap<String,Term> map = Tools.mapcreator();

		String stringLine = reader.buffer.readLine();
		String[] terms = stringLine.split("\\s+");
		for(int line = 0; (stringLine = reader.buffer.readLine()) != null && line<Main.DataSetSize; line++){
			for(int j=0;j<terms.length;j++){
				Term obj = (Term) map.get(terms[j]);
				if(obj != null){
					obj.numberOfTweets++;
				}
			}
			terms = stringLine.split("\\s+");
		}

		ArrayList<Term> sortByNumberOfTweets = new ArrayList<Term>();
		sortByNumberOfTweets.addAll(map.values());

		Collections.sort(sortByNumberOfTweets, new Comparator<Term>() {
			@Override
			public int compare(Term o1, Term o2){
				if (o1.numberOfTweets < o2.numberOfTweets)
					return 1;
				else if(o1.numberOfTweets == o2.numberOfTweets)
					return o1.text.compareTo(o2.text);
				else
					return -1;
			}
		});
		reader.buffer.close();
		System.out.println("Task 1 terminated." + "\n" + "--------------------");
		return sortByNumberOfTweets;
	}

	//=====BRUTE FORCE =====//
	public static long bruteforce() throws IOException{
		System.out.println("Brute Force initiated.");
		DataBuff Buff = new DataBuff(Main.DataFile);
		
		ArrayList<String> Querry = new ArrayList<String>();
		String stringLine = null;
		double[] angles = new double[1000];
		Arrays.fill(angles, Math.PI/2);
		
		//int indextwt = 0;
		//int indexque = 0;
		//String twt = null;
		//String que = null;
		
		for(int i = 0; i<Main.QuerrySize;i++){
			stringLine = Buff.buffer.readLine();
			Querry.add(stringLine);
		}
		System.out.println("Querry saved in memory. Starting to read.");
		
		long startTime = System.currentTimeMillis();
		stringLine = Buff.buffer.readLine();
		for(int line = Main.QuerrySize; (line<Main.DataSetSize && stringLine!=null);line++){
			String[] Lsplitted = stringLine.split("\\s+");
			
			for(int ii=0;ii<1000;ii++){ //for(String querry : Querry){
				String querry = Querry.get(ii);
				String[] Qsplitted = querry.split("\\s+");

				double aux = Tools.angle_bruteforce(Lsplitted, Qsplitted);
				if(aux<angles[ii]){
					angles[ii] = aux;
					//indextwt = Buff.buffer.getLineNumber();
					//indexque = Querry.indexOf(querry);
					//twt = stringLine;
					//que = querry;
				}
			}
			if(line%500000==0) System.out.println("Current line: " + line);
			stringLine = Buff.buffer.readLine();
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		//System.out.println("Angle: " + angle + "\n" + "Querry at line " + indexque + ": " + que + "\n" + "Tweet at line " + indextwt + ": " + twt);
		System.out.println("Total Running Time: " + totalTime + "ms");
		System.out.println("Brute Force terminated.");
		Buff.buffer.close();
		return totalTime;
	}
	
	//=====TASK 2 =====//
	public static long task2(String method,int d) throws Exception{
		System.out.println("Task 2 initiated using Method " + method + " and d=" + d);
		String path_file = Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+ d;
		
		DataBuff Buff = new DataBuff(path_file);
		
		ArrayList<Tweet> Querry = new ArrayList<Tweet>();
		
		double[] angles = new double[1000];
		Arrays.fill(angles, Math.PI/2);
		
		//Tweet querryMin = new Tweet();
		//Tweet tweetMin = new Tweet();

		for(int line=0;line<Main.QuerrySize;line++){
			Querry.add(Buff.getNextTweet());
		}
		
		Tweet tweet = Buff.getNextTweet();
		
		long startTime = System.currentTimeMillis();
		while(tweet != null){
			for(int ii=0;ii<1000;ii++){ //for(Tweet querry : Querry){
				Tweet querry = Querry.get(ii);
				double aux = Tools.angleAlphabet(querry, tweet);
				if(aux<angles[ii]){
					angles[ii] = aux;
					//querryMin = querry;
					//tweetMin = tweet;
				}
			}
			tweet = Buff.getNextTweet();
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		//System.out.println("Minimum Angle: " + angle);
		//System.out.println("Querry at line " + querryMin.index + ": " + querryMin + "\n" + "Tweet at line " + tweetMin.index + ": " + tweetMin);
		System.out.println("Total Running Time: " + totalTime + "ms");
		System.out.println("--------------------");
		return totalTime;
	}

	public static class AngleXPTO{ 
		public final int index; 
		public Double angle;
		
		public static Comparator<AngleXPTO> compF = new Comparator<AngleXPTO>() {
			@Override
			public int compare(AngleXPTO o1, AngleXPTO o2){
				int ret = o1.angle.compareTo(o2.angle);
				if (ret == 0 )
					ret = Integer.compare(o1.index,o2.index);
				if (ret == 0 )
					throw new RuntimeException();
				return ret;
			}
		};
		public static Comparator<AngleXPTO> compI = new Comparator<AngleXPTO>() {
			@Override
			public int compare(AngleXPTO o1, AngleXPTO o2){
				return Integer.compare(o1.index,o2.index);
			}
		};
		
		public AngleXPTO(int index, Double angle) { 
			this.index = index; 
			this.angle = angle; 
		}
		@Override
		public String toString(){
			return " ;" + "index "  + index  + "   angle " + angle;
		}
	}
	
	//=====TASK 3 and 4 =====//
	public static long task34(String method,int d, double aprox) throws Exception{
		System.out.println("Task "+ (aprox==0 ? "3" : "4" ) + " initiated using Method " + method + " and d=" + d + " and error of " + aprox*100 + "%");
		String path_file = Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+ d;
		
		DataBuff Buff = new DataBuff(path_file);
		
		ArrayList<Tweet> Querry = new ArrayList<Tweet>();
		//Tweet querryMin = new Tweet();
		//Tweet tweetMin = new Tweet();
		
		
		ArrayList<AngleXPTO> anglesXPTO = new ArrayList<AngleXPTO>(1000);
		for(int ii = 0; ii<1000 ; ii++)
			anglesXPTO.add(new AngleXPTO(ii, Math.PI/2+aprox*Math.PI/2));
		
		for(int line=0;line<Main.QuerrySize;line++){
			Querry.add(Buff.getNextTweet());
		}
		
		
		
		long startTime = System.currentTimeMillis();
		boolean update=false;
		Tweet tweet = Buff.getNextTweet();
		do{ // there is a first time for everything!! =P
			double optimistic = Math.acos(tweet.listOfTerms.size()/tweet.numberOfTerms);
			
			for(int ii=999;ii>=0 && (anglesXPTO.get(ii).angle > optimistic); ii--){
				int q= anglesXPTO.get(ii).index;
				//if(ii==0) System.out.println("ssssssssssssssssssssssssssssssssssssssssssss");
				Tweet querry = Querry.get(q);
				double aux = Tools.angleAlphabet(querry, tweet);
				if(aux < anglesXPTO.get(ii).angle){
					anglesXPTO.get(ii).angle = aux;
					update = true;
				}
				
			}
			if(update==true){
				Collections.sort(anglesXPTO, AngleXPTO.compF);
			}
			tweet = Buff.getOptimisticTweet(anglesXPTO.get(999).angle - aprox*Math.PI/2); //AngleXPTO[0] is the bigest angle
		}while(tweet != null);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		Collections.sort(anglesXPTO, AngleXPTO.compI);
		for(int ii=0; ii<1000; ii++){
			if(aprox==0) Main.task3angles
				[Arrays.asList(Main.D).indexOf(d)]
				[Arrays.asList(Main.Methods).indexOf(method)]
				[anglesXPTO.get(ii).index]= anglesXPTO.get(ii).angle;
			else Main.task4angles
				[Arrays.asList(Main.D).indexOf(d)]
				[Arrays.asList(Main.Methods).indexOf(method)]
				[anglesXPTO.get(ii).index]= anglesXPTO.get(ii).angle;
		}
		//System.out.println("AngleS: " + anglesXPTO);
		System.out.println("Total Running Time: " + totalTime + "ms");
		System.out.println("--------------------");
		return totalTime;
	}
}