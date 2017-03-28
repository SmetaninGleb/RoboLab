package ru.li24robotics.ev3.robolab.lab;

import java.io.*;
import java.util.ArrayList;

/**
 * Класс парсит лабиринт из файла. Внимание, принимается специальный вид хранения в лабиринте!
 * Для правильноq работы парсера, используйте специальный инструмент для конвертации(LabParser Project)
 * Файл имеет название MainLab.out
 *
 * @author Smetanin Gleb
 */
public class LabFileParser{
    private static ArrayList<ArrayList<LabItem>> mainLab;
    private static FileInputStream fileInputStream;
    private static ObjectInputStream objectInputStream;


    public static void InitLabFileParser() throws IOException {
        fileInputStream = new FileInputStream("MainLab.out");
        objectInputStream = new ObjectInputStream(fileInputStream);
    }



    public static ArrayList<ArrayList<LabItem>> getMainLab() throws IOException, ClassNotFoundException {
        ArrayList<ArrayList<LabItem>> mainLab = (ArrayList<ArrayList<LabItem>>) objectInputStream.readObject();
        return mainLab;
    }
}
