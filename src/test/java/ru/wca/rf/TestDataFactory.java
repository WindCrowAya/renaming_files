package ru.wca.rf;

import java.io.File;
import java.io.IOException;
import java.util.List;

class TestDataFactory {

    static List<File> filesWithNumbering() {
        return List.of(
                new File("D:\\ProjectData\\Test_rf\\test\\4 - aya.txt"),
                new File("D:\\ProjectData\\Test_rf\\test\\2.s.png"),
                new File("D:\\ProjectData\\Test_rf\\test\\3. txt.txt"),
                new File("D:\\ProjectData\\Test_rf\\test\\aaa.txt"),
                new File("D:\\ProjectData\\Test_rf\\test\\1 aaa.png")
        );
    }

    static void createTestFiles() {
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

    static void createExpectedFilesForCmdRenameAll() {
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

    static void createExpectedFilesForCmdAll() {
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

    static void createExpectedFilesForCmdRename() {
        new File("D:\\ProjectData\\Test_rf\\expected\\1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\2").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\3").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\4").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\5").mkdir();
        List<File> files = List.of(
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG000.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG001.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\1_IMG002.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\2 - IMG003.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\3 IMG004.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\4-IMG005.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\5. IMG006.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\6.IMG007.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG008.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG009.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\1.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\2.mp3")
        );
        createFiles(files);
    }

    static void createExpectedFilesForCmdAdd() {
        new File("D:\\ProjectData\\Test_rf\\expected\\1 123").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\2 2 folder").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\3 3-folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\4 456 folder2").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\5 folder1").mkdir();
        List<File> files = List.of(
                new File("D:\\ProjectData\\Test_rf\\expected\\01 1_IMG002.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\02 2 - IMG003.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\03 3 IMG004.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\04 4-IMG005.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\05 5. IMG006.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\06 6.IMG007.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\07 IMG000.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\08 IMG001.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\09 IMG008.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\10 IMG009.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\004 - music.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\005 - new music.mp3")
        );
        createFiles(files);
    }

    static void createExpectedFilesForCmdDeleteNum() {
        new File("D:\\ProjectData\\Test_rf\\expected\\3-folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\123").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\folder").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\folder2").mkdir();
        List<File> files = List.of(
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG000.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG001.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG002.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG003.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG004.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG005.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG006.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG007.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG008.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG009.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\004 - music.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\005 - new music.mp3")
        );
        createFiles(files);
    }

    static void createExpectedFilesForCmdDeleteAll() {
        new File("D:\\ProjectData\\Test_rf\\expected\\3-folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\123").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\folder").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\folder1").mkdir();
        new File("D:\\ProjectData\\Test_rf\\expected\\folder2").mkdir();
        List<File> files = List.of(
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG000.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG001.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG002.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG003.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG004.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG005.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG006.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG007.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG008.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\IMG009.jpg"),
                new File("D:\\ProjectData\\Test_rf\\expected\\music.mp3"),
                new File("D:\\ProjectData\\Test_rf\\expected\\new music.mp3")
        );
        createFiles(files);
    }

    static void createFiles(List<File> files) {
        try {
            for (File file : files) {
                file.createNewFile();
            }
        } catch (IOException ignored) {}
    }
}
