package ru.wca.rf;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    private File[] filesForTest, filesForExpected;

    @AfterEach
    public void rollback() {
        if (nonNull(filesForTest) && filesForTest.length != 0) {
            for (File file : filesForTest) {
                file.delete();
            }
        }
        if (nonNull(filesForExpected) && filesForExpected.length != 0) {
            for (File file : filesForExpected) {
                file.delete();
            }
        }
    }

    @Test
    public void renameByDeletingNumber() {
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
        assertFalse(Util.checkForSeparatorsInFile("01Reimu.png",   2));
    }

    @Test
    public void lengthOfSeparator() {
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

    @Test
    public void executeCommandRenameAll() {
        //given
        createTestFiles();
        File folder = new File("D:\\ProjectData\\Test_rf\\test");
        File folderForExpected = new File("D:\\ProjectData\\Test_rf\\expected");
        //when
        Main.executeCommandRenameAll(folder.listFiles(), folder, 0);
        //then
        new File("D:\\ProjectData\\Test_rf\\expected\\1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\2").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\3").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\4").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\5").mkdir();
        List<File> expectedFilesList = List.of(
                new File("D:\\ProjectData\\Test_rf\\expected\\01.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\02.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\03.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\04.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\05.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\06.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\07.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\08.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\09.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\10.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\1.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\2.mp3")
        );
        File[] expectedFiles = expectedFilesList.toArray(new File[expectedFilesList.size()]);
        for (File file : expectedFiles) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filesForTest = folder.listFiles();
        filesForExpected = folderForExpected.listFiles();
        String[] filenames = new String[filesForTest.length];
        for (int i = 0; i < filesForTest.length; i++) {
            filenames[i] = filesForTest[i].getName();
        }
        String[] filenamesExpected = new String[requireNonNull(filesForExpected).length];
        for (int i = 0; i < filesForExpected.length; i++) {
            filenamesExpected[i] = filesForExpected[i].getName();
        }
        Arrays.sort(filenames);
        Arrays.sort(filenamesExpected);
        assertArrayEquals(filenames, filenamesExpected);
    }

    private List<File> createTestFiles() {
        new File("D:\\ProjectData\\Test_rf\\test\\folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\test\\2 folder").mkdir();
        new File("D:\\ProjectData\\Test_rf\\test\\3-folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\test\\123").mkdir();
        new File("D:\\ProjectData\\Test_rf\\test\\456 folder2").mkdir();
        List<File> files = List.of(
                new File("D:\\ProjectData\\Test_rf\\test\\IMG000.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\IMG001.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\1_IMG002.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\2 - IMG003.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\3 IMG004.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\4-IMG005.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\5. IMG006.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\6.IMG007.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\IMG008.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\IMG009.jpg"),
                new File("D:\\ProjectData\\Test_rf\\test\\004 - music.mp3"),
                new File("D:\\ProjectData\\Test_rf\\test\\005 - new music.mp3")
        );

        for (File file : files) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return files;
    }
}
