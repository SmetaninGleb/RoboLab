package ru.li24robotics.ev3.robolab.lab;


import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Класс анализирует лабиринт, который видит робот, и хранит
 * его в статичном поле.
 *
 * @author Smetanin Gleb
 */

public class LabAnalyzer {
    /**
     * Поле {@code analyzeField} хранит лабиринт, который проанализировал робот
     * Поле {@code corNow} хранит координаты анализированного лабиринта, в которых стоит робот(corNow[0] - x, corNow[1] - y)
     */
    private static ArrayList<ArrayList<LabItem>> analyzeField;
    private static int[] corNow = {0, 0};

    /**
     * Инициализирует класс анализатора, создает {@code analyzeField}, получает на вход первый {@link LabItem}
     *
     * @see ru.li24robotics.ev3.robolab.lab.LabItem
     */
    public static void InitLabAnalyzer(LabItem startItem)
    {
        analyzeField = new ArrayList<ArrayList<LabItem>>();
        analyzeField.add(new ArrayList<LabItem>());
        analyzeField.get(0).add(startItem);
    }

    /**
     * Методы {@code putRobotToBack}, {@code putRobotToForward},
     * {@code putRobotToRight}, {@code putRobotToLeft} перемещает робота в анализируемом поле {@code analyzeField},
     * меняет координаты {@code corNow} робота в нем
     */
    public static void putRobotToForward()
    {
        if (analyzeField.get(0).size() - 1 == corNow[1]) {
            addItemToForward(new LabItem("2"));
        }
        corNow[1]++;
    }

    public static void putRobotToBack()
    {
        if (corNow[1] == 0) {
            addItemToBack(new LabItem("2"));
        }
        corNow[1]--;
    }

    public static void putRobotToRight()
    {
        if (corNow[0] == analyzeField.size() - 1) {
            addItemToRight(new LabItem("2"));
        }
        corNow[0]++;
    }

    public static void putRobotToLeft()
    {
        if (corNow[0] == 0) {
            addItemToLeft(new LabItem("2"));
        }
        corNow[0]--;
    }

