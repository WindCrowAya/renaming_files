package ru.wca.rf;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CmdTest {

    private File[] filesForTest,
                   filesForExpected;

    @AfterEach
    void rollback() {
        deleteFiles(filesForTest);
        deleteFiles(filesForExpected);
    }

    private void deleteFiles(File[] files) {
        if (nonNull(files) && files.length != 0) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Test
    void executeCommandRenameAll() {
        //given
        createTestFiles();
        File folderTest = folder("test");
        File folderExpected = folder("expected");
        //when
        RenamingService.executeCommandRenameAll(folderTest.listFiles(), folderTest, 0);
        //then
        createExpectedFilesForCmdRenameAll();
        filesForTest = folderTest.listFiles();
        filesForExpected = folderExpected.listFiles();
        assertArrayEquals(filenames(filesForTest), filenames(filesForExpected));
    }

    @Test
    void executeCommandAll() {
        //given
        createTestFiles();
        File folderTest = folder("test");
        File folderExpected = folder("expected");
        //when
        RenamingService.executeCommandAll(folderTest.listFiles(), folderTest, 1, 0);
        //then
        createExpectedFilesForCmdAll();
        filesForTest = folderTest.listFiles();
        filesForExpected = folderExpected.listFiles();
        assertArrayEquals(filenames(filesForTest), filenames(filesForExpected));
    }

    private File folder(String folderName) {
        return new File("D:\\ProjectData\\Test_rf\\" + folderName);
    }

    private String[] filenames(File[] files) {
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }
        Arrays.sort(filenames);
        return filenames;
    }

    private void createTestFiles() {
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
        createFiles(files);
    }

    private void createExpectedFilesForCmdRenameAll() {
        new File("D:\\ProjectData\\Test_rf\\expected\\1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\2").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\3").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\4").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\5").mkdir();
        List<File> files = List.of(
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
        createFiles(files);
    }

    private void createExpectedFilesForCmdAll() {
        new File("D:\\ProjectData\\Test_rf\\expected\\1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\2").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\3").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\4").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\5").mkdir();
        List<File> files = List.of(
                new File("D:\\ProjectData\\Test_rf\\expected\\01.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\02.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\03.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\04.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\05.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\06.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\07.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\08.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\09.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\10.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\11.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\12.jpg")
        );
        createFiles(files);
    }

    private void createFiles(List<File> files) {
        try {
            for (File file : files) {
                file.createNewFile();
            }
        } catch (IOException ignored) {}
    }
}
