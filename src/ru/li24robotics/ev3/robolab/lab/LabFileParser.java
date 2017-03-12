package ru.li24robotics.ev3.robolab.lab;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Класс парсит лабиринт из файла. Внимание, принимается специальный вид хранения в лабиринте!
 * Для правильноq работы парсера, используйте специальный инструмент для конвертации(LabParser Project)
 * Файл имеет название MainField.lab
 *
 * @author Smetanin Gleb
 */
public class LabFileParser{
    private static ArrayList<ArrayList<LabItem>> mainLab;
    private static File labFile;
    private static Scanner scanner;

    public static void InitLabFileParser() throws FileNotFoundException {
        scanner = new Scanner(labFile);
        labFile = new File("MainField.lab");
        mainLab = new ArrayList<ArrayList<LabItem>>();
    }



    public static ArrayList<ArrayList<LabItem>> parseLab(){
        for(int j = 0; scanner.hasNextLine(); j++){
            for(int i = 0; scanner.next() != "l"; i++){
                if(j == 0){
                    mainLab.add(i, new ArrayList<LabItem>());
                }
                mainLab.get(i).add(0, new LabItem(Integer.toString(scanner.nextInt())));

                if(scanner.nextInt() == 1){
                    mainLab.get(i).get(0).toForward.setWallIsHere(true);
                }else{
                    mainLab.get(i).get(0).toForward.setWallIsHere(false);
                }
                if(scanner.nextInt() == 1){
                    mainLab.get(i).get(0).toRight.setWallIsHere(true);
                }else{
                    mainLab.get(i).get(0).toRight.setWallIsHere(false);
                }
                if(scanner.nextInt() == 1){
                    mainLab.get(i).get(0).toBack.setWallIsHere(true);
                }else{
                    mainLab.get(i).get(0).toBack.setWallIsHere(false);
                }
                if(scanner.nextInt() == 1){
                    mainLab.get(i).get(0).toLeft.setWallIsHere(true);
                }else{
                    mainLab.get(i).get(0).toLeft.setWallIsHere(false);
                }
            }
        }
        return mainLab;
    }
}
