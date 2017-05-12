package ru.li24robotics.ev3.robolab.lab;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Класс парсит лабиринт из файла. Внимание, принимается специальный вид хранения в лабиринте!
 * Для правильноq работы парсера, используйте специальный инструмент для конвертации(LabParser Project)
 * Файл имеет название MainLab.out
 *
 * @author Smetanin Gleb
 */
public class LabFileParser {
    private static ArrayList<ArrayList<LabItem>> mainLab;
    private static FileInputStream fileInputStream;
    private static ObjectInputStream objectInputStream;


    public static void InitLabFileParser()
    {
    	try {
    		fileInputStream = new FileInputStream("MainLab.out");
    		objectInputStream = new ObjectInputStream(fileInputStream);
    	} catch(Exception e) {
    		System.err.println("No MainLab.out File!!!");
    		fileInputStream = null;
    		objectInputStream = null;
    	}
    }


    public static ArrayList<ArrayList<LabItem>> getMainLab() {
    	try {
        ArrayList<ArrayList<LabItem>> mainLab = (ArrayList<ArrayList<LabItem>>) objectInputStream.readObject();
        return mainLab;
    	}catch(Exception e){
    		System.err.println("Cannot read object from MainLab.out file!!!");
    		return null;
    	}
    }
}
