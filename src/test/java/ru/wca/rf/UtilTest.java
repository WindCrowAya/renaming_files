package ru.wca.rf;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    public void renameByDeletingNumber() {
        File file1 = new File("D:\\Тест\\Inner\\4 - aya.txt");
        File file2 = new File("D:\\Тест\\Inner\\2.s.png");
        File file3 = new File("D:\\Тест\\Inner\\3. txt.txt");
        File file4 = new File("D:\\Тест\\Inner\\aaa.txt");
        File file5 = new File("D:\\Тест\\Inner\\1 aaa.png");

        assertEquals(Paths.get("D:\\Тест\\Inner\\aya.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(file1.getParent()).toFile(), file1.getName(),3));
        assertEquals(Paths.get("D:\\Тест\\Inner\\s.png"  ).toFile(),
                Util.renameByDeletingNumber(Paths.get(file2.getParent()).toFile(), file2.getName(),1));
        assertEquals(Paths.get("D:\\Тест\\Inner\\txt.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(file3.getParent()).toFile(), file3.getName(),2));
        assertEquals(Paths.get("D:\\Тест\\Inner\\aaa.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(file4.getParent()).toFile(), file4.getName(),-1));  //такой случай по условиям не произойдет
        assertEquals(Paths.get("D:\\Тест\\Inner\\aaa.png").toFile(),
                Util.renameByDeletingNumber(Paths.get(file5.getParent()).toFile(), file5.getName(),1));
    }

    @Test
    public void countOfPointMoreThanOne() {
        assertTrue(Util.countOfPointMoreThanOne("01. Reimu.jpg"));
        assertTrue(Util.countOfPointMoreThanOne("..jpg"));
        assertFalse(Util.countOfPointMoreThanOne("01 Reimu.jpg"));
    }

    @Test
    public void checkForSeparators() {
        assertTrue(Util.checkForSeparatorsInFile("01 - Reimu.png", 2));
        assertTrue(Util.checkForSeparatorsInFile("01 Reimu.png",   2));
        assertTrue(Util.checkForSeparatorsInFile("01-Reimu.png",   2));
        assertTrue(Util.checkForSeparatorsInFile("01. Reimu.png",  2));
        assertTrue(Util.checkForSeparatorsInFile("01.Reimu.png",   2));
    }

    @Test
    public void getLengthOfSeparator() {
        File file1 = new File("D:\\Тест\\Inner\\4 - aya.txt");
        File file2 = new File("D:\\Тест\\Inner\\2.s.png");
        File file3 = new File("D:\\Тест\\Inner\\3. txt.txt");
        File file4 = new File("D:\\Тест\\Inner\\aaa.txt");
        File file5 = new File("D:\\Тест\\Inner\\1 aaa.png");

        assertEquals(3, Util.getLengthOfSeparator(file1.getName(), 1));
        assertEquals(1, Util.getLengthOfSeparator(file2.getName(), 1));
        assertEquals(2, Util.getLengthOfSeparator(file3.getName(), 1));
        assertEquals(0, Util.getLengthOfSeparator(file4.getName(), 0));
        assertEquals(1, Util.getLengthOfSeparator(file5.getName(), 1));
    }
}