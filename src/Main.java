import java.util.ArrayList;

public class Main {
	
	// ===== Configuration Values =====//
	public static final Integer QuerrySize  = 1000;
	//public static final Integer DataSetSize  = 15000000;
	public static final Integer DataSetSize  = 5000;
	public static final String DataFile = "tweets_15m.txt";
	public static final String FilesPath = "tmp/";
	public static final String sortedArrayFileName = FilesPath + "sorted.csv";
	public static final String[] Methods = {"frequent","infrequent","random"};
	public static final Integer[] D = {0,2,4,6,8,10,12,14};
	public static final double[] aprox = {0.5,0.2};
	public static double[][][] task3angles = new double[8][3][1000];
	public static double[][][] task4angles = new double[8][3][1000];
	
	public static double[][][] task4_05angles = new double[8][3][1000];
	public static double[][][] task4_02angles = new double[8][3][1000];
	
	// ===== To Run or Not to Run ===== //
	public static final boolean rerunTask1 = true;
	public static final boolean runBruteForce = true;
	public static final boolean runDataReduction = true;
	public static final boolean runTask2 = true;
	public static final boolean runTask3 = true;
	public static final boolean runTask4 = false;
	
	// ===== Main ===== //
	public static void main(String[] args) throws Exception{
		Tools.createFolder(FilesPath);
		ArrayList<Term> sortByNumberOfTweets = Tools.readerAL(sortedArrayFileName);
		
		//-----Task1-----//
		if(sortByNumberOfTweets.isEmpty() || rerunTask1){
			sortByNumberOfTweets = Tasks.task1(); //List of Sorted Tweets by Number
			Term.writerArrayTerms(sortedArrayFileName, sortByNumberOfTweets);
		}
		
		//-----Brute Force Algorithm-----//
		if(runBruteForce){
			long[][] time = new long[1][1];
			time[0][0] = Tasks.bruteforce();
			Tools.writerTimes("BF_time.csv", time);
			System.out.println("Total Running Time: " + time + "ms");
		}
		
		long[][] task2times = new long[8][3];
		long[][] task3times = new long[8][3];
		long[][] task4times_05 = new long[8][3];
		long[][] task4times_02 = new long[8][3];
		for(String ii : Methods){
			for(int jj : D){
				
				//-----DataReduction-----//
				if(runDataReduction){
					long startTime = System.currentTimeMillis();
					Reduction.dataReduction(ii, jj, sortByNumberOfTweets);
					long endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					System.out.println("Data Reduction completed. Total time for reduction: " + totalTime + "ms");
				}
			
				//-----Task2-----//
				if(runTask2){
					int kk;
					for(kk =0;! Methods[kk].equals(ii); kk++);
					task2times[jj/2][kk] = Tasks.task2(ii, jj);
				}
				
				//-----Task3-----//
				if(runTask3){
					int kk;
					for(kk =0;! Methods[kk].equals(ii); kk++);
					task3times[jj/2][kk] = Tasks.task34(ii, jj, 0);
				}
				
				//-----Task4-----//
				if(runTask4){
					for(double aproxv : aprox){
						int kk;
						for(kk =0;! Methods[kk].equals(ii); kk++);
						long time =  Tasks.task34(ii, jj, aproxv);
						
						if(aprox[0] == aproxv)
							task4times_05[jj/2][kk] = time;
						else
							task4times_02[jj/2][kk] = time;
					}
				}
				Reduction.deleteData(ii, jj);
			}
		}
		Tools.writerTimes("Task2.csv", task2times);
		Tools.writerTimes("Task3.csv", task3times);
		Tools.writerAngles("Task3_angles.csv", task3angles);
		Tools.writerTimes("Task4_" + "aprox05" + ".csv", task4times_05);
		Tools.writerAngles("Task4_" + "aprox05" + "_angles.csv", task4_05angles);
		Tools.writerTimes("Task4_" + "aprox02" + ".csv", task4times_05);
		Tools.writerAngles("Task4_" + "aprox02" + "_angles.csv", task4_02angles);
	}
}
