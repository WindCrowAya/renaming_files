package ru.wca.rf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Renaming files in specified folder.
 *
 * <p> Программа предназначена для переименования файлов в определенной папке.
 * Переименование файлов происходит несколькими способами в зависимости от введенного номера команды.
 * Описание каждой команды дается сразу же после старта программы.
 *
 * <p> Use the console to work with this program.
 *
 * <p> View <a href="https://github.com/WindCrowAya/renaming_files">https://github.com/WindCrowAya/renaming_files</a>
 * to see the history of the project change.
 *
 * @author <a href="https://github.com/WindCrowAya">WindCrowAya</a>
 */

public class Main {

    /**
     * Основной метод по переименованию файлов. Здесь находится основная работа программы.
     */
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
                "                      (вместо ввода этой команды достаточно нажать Enter, оставив поле ввода пустым)\n" +
                "3. Прописывайте расширения через запятую, для переименования папок введите \"folders\"\n\n");
        do {
            System.out.print("Введите путь: ");
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


            //если введенная команда не обрабатывает все файлы, то вводим расширения нужных файлов
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


        //подсчитываем количество папок, файлов и нулей перед ними
        for (File file : listFiles) {
            if (file.isDirectory()) {
                numberOfFolders++;
            }
        }
        numberOfFiles = listFiles.length - numberOfFolders;
        numberOfZerosToFolders = String.valueOf(numberOfFolders).length() - 1;
        numberOfZerosToFiles = String.valueOf(numberOfFiles).length() - 1;


        //обработка выбранной команды
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

    /**
     * Команда "all" переименует все файлы подряд и отдельно папки.
     *
     * @param listFiles                Список файлов, находящийся в заданной папке
     * @param folder                   Заданная папка
     * @param numberOfZerosToFiles     Количество нулей, добавляемое к началу нумерации файла
     * @param numberOfZerosToFolders   Количество нулей, добавляемое к началу нумерации папки
     */
    static void executeCommandAll(File[] listFiles, File folder, int numberOfZerosToFiles, int numberOfZerosToFolders) {
        String fileToString,
               currentExtension;

        int countFilesWithAnyExtension = 0,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: изменение входного параметра нежелательно; введена temp-переменная
            countDirectories = 0;

        //проходим по всему списку файлов
        for (File file : listFiles) {
            if (!file.isDirectory()) {
                fileToString = file.toString();
                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                file.renameTo(Util.renameToNumbersFiles(
                        folder,
                        numberOfZerosToFiles = Util.changeNumberOfZeros(
                                numberOfZerosToFiles,
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

    /**
     * Команда "rename" переименует файлы по каждому введенному расширению.
     *
     * @param listFiles                Список файлов, находящийся в заданной папке
     * @param folder                   Заданная папка
     * @param numberOfZerosToFolders   Количество нулей, добавляемое к началу нумерации папки
     * @param extensions               Массив введенных расширений
     */
    static void executeCommandRename(File[] listFiles, File folder, int numberOfZerosToFolders, String[] extensions) {
        String fileToString,
               currentExtension;

        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: изменение входного параметра нежелательно; введена temp-переменная
            countDirectories = 0;

        //мапа с ключом-значением: расширение-количество вхождений
        Map<String, Integer> extensionsInDir = Util.putExtensions(extensions, listFiles);

        //проходим по мапе, по каждому введенному расширению
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

    /**
     * Команда "add num" добавляет к названиям файлов порядковый номер.
     *
     * @param listFiles                Список файлов, находящийся в заданной папке
     * @param folder                   Заданная папка
     * @param numberOfZerosToFolders   Количество нулей, добавляемое к началу нумерации папки
     * @param extensions               Массив введенных расширений
     */
    static void executeCommandAdd(File[] listFiles, File folder, int numberOfZerosToFolders, String[] extensions) {
        String fileToString,
               currentExtension;

        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: изменение входного параметра нежелательно; введена temp-переменная
            countDirectories = 0;

        //мапа с ключом-значением: расширение-количество вхождений
        Map<String, Integer> extensionsInDir = Util.putExtensions(extensions, listFiles);

        //проходим по мапе, по каждому введенному расширению
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

    /**
     * Команда "delete num" удаляет нумерацию файлов по введенным расширениям.
     * <p> Используется в цикле, обход в котором идет по расширениям.
     * <p> Флаги {@code isItFolder} и {@code isItFiles} должны иметь разные значения.
     *
     * <p> Команда "delete all" удаляет нумерацию каждого файла в папке.
     * <p> Флаги {@code isItFolder} и {@code isItFiles} должны иметь значение {@code true}.
     *
     * @param isItFolders   Флаг указывает, будут ли обрабатываться папки
     * @param isItFiles     Флаг указывает, будут ли обрабатываться файлы
     * @param listFiles     Список файлов, находящийся в заданной папке в данной итерации
     * @param extension     Текущее расширение в данной итерации
     * @param folder        Заданная папка
     */
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
                fileNameWithoutEx = fileName.substring(0, fileName.lastIndexOf(".")).trim();    //имя файла без расширения
                fileNameWithoutExCharArray = fileNameWithoutEx.toCharArray();
                for (int i = 0; i < fileNameWithoutExCharArray.length; i++) {
                    if (Character.isDigit(fileNameWithoutExCharArray[i])) {     //проверка, есть ли в начале имени файла цифры
                        if (i + 1 == fileNameWithoutExCharArray.length) {       //если файл состоит целиком из цифр, то удаления цифр не будет
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
                (isItFiles && isItFolders) ||                                                                       //folders & files
                (isItFolders && file.isDirectory())) {                                                              //folders only
                for (int i = 0; !separatorIsFound; i++) {   //поиск разделителя, если его нет, то прекращаем поиск
                    if (Character.isDigit(fileNameCharArray[i])) {
                        numberIsFound = true;
                    } else if (numberIsFound && (Util.checkFile(file, fileName, i) ||   //проверка наличия числа на удаление и проверка на наличие разделителя
                                                 Util.checkFolder(file, fileName, i))) {
                        file.renameTo(Util.renameByDeletingNumber(
                                folder,
                                fileName,
                                (i - 1) + Util.getLengthOfSeparator(fileName, i)));  //здесь (i - 1) - смещение указателя
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

    /**
     * Команда "rename all" добавляет к названиям файлов порядковый номер.
     * <p> Является командой по умолчанию.
     *
     * @param listFiles                Список файлов, находящийся в заданной папке
     * @param folder                   Заданная папка
     * @param numberOfZerosToFolders   Количество нулей, добавляемое к началу нумерации папки
     */
    static void executeCommandRenameAll(File[] listFiles, File folder, int numberOfZerosToFolders) {
        String fileToString,
               currentExtension;

        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: изменение входного параметра нежелательно; введена temp-переменная
            countDirectories = 0;

        //мапа с ключом-значением: расширение-количество вхождений
        Map<String, Integer> extensionsInDir = Util.putAllExtensions(listFiles);

        //проходим по мапе, по каждому введенному расширению
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

    /**
     * Метод, исполняющий работу программы.
     */
    public static void main(String[] args) {
        try {
            renameFiles();
        } catch (IOException e) {
            System.err.println("Ошибка ввода/вывода!");
            e.printStackTrace();
        }
    }
}