    public static boolean isItemAtForward()
    {
        if(corNow[1] == analyzeField.get(corNow[0]).size() - 1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isItemAtRight()
    {
        if(corNow[0] == analyzeField.size() - 1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isItemAtLeft()
    {
        if(corNow[0] == 0)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isItemAtBack()
    {
        if(corNow[1] == 0)
        {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Методы {@code addItemToBack}, {@code addItemToRight},
     * {@code addItemToForward}, {@code addItemToLeft} добавляет {@link LabItem} в {@code analyzeField}
     *
     * @see ru.li24robotics.ev3.robolab.lab.LabItem
     */

    public static void addItemToRight(LabItem item)
    {
        if (corNow[0] == analyzeField.size() - 1)
        {
            analyzeField.add(corNow[0] + 1, new ArrayList<LabItem>());
            for (int i = 0; i < analyzeField.get(corNow[0]).size(); i++)
            {
                if (i == corNow[1])
                {
                    analyzeField.get(corNow[0] + 1).add(i, item);
                }
                else {
                    analyzeField.get(corNow[0] + 1).add(i, null);
                }
            }
        }
        else {
            LabItem _now = labItemSum(item, analyzeField.get(corNow[0] + 1).get(corNow[1]));
            analyzeField.get(corNow[0] + 1).set(corNow[1], _now);
        }
    }

    public static void addItemToLeft(LabItem item)
    {
        if (corNow[0] == 0) {
            analyzeField.add(0, new ArrayList<LabItem>());
            corNow[0]++;
            for (int i = 0; i < analyzeField.get(corNow[0]).size(); i++) {
                if (i == corNow[1]) {
                    analyzeField.get(0).add(i, item);
                } else {
                    analyzeField.get(0).add(i, null);
                }
            }
        }
        else {
            LabItem _now = labItemSum(item, analyzeField.get(corNow[0] - 1).get(corNow[1]));
            analyzeField.get(corNow[0] - 1).set(corNow[1], _now);
        }
    }

    public static void addItemToForward(LabItem item)
    {
        if (corNow[1] == analyzeField.get(corNow[0]).size() - 1) {
        	System.out.println("OK");
            for (int i = 0; i < analyzeField.size(); i++) {
                if (i == corNow[0]) {
                	System.out.println("OK2");
                    analyzeField.get(i).add(corNow[1] + 1, item);
                } else {
                    analyzeField.get(i).add(corNow[1] + 1, null);
                }
            }
        } else {
            LabItem _now = labItemSum(item, analyzeField.get(corNow[0]).get(corNow[1] + 1));
            analyzeField.get(corNow[0]).set(corNow[1] + 1, _now);
        }

    }

    public static void addItemToBack(LabItem item)
    {
        if (corNow[1] == 0) {
            for (int i = 0; i < analyzeField.size(); i++) {
                if (corNow[0] == i) {
                    analyzeField.get(i).add(0, item);
                } else {
                    analyzeField.get(i).add(0, null);
                }
            }
            corNow[1]++;
        } else {
            LabItem _now = labItemSum(item, analyzeField.get(corNow[0]).get(corNow[1] - 1));
            analyzeField.get(corNow[0]).set(corNow[1] - 1, _now);
        }
    }

    public void setNowItem(LabItem item)
    {
    	analyzeField.get(corNow[0]).set(corNow[1], labItemSum(item, analyzeField.get(corNow[0]).get(corNow[1])));
    }
    
    private static LabItem labItemSum(LabItem item1, LabItem item2)
    {
        LabItem sum = new LabItem("10");
        if(!item1.toForward.isNothingAboutWallHere())
        {
            sum.toForward.setWallIsHere(item1.toForward.isWallIsHere());
        }
        else if (!item2.toForward.isNothingAboutWallHere())
        {
            sum.toForward.setWallIsHere(item2.toForward.isWallIsHere());
        }
        else {
            sum.toForward.setNothingAboutWallHere(true);
        }

        if(!item1.toRight.isNothingAboutWallHere())
        {
            sum.toRight.setWallIsHere(item1.toRight.isWallIsHere());
        }
        else if (!item2.toRight.isNothingAboutWallHere())
        {
            sum.toRight.setWallIsHere(item2.toRight.isWallIsHere());
        }
        else {
            sum.toRight.setNothingAboutWallHere(true);
        }

        if(!item1.toLeft.isNothingAboutWallHere())
        {
            sum.toLeft.setWallIsHere(item1.toLeft.isWallIsHere());
        }
        else if (!item2.toLeft.isNothingAboutWallHere())
        {
            sum.toLeft.setWallIsHere(item2.toLeft.isWallIsHere());
        }
        else {
            sum.toLeft.setNothingAboutWallHere(true);
        }

        if(!item1.toBack.isNothingAboutWallHere())
        {
            sum.toBack.setWallIsHere(item1.toBack.isWallIsHere());
        }
        else if (!item2.toBack.isNothingAboutWallHere())
        {
            sum.toBack.setWallIsHere(item2.toBack.isWallIsHere());
        }
        else {
            sum.toBack.setNothingAboutWallHere(true);
        }

        return sum;
    }

    /**
     * Метод {@code getRobotCoordinatesOnMainLab} принимает на вход полный либаринт
     * и возвращает массив возможных координат нахождений робота
     *
     * @return массив координат, где модет находиться робот
     */
    public static ArrayList<int[]> getRobotCoordinatesOnMainLab(ArrayList<ArrayList<LabItem>> field) {
        ArrayList<int[]> potentialCorOnMainField = new ArrayList<int[]>();
        potentialCorOnMainField.addAll(getRobotCoordinatesNotRotatedAnalyzer(field));
        potentialCorOnMainField.addAll(getRobotCoordinatesRightRotatedAnalyzer((field)));
        potentialCorOnMainField.addAll(getRobotCoordinatesOverRotatedAnalyzer(field));
        potentialCorOnMainField.addAll(getRobotCoordinatesLeftRotatedAnalyzer(field));

        for (int i = 0; i < potentialCorOnMainField.size(); i++) {
            for (int j = 0; j < potentialCorOnMainField.size(); j++) {
                if (potentialCorOnMainField.get(i)[0] == potentialCorOnMainField.get(j)[0] && potentialCorOnMainField.get(i)[1] == potentialCorOnMainField.get(j)[1] && i != j) {
                    potentialCorOnMainField.remove(j);
                }
            }
        }

        return potentialCorOnMainField;
    }

    /**
     * Методы {@code getRobotCoordinatesNotRotatedAnalyzer}, {@code getRobotCoordinatesLeftRotatedAnalyzer},
     * {@code getRobotCoordinatesRightRotatedAnalyzer}, {@code getRobotCoordinatesOverRotatedAnalyzer}
     * ищут возможные кординаты робота главном лабиринте, учитывают возможные положения робота
     * в главном лабиринте и вызывают методы{@code equalsNotRotate},
     * {@code equalsRightRotate}, {@code equalsOverRotated}, {@code equalsLeftRotated} которые сравнивают
     * просканированный лабиринт с главным в различный направлениях соответственно
     */
    private static ArrayList<int[]> getRobotCoordinatesNotRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field) {
        ArrayList<int[]> potentialCor = new ArrayList<int[]>();
        for (int j = 0; j <= field.get(0).size() - analyzeField.get(0).size(); j++) {
            for (int i = 0; i <= field.size() - analyzeField.size(); i++) {
                if (equalsPartsOfFieldsNotRotated(field, i, j)) {
                    potentialCor.add(new int[]{i + corNow[0], j + corNow[1], 0});
                }
            }
        }
        return potentialCor;
    }

    private static boolean equalsPartsOfFieldsNotRotated(ArrayList<ArrayList<LabItem>> field, int x, int y) {
        for (int i = 0; i < analyzeField.size(); i++) {
            for (int j = 0; j < analyzeField.get(i).size(); j++) {
                if (!analyzeField.get(i).get(j).equalsNotRotate(field.get(i + x).get(j + y))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static ArrayList<int[]> getRobotCoordinatesRightRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field) {
        ArrayList<int[]> potentialCor = new ArrayList<int[]>();
        for (int j = 0; j <= field.get(0).size() - analyzeField.size(); j++) {
            for (int i = 0; i <= field.size() - analyzeField.get(0).size(); i++) {
                if (equalsPartsOfFieldsRightRotated(field, i, j)) {
                    potentialCor.add(new int[]{i + corNow[1], j + (analyzeField.size() - corNow[0] - 1), 90});
                }
            }
        }
        return potentialCor;
    }

    private static boolean equalsPartsOfFieldsRightRotated(ArrayList<ArrayList<LabItem>> field, int x, int y) {
        for (int i = 0; i < analyzeField.size(); i++) {
            for (int j = 0; j < analyzeField.get(0).size(); j++) {
                if (!analyzeField.get(i).get(j).equalsRightRotate(field.get(x + j).get(y + (analyzeField.size() - i - 1)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static ArrayList<int[]> getRobotCoordinatesOverRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field) {
        ArrayList<int[]> potentialCor = new ArrayList<int[]>();
        for (int j = 0; j <= field.get(0).size() - analyzeField.get(0).size(); j++) {
            for (int i = 0; i <= field.size() - analyzeField.size(); i++) {
                if (equalsPartsOfFieldsOverRotated(field, i, j)) {
                    potentialCor.add(new int[]{i + (analyzeField.size() - corNow[0] - 1), j + (analyzeField.get(0).size() - corNow[1] - 1), 180});
                }
            }
        }
        return potentialCor;
    }

    private static boolean equalsPartsOfFieldsOverRotated(ArrayList<ArrayList<LabItem>> field, int x, int y) {
        for (int i = 0; i < analyzeField.size(); i++) {
            for (int j = 0; j < analyzeField.get(0).size(); j++) {
                if (!analyzeField.get(i).get(j).equalsOverRotated(field.get(x + (analyzeField.size() - i - 1)).get(y + (analyzeField.get(0).size() - j - 1)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static ArrayList<int[]> getRobotCoordinatesLeftRotatedAnalyzer(ArrayList<ArrayList<LabItem>> field) {
        ArrayList<int[]> potentialCor = new ArrayList<int[]>();

        for (int j = 0; j <= field.get(0).size() - analyzeField.size(); j++) {
            for (int i = 0; i <= field.size() - analyzeField.get(0).size(); i++) {
                if (equalsPartsOfFieldsLeftRotated(field, i, j)) {
                    potentialCor.add(new int[]{i + (analyzeField.get(0).size() - corNow[1] - 1), j + corNow[0], 270});
                }
            }
        }

        return potentialCor;
    }

    private static boolean equalsPartsOfFieldsLeftRotated(ArrayList<ArrayList<LabItem>> field, int x, int y) {
        for (int i = 0; i < analyzeField.size(); i++) {
            for (int j = 0; j < analyzeField.get(0).size(); j++) {
                if (!analyzeField.get(i).get(j).equalsLeftRotated(field.get(x + analyzeField.get(0).size() - j - 1).get(y + i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Метод {@code outField} выводит просканируемый лабиринт
     */
    
   public static PrintWriter out;
    static {
    	try{
    		out = new PrintWriter(new File("lab.txt"));
    	}
    	catch (Exception e){
    	
    	}
    }

    public static void outField() {
    	 
        for (int j = analyzeField.get(0).size() - 1; j >= 0; j--) {
            for (int i = 0; i < analyzeField.size(); i++) {
                if (analyzeField.get(i).get(j) != null) {
                    out.print(analyzeField.get(i).get(j).toString() + "[" + i + "][" + j + "]");
                } else {
                    out.print("n" + "[" + i + "][" + j + "]");
                }
                if (corNow[0] == i && corNow[1] == j) {
                    out.print("!");
                }
                out.print(" ");
            }
            out.println();
        }
        out.println("/////////////////////////////////////////////////////////////");
    }

    /**
     * Getters and Setters просканированного поля и координат в нем робота
     */
    public static ArrayList<ArrayList<LabItem>> getAnalyzeField() {
        return analyzeField;
    }

    public static int[] getCorNow() {
        return corNow;
    }
}
