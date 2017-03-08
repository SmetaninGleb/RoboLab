package ru.li24robotics.ev3.robolab.lab;


import java.util.ArrayList;


public class LabAnalyzer {
	private static ArrayList<ArrayList<LabItem>> analyzeField;
	private static int [] corNow = {0, 0};
	
	public static void InitLabAnalyzer(LabItem startItem){

		
		analyzeField = new ArrayList<ArrayList<LabItem>>();
		analyzeField.add(new ArrayList<LabItem>());
		analyzeField.get(0).add(startItem);
	}
	
	//about moving robot in field
	public static void putRobotToForwad(){
		corNow[1] ++;
	}
	
	public static void putRobotToBack(){
		corNow[1] --;	
	}
	
	public static void putRobotToRight(){
		corNow[0] ++;
	}
	
	public static void putRobotToLeft(){
		corNow[0] --;
	}
	
	//about building field
	public static void addItemToRight(LabItem item){
		if(corNow[0] == analyzeField.size() - 1){
			analyzeField.add(corNow[0] + 1, new ArrayList<LabItem>());
			
			for(int i = 0; i <analyzeField.get(corNow[0]).size(); i ++){
				if(i == corNow[1]){
					analyzeField.get(corNow[0] + 1).add(i, item);
				}else{
					analyzeField.get(corNow[0] + 1).add(i, null);
				}
			}
		}else{
			analyzeField.get(corNow[0]+1).set(corNow[1], item);
		}
	}
	
	public static void addItemToLeft(LabItem item){
		if(corNow[0] == 0){
			analyzeField.add(0, new ArrayList<LabItem>());
			corNow[0] ++;
			for(int i = 0; i < analyzeField.get(corNow[0]).size(); i ++){
				if(i == corNow[1]){
					analyzeField.get(0).add(i, item);
				}else{
					analyzeField.get(0).add(i, null);
				}
			}
		}else{
			analyzeField.get(corNow[0] - 1).set(corNow[1], item);
		}
	}
	
	public static void addItemToForward(LabItem item){
		if(corNow[1] == analyzeField.get(corNow[0]).size() - 1){
			for(int i = 0; i < analyzeField.size(); i++){
				if(i == corNow[0]){
					analyzeField.get(i).add(corNow[1] + 1,item);
				}else{
					analyzeField.get(i).add(corNow[1] + 1,null);
				}	
			}
		}else{
			analyzeField.get(corNow[0]).set(corNow[1] + 1, item);
		}
		
	}
	
	public static void addItemToBack(LabItem item){
		if(corNow[1] == 0){
			for (int i = 0; i < analyzeField.size(); i++){
				if(corNow[0] == i){
					analyzeField.get(i).add(0, item);
				}else{
					analyzeField.get(i).add(0, null);
				}
			}
			corNow[1] ++;
		}else{
			analyzeField.get(corNow[0]).set(corNow[1] - 1, item);
		}
	}

	//for get Robot's coordinates on another fields
	public static ArrayList<int[]> getRobotCoordinatesOnMainLab(ArrayList<ArrayList<LabItem>> field){
		ArrayList<int[]> potentialCorOnMainField = new ArrayList<int[]>();
		potentialCorOnMainField.addAll(getRobotCoordinatesNotRotatedAnalyzer(field));
		potentialCorOnMainField.addAll(getRobotCoordinatesRightRotatedAnalyzer((field)));
		potentialCorOnMainField.addAll(getRobotCoordinatesOverRotatedAnalyzer(field));
		potentialCorOnMainField.addAll(getRobotCoordinatesLeftRotatedAnalyzer(field));

		for(int i = 0; i < potentialCorOnMainField.size(); i ++){
			for(int j = 0; j < potentialCorOnMainField.size(); j++){
				if(potentialCorOnMainField.get(i)[0] == potentialCorOnMainField.get(j)[0] && potentialCorOnMainField.get(i)[1] == potentialCorOnMainField.get(j)[1] &&i != j){
					potentialCorOnMainField.remove(j);
				}
			}
		}

		return potentialCorOnMainField;
	}

