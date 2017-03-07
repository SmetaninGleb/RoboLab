package ru.li24robotics.ev3.robolab.lab;


import java.util.ArrayList;


public class LabAnalyzer {
	private static ArrayList<ArrayList<LabItem>> analyzeField;
	private static int [] corNow = {0, 0};
	
	public static void InitLabAnalyzer(String sensorPortName){

		
		analyzeField = new ArrayList<ArrayList<LabItem>>();
		analyzeField.add(new ArrayList<LabItem>());
		analyzeField.get(0).add(new LabItem("0"));
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
