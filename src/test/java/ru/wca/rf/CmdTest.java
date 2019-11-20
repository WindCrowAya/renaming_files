package ru.wca.rf;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static ru.wca.rf.Constants.FOLDERS;
import static ru.wca.rf.TestDataFactory.*;

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

    @Test
    void executeCommandRename() {
        //given
        createTestFiles();
        File folderTest = folder("test");
        File folderExpected = folder("expected");
        //when
        RenamingService.executeCommandRename(folderTest.listFiles(), folderTest, 0,
                new String[] {"mp3","folders"});
        //then
        createExpectedFilesForCmdRename();
        filesForTest = folderTest.listFiles();
        filesForExpected = folderExpected.listFiles();
        assertArrayEquals(filenames(filesForTest), filenames(filesForExpected));
    }

    @Test
    void executeCommandAdd() {
        //given
        createTestFiles();
        File folderTest = folder("test");
        File folderExpected = folder("expected");
        //when
        RenamingService.executeCommandAdd(folderTest.listFiles(), folderTest, 0,
                new String[] {"folders","jpg"});
        //then
        createExpectedFilesForCmdAdd();
        filesForTest = folderTest.listFiles();
        filesForExpected = folderExpected.listFiles();
        assertArrayEquals(filenames(filesForTest), filenames(filesForExpected));
    }

    @Test
    void executeCommandDeleteNum() {
        //given
        createTestFiles();
        File folderTest = folder("test");
        File folderExpected = folder("expected");
        String[] extensions = new String[] {"jpg","folders"};
        //when
        executeCommandDeleteNum(folderTest, extensions);
        //then
        createExpectedFilesForCmdDeleteNum();
        filesForTest = folderTest.listFiles();
        filesForExpected = folderExpected.listFiles();
        assertArrayEquals(filenames(filesForTest), filenames(filesForExpected));
    }

    @Test
    void executeCommandDeleteAll() {
        //given
        createTestFiles();
        File folderTest = folder("test");
        File folderExpected = folder("expected");
        //when
        RenamingService.executeCommandDel(true, true, folderTest.listFiles(), null, folderTest);
        //then
        createExpectedFilesForCmdDeleteAll();
        filesForTest = folderTest.listFiles();
        filesForExpected = folderExpected.listFiles();
        assertArrayEquals(filenames(filesForTest), filenames(filesForExpected));
    }

    private File folder(String folderName) {
        return new File("D:\\ProjectData\\Test_rf\\" + folderName);
    }

    private String[] filenames(File[] files) {  // TODO: 20.11.2019 Попробовать реализовать через стримы
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }
        Arrays.sort(filenames);
        return filenames;
    }

    private void executeCommandDeleteNum(File folder, String[] extensions) {
        for (String ex : extensions) {
            File[] files = folder.listFiles();
            if (!FOLDERS.equals(ex)) {
                RenamingService.executeCommandDel(false, true, files, ex, folder);
            } else {
                RenamingService.executeCommandDel(true, false, files, ex, folder);
            }
        }
    }
}