	private static ArrayList<int[]> getRobotCoordinatesNotRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field){
		ArrayList<int[]> potentialCor = new ArrayList<int[]>();
		for(int j = 0; j <= field.get(0).size() - analyzeField.get(0).size(); j++){
			for(int i = 0; i <= field.size() - analyzeField.size(); i ++){
				if(equalsPartsOfFieldsNotRotated(field, i, j)){
					potentialCor.add(new int[] {i + corNow[0], j + corNow[1]});
				}
			}
		}
		return potentialCor;
	}
	private static boolean equalsPartsOfFieldsNotRotated (ArrayList<ArrayList<LabItem>> field, int x, int y){
		for(int i = 0; i < analyzeField.size(); i ++){
			for(int j = 0; j < analyzeField.get(i).size(); j++){
				if(!analyzeField.get(i).get(j).equalsNotRotate(field.get(i + x).get(j + y))){
					return false;
				}
			}
		}
		return true;
	}

	private static ArrayList<int[]> getRobotCoordinatesRightRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field){
		ArrayList<int[]> potentialCor = new ArrayList<int[]>();
		for(int j = 0; j <= field.get(0).size() - analyzeField.size(); j ++){
			for(int i = 0; i <= field.size() - analyzeField.get(0).size(); i ++){
				if(equalsPartsOfFieldsRightRotated(field, i, j)){
					potentialCor.add(new int[] {i + corNow[1], j + (analyzeField.size() - corNow[0] - 1)});
				}
			}
		}
		return potentialCor;
	}
	private static boolean equalsPartsOfFieldsRightRotated(ArrayList<ArrayList<LabItem>> field, int x, int y){
		for(int i = 0; i < analyzeField.size(); i ++){
			for (int j = 0; j < analyzeField.get(0).size(); j++){
				if (!analyzeField.get(i).get(j).equalsRightRotate(field.get(x + j).get(y + (analyzeField.size() - i - 1)))) {
					return false;
				}
			}
		}
		return true;
	}

	private static ArrayList<int[]> getRobotCoordinatesOverRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field){
		ArrayList<int[]> potentialCor = new ArrayList<int []>();
		for(int j = 0; j <= field.get(0).size() - analyzeField.get(0).size(); j++){
			for(int i = 0; i <= field.size() - analyzeField.size(); i ++){
				if (equalsPartsOfFieldsOverRotated(field, i, j)) {
					potentialCor.add(new int[] {i + (analyzeField.size() - corNow[0] - 1), j + (analyzeField.get(0).size() - corNow[1])});
				}
			}
		}
		return potentialCor;
	}
	private static boolean equalsPartsOfFieldsOverRotated(ArrayList<ArrayList<LabItem>> field, int x, int y){
		for(int i = 0; i < analyzeField.size(); i ++){
			for(int j = 0; j < analyzeField.get(0).size(); j ++){
				if(!analyzeField.get(i).get(j).equalsOverRotated(field.get(x + (analyzeField.size() - i - 1)).get(y + (analyzeField.get(0).size() - j - 1)))){
					return false;
				}
			}
		}
		return true;
	}

	private static ArrayList<int[]> getRobotCoordinatesLeftRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field){
		ArrayList<int[]> potentialCor = new ArrayList<int[]>();

		for(int j = 0; j <= field.get(0).size() - analyzeField.size(); j++){
			for(int i = 0; i <= field.size() - analyzeField.get(0).size(); i++){
				if(equalsPartsOfFieldsLeftRotated(field, i, j)){
					potentialCor.add(new int[] {i + (analyzeField.get(0).size() - corNow[1] - 1), j + corNow[0]});
				}
			}
		}

		return potentialCor;
	}
	private static boolean equalsPartsOfFieldsLeftRotated(ArrayList<ArrayList<LabItem>> field, int x, int y){
		for(int i = 0; i < analyzeField.size(); i ++){
			for(int j = 0; j < analyzeField.get(0).size(); j++){
				if(!analyzeField.get(i).get(j).equalsLeftRotated(field.get(x + analyzeField.get(0).size() - j - 1).get(y + i))){
					return false;
				}
			}
		}
		return true;
	}

	//for debug
	public static void outField(){
		for(int j = analyzeField.get(0).size() - 1; j >= 0; j --){
			for(int i = 0; i < analyzeField.size(); i ++){
				if(analyzeField.get(i).get(j) != null){
					System.out.print(analyzeField.get(i).get(j).toString() + "[" + i + "][" + j + "]");
				}else{
					System.out.print("n" + "[" + i + "][" + j + "]");
				}
				if(corNow[0] == i && corNow[1] == j){
					System.out.print("!");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	//getters and setters
	public static ArrayList<ArrayList<LabItem>> getAnalyzeField() {
		return analyzeField;
	}

	public static int[] getCorNow() {
		return corNow;
	}
}
