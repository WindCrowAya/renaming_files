package ru.wca.rf;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static ru.wca.rf.TestDataFactory.filesWithNumbering;

class UtilTest {

    @Test
    void renameByDeletingNumber() {
        File[] files = filesWithNumbering();

        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\aya.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(files[0].getParent()).toFile(), files[0].getName(),3));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\s.png"  ).toFile(),
                Util.renameByDeletingNumber(Paths.get(files[1].getParent()).toFile(), files[1].getName(),1));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\txt.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(files[2].getParent()).toFile(), files[2].getName(),2));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\aaa.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(files[3].getParent()).toFile(), files[3].getName(),-1));  //такой случай по условиям не произойдет
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\aaa.png").toFile(),
                Util.renameByDeletingNumber(Paths.get(files[4].getParent()).toFile(), files[4].getName(),1));
    }

    @Test
    void renameByAddingNumber() {
        File[] files = filesWithNumbering();
        Arrays.sort(files);

        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\1 1 aaa.png").toFile(),
                Util.renameByAddingNumber(Paths.get(files[0].getParent()).toFile(),
                        0, 1, files[0].getName()));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\2 2.s.png").toFile(),
                Util.renameByAddingNumber(Paths.get(files[1].getParent()).toFile(),
                        0,2, files[1].getName()));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\3 3. txt.txt").toFile(),
                Util.renameByAddingNumber(Paths.get(files[2].getParent()).toFile(),
                        0,3, files[2].getName()));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\4 4 - aya.txt").toFile(),
                Util.renameByAddingNumber(Paths.get(files[3].getParent()).toFile(),
                        0,4, files[3].getName()));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\5 aaa.txt").toFile(),
                Util.renameByAddingNumber(Paths.get(files[4].getParent()).toFile(),
                        0,5, files[4].getName()));
    }

    @Test
    void renameByAddingNumber_moreTenFiles() {
        File[] files = filesWithNumbering();
        Arrays.sort(files);

        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\01 1 aaa.png").toFile(),
                Util.renameByAddingNumber(Paths.get(files[0].getParent()).toFile(),
                        1, 1, files[0].getName()));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\02 2.s.png").toFile(),
                Util.renameByAddingNumber(Paths.get(files[1].getParent()).toFile(),
                        1,2, files[1].getName()));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\10 3. txt.txt").toFile(),
                Util.renameByAddingNumber(Paths.get(files[2].getParent()).toFile(),
                        0,10, files[2].getName()));
    }

    @Test
    void countOfPointMoreThanOne() {
        assertTrue(Util.countOfPointMoreThanOne("01. Reimu.jpg"));
        assertTrue(Util.countOfPointMoreThanOne("..jpg"));
        assertFalse(Util.countOfPointMoreThanOne("01 Reimu.jpg"));
    }

    @Test
    void checkForSeparators() {
        assertTrue(Util.checkForSeparatorsInFile("01 - Reimu.png", 2));
        assertTrue(Util.checkForSeparatorsInFile("01 Reimu.png",   2));
        assertTrue(Util.checkForSeparatorsInFile("01-Reimu.png",   2));
        assertTrue(Util.checkForSeparatorsInFile("01. Reimu.png",  2));
        assertTrue(Util.checkForSeparatorsInFile("01.Reimu.png",   2));
        assertFalse(Util.checkForSeparatorsInFile("01Reimu.png",   2));
    }

    @Test
    void lengthOfSeparator() {
        File[] files = filesWithNumbering();

        assertEquals(3, Util.lengthOfSeparator(files[0].getName(), 1));
        assertEquals(1, Util.lengthOfSeparator(files[1].getName(), 1));
        assertEquals(2, Util.lengthOfSeparator(files[2].getName(), 1));
        assertEquals(0, Util.lengthOfSeparator(files[3].getName(), 0));
        assertEquals(1, Util.lengthOfSeparator(files[4].getName(), 1));
    }
}
