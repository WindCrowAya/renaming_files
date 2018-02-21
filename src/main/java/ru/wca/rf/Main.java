package ru.wca.rf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Renaming files in specified folder.
 * Use the console to work with this program.
 *
 * Useful links:
 *   Arrays.copyOf() : https://stackoverflow.com/questions/1018750/how-to-convert-object-array-to-string-array-in-java
 *   https://ru.stackoverflow.com/questions/679318/%D0%92%D0%BC%D0%B5%D1%81%D1%82%D0%BE-%D0%B2%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%BD%D1%8B%D1%85-%D1%81%D0%BB%D0%BE%D0%B2-%D0%BE%D1%82%D0%BE%D0%B1%D1%80%D0%B0%D0%B6%D0%B0%D1%8E%D1%82%D1%81%D1%8F-%D1%81%D0%B8%D0%BC%D0%B2%D0%BE%D0%BB%D1%8B/679343#679343
 *
 * View https://github.com/WindCrowAya/sample to see the history of the project change
 */
public class Main {

    private static void renameFiles() throws IOException {
        //задаем свойство для чтения кириллицы в консоли, если она присутствует в директории
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
                System.getProperty("console.encoding", "cp866")));

        boolean pathIsEmpty,
                stringOfExtensionsIsEmpty = true,
                listFilesIsEmpty = true,
                defIsEnabled = false,
                allIsEnabled  = false,
                delPlusIsEnabled = false;

        File folder = null;

        File[] listFiles = new File[0];

        String path,
               command = "",
               stringOfExtensions;

        String[] extensions = new String[0];

        int numberOfFiles,
            numberOfFolders = 0,
            numberOfZerosToFolders,
            numberOfZerosToFiles;

        System.out.print(
                "\n" +
                "+-------------------------------+\n" +
                "| Переименование файлов в папке |\n" +
                "+-------------------------------+\n\n" +
                "Как работать с данной программой:\n" +
                "1. Прописывайте путь к папке.\n" +
                "2. Воспользуйтесь специальной командой, введя его id:\n" +
                "-> 1 :        (all) переименование всех файлов подряд и отдельно папок\n" +
                "-> 2 :     (rename) переименование по каждому введенному расширению\n" +
                "-> 3 :    (add num) добавляет к названиям файлов порядковый номер\n" +
                "-> 4 : (delete num) удаляет нумерацию файлов по введенным расширениям\n" +
                "-> 5 : (delete all) удаляет нумерацию каждого файла\n" +
                "-> 6 : (rename all) преобразование по умолчанию, переименование всех файлов по расширениям\n" +
                "                   (вместо ввода этой команды достаточно нажать Enter, оставив поле ввода пустым)\n" +
                "3. Прописывайте расширения через запятую, для переименования папок введите \"folders\"\n\n" +
                "Введите путь: ");
        do {
            path = reader.readLine().trim();

            pathIsEmpty = Util.isEmpty(path);
            if (pathIsEmpty) {
                System.out.print("Пустой запрос. Повторите ввод заново, начиная с пути: ");
                continue;
            }

            folder = new File(path);
            listFiles = folder.listFiles();

            listFilesIsEmpty = (listFiles == null) || listFiles.length == 0;
            if (listFilesIsEmpty) {
                System.out.print("Файлов в папке нет или такого пути не существует. Повторите ввод заново, начиная с пути: ");
                continue;
            }


            System.out.print("Введите команду: ");
            command = reader.readLine().trim();

            if ("2".equals(command) ||
                "3".equals(command) ||
                "4".equals(command)) {
                //do nothing, because command is correct
            } else if (Util.isEmpty(command) || "6".equals(command)) {
                defIsEnabled = true;
            } else if ("1".equals(command)) {
                allIsEnabled = true;
            } else if ("5".equals(command)) {
                delPlusIsEnabled = true;
            } else {
                System.out.print("Некорректная команда. Повторите ввод заново, начиная с пути: ");
                continue;
            }


            if (!allIsEnabled && !defIsEnabled && !delPlusIsEnabled) {
                System.out.print("Введите расширения: ");
                stringOfExtensions = reader.readLine().trim();

                stringOfExtensionsIsEmpty = Util.isEmpty(stringOfExtensions);
                if (stringOfExtensionsIsEmpty) {
                    System.out.print("Пустой запрос. Повторите ввод заново, начиная с пути: ");
                    continue;
                }

                extensions = stringOfExtensions.split(",");
                for (int i = 0; i < extensions.length; i++) {
                    extensions[i] = extensions[i].toLowerCase().trim();
                }
                extensions = Util.removeDuplicates(extensions);

                if (extensions.length < 1) {
                    System.out.print("Не введены расширения. Повторите ввод заново, начиная с пути: ");
                }
            } else {
                stringOfExtensionsIsEmpty = false;
            }

            } while (pathIsEmpty || listFilesIsEmpty || stringOfExtensionsIsEmpty);


        for (File file : listFiles) {
            if (file.isDirectory()) {
                numberOfFolders++;
            }
        }
        numberOfFiles = listFiles.length - numberOfFolders;
        numberOfZerosToFolders = String.valueOf(numberOfFolders).length() - 1;
        numberOfZerosToFiles = String.valueOf(numberOfFiles).length() - 1;


        switch (command) {
            case "1":
                executeCommandAll(listFiles, folder, numberOfZerosToFiles, numberOfZerosToFolders);
                break;

            case "2":
                executeCommandRename(listFiles, folder, numberOfZerosToFolders, extensions);
                break;

            case "3":
                executeCommandAdd(listFiles, folder, numberOfZerosToFolders, extensions);
                break;

            case "4":
                for (String ex : extensions) {
                    listFiles = folder.listFiles();
                    if (!"folders".equals(ex)) {
                        executeCommandDel(false, true, listFiles, ex, folder);
                    } else {
                        executeCommandDel(true, false, listFiles, ex, folder);
                    }
                }
                break;

            case "5":
                executeCommandDel(true, true, listFiles, null, folder);
                break;

            default:
                executeCommandRenameAll(listFiles, folder, numberOfZerosToFolders);
                break;
            }

            System.out.println("Переименование завершено.");
        }

    static void executeCommandAll(File[] listFiles, File folder, int numberOfZerosToFiles, int numberOfZerosToFolders) {
        String fileToString,
               currentExtension;

        int countFilesWithAnyExtension = 0,
            numberOfZerosToFilesTemp = numberOfZerosToFiles,      //Codacy: изменение входного параметра нежелательно; введена temp-переменная (1)
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,
            countDirectories = 0;

        for (File file : listFiles) {
            if (!file.isDirectory()) {
                fileToString = file.toString();
                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                file.renameTo(Util.renameToNumbersFiles(
                        folder,
                        numberOfZerosToFilesTemp = Util.changeNumberOfZeros(
                                numberOfZerosToFilesTemp,
                                countFilesWithAnyExtension),
                        ++countFilesWithAnyExtension,
                        currentExtension));
            } else {
                file.renameTo(Util.renameToNumbersFolders(
                        folder,
                        numberOfZerosToFoldersTemp = Util.changeNumberOfZeros(
                                numberOfZerosToFoldersTemp,
                                countDirectories),
                        ++countDirectories));
            }
        }
    }

    static void executeCommandRename(File[] listFiles, File folder, int numberOfZerosToFolders, String[] extensions) {
        String fileToString,
                currentExtension;

        int countFilesWithCurrentExtension,
                numberOfZerosToFilesWithCurrentEx,
                numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //(1)
                countDirectories = 0;

        Map<String, Integer> extensionsInDir = Util.putExtensions(extensions, listFiles);

        for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
            numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
            if (!ex.getKey().equals("folders")) {
                countFilesWithCurrentExtension = 0;
                for (File file : listFiles) {
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                        if (currentExtension.equals(ex.getKey())) {
                            file.renameTo(Util.renameToNumbersFiles(
                                    folder,
                                    numberOfZerosToFilesWithCurrentEx = Util.changeNumberOfZeros(
                                            numberOfZerosToFilesWithCurrentEx,
                                            countFilesWithCurrentExtension),
                                    ++countFilesWithCurrentExtension,
                                    currentExtension));
                        }
                    }
                }
            } else {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        file.renameTo(Util.renameToNumbersFolders(
                                folder,
                                numberOfZerosToFoldersTemp = Util.changeNumberOfZeros(
                                        numberOfZerosToFoldersTemp,
                                        countDirectories),
                                ++countDirectories));
                    }
                }
            }
        }
    }

    static void executeCommandAdd(File[] listFiles, File folder, int numberOfZerosToFolders, String[] extensions) {
        String fileToString,
               currentExtension;

        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //(1)
            countDirectories = 0;

        Map<String, Integer> extensionsInDir = Util.putExtensions(extensions, listFiles);

        for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
            numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
            if (!ex.getKey().equals("folders")) {
                countFilesWithCurrentExtension = 0;
                for (File file : listFiles) {
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                        if (currentExtension.equals(ex.getKey())) {
                            file.renameTo(Util.renameByAddingNumber(
                                    folder,
                                    numberOfZerosToFilesWithCurrentEx = Util.changeNumberOfZeros(
                                            numberOfZerosToFilesWithCurrentEx,
                                            countFilesWithCurrentExtension),
                                    ++countFilesWithCurrentExtension,
                                    file.getName()));
                        }
                    }
                }
            } else {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        file.renameTo(Util.renameByAddingNumber(
                                folder,
                                numberOfZerosToFoldersTemp = Util.changeNumberOfZeros(
                                        numberOfZerosToFoldersTemp,
                                        countDirectories),
                                ++countDirectories,
                                file.getName()));
                    }
                }
            }
        }
    }

    static void executeCommandDel(boolean isItFolders, boolean isItFiles, File[] listFiles, String extension, File folder) {
        String fileName,
               fileNameWithoutEx,
               fileToString;

        char[] fileNameCharArray,
               fileNameWithoutExCharArray;

        boolean separatorIsFound = false,
                numberIsFound = false;

        search:
        for (File file : listFiles) {
            fileName = file.getName();
            fileNameCharArray = fileName.toCharArray();
            fileToString = file.toString();
            if (!file.isDirectory()) {
                fileNameWithoutEx = fileName.substring(0, fileName.lastIndexOf(".")).trim();
                fileNameWithoutExCharArray = fileNameWithoutEx.toCharArray();
                for (int i = 0; i < fileNameWithoutExCharArray.length; i++) {
                    if (Character.isDigit(fileNameWithoutExCharArray[i])) {
                        if (i + 1 == fileNameWithoutExCharArray.length) {
                            continue search;
                        }
                    } else {
                        break;
                    }
                }
            } else {
                for (int i = 0; i < fileNameCharArray.length; i++) {
                    if (Character.isDigit(fileNameCharArray[i])) {
                        if (i + 1 == fileNameCharArray.length) {
                            continue search;
                        }
                    } else {
                        break;
                    }
                }
            }
            if ((isItFiles && fileToString.substring(fileToString.lastIndexOf(".") + 1).equals(extension)) ||   //files only
                (isItFiles && isItFolders) ||                                                                   //folders & files
                (isItFolders && file.isDirectory())) {                                                          //folders only
                for (int i = 0; !separatorIsFound; i++) {
                    if (Character.isDigit(fileNameCharArray[i])) {
                        numberIsFound = true;
                    } else if (numberIsFound && (Util.checkFile(file, fileName, i) ||
                                                 Util.checkFolder(file, fileName, i))) {
                        file.renameTo(Util.renameByDeletingNumber(
                                folder,
                                fileName,
                                (i - 1) + Util.getLengthOfSeparator(fileName, i)));  //здесь (i - 1) - смещение
                        separatorIsFound = true;
                    } else {
                        break;
                    }
                }
            }
            numberIsFound = false;
            separatorIsFound = false;
        }
    }

    static void executeCommandRenameAll(File[] listFiles, File folder, int numberOfZerosToFolders) {
        String fileToString,
                currentExtension;

        int countFilesWithCurrentExtension,
                numberOfZerosToFilesWithCurrentEx,
                numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //(1)
                countDirectories = 0;

        //мапа с ключом-значением: расширение-количество вхождений
        Map<String, Integer> extensionsInDir = Util.putAllExtensions(listFiles);

        for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
            countFilesWithCurrentExtension = 0;
            numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
            for (File file : listFiles) {
                if (!file.isDirectory()) {
                    fileToString = file.toString();
                    currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                    if (currentExtension.equals(ex.getKey())) {
                        file.renameTo(Util.renameToNumbersFiles(
                                folder,
                                numberOfZerosToFilesWithCurrentEx = Util.changeNumberOfZeros(
                                        numberOfZerosToFilesWithCurrentEx,
                                        countFilesWithCurrentExtension),
                                ++countFilesWithCurrentExtension,
                                currentExtension));
                    }
                } else {
                    file.renameTo(Util.renameToNumbersFolders(
                            folder,
                            numberOfZerosToFoldersTemp = Util.changeNumberOfZeros(
                                    numberOfZerosToFoldersTemp,
                                    countDirectories),
                            ++countDirectories));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            renameFiles();
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода!");
            e.printStackTrace();
        }
    }
}
