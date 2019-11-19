package ru.wca.rf;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void renameByDeletingNumber() {
        File file1 = files().get(0);
        File file2 = files().get(1);
        File file3 = files().get(2);
        File file4 = files().get(3);
        File file5 = files().get(4);

        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\aya.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(file1.getParent()).toFile(), file1.getName(),3));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\s.png"  ).toFile(),
                Util.renameByDeletingNumber(Paths.get(file2.getParent()).toFile(), file2.getName(),1));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\txt.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(file3.getParent()).toFile(), file3.getName(),2));
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\aaa.txt").toFile(),
                Util.renameByDeletingNumber(Paths.get(file4.getParent()).toFile(), file4.getName(),-1));  //такой случай по условиям не произойдет
        assertEquals(Paths.get("D:\\ProjectData\\Test_rf\\test\\aaa.png").toFile(),
                Util.renameByDeletingNumber(Paths.get(file5.getParent()).toFile(), file5.getName(),1));
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
        assertEquals(3, Util.lengthOfSeparator(files().get(0).getName(), 1));
        assertEquals(1, Util.lengthOfSeparator(files().get(1).getName(), 1));
        assertEquals(2, Util.lengthOfSeparator(files().get(2).getName(), 1));
        assertEquals(0, Util.lengthOfSeparator(files().get(3).getName(), 0));
        assertEquals(1, Util.lengthOfSeparator(files().get(4).getName(), 1));
    }

    private List<File> files() {
        return List.of(
                new File("D:\\ProjectData\\Test_rf\\test\\4 - aya.txt"),
                new File("D:\\ProjectData\\Test_rf\\test\\2.s.png"),
                new File("D:\\ProjectData\\Test_rf\\test\\3. txt.txt"),
                new File("D:\\ProjectData\\Test_rf\\test\\aaa.txt"),
                new File("D:\\ProjectData\\Test_rf\\test\\1 aaa.png")
        );
    }
}
