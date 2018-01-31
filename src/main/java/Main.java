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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
                System.getProperty("console.encoding", "cp866")));

        boolean pathIsEmpty,
                commandIsEmpty,
                stringOfExtensionsIsEmpty = true,
                listFilesIsEmpty = true,
                allIsEnabled  = false,
                eachIsEnabled = false;

        File folder = null;

        File[] listFiles = new File[0];

        String path,
               command = "none",
               stringOfExtensions,
               currentExtension,
               fileToString;

        String[] extensions = new String[0];

        int numberOfFiles,
            numberOfFolders = 0,
            numberOfZerosToFolders,
            numberOfZerosToFiles,
            numberOfZerosToFilesWithCurrentEx,
            countDirectories = 0,
            countFilesWithAnyExtension = 0,
            countFilesWithCurrentExtension;

        //мапа с ключом-значением: расширение-количество вхождений
        Map<String, Integer> extensionsInDir;

        System.out.print(
                "\n" +
                "+--------------------------------+\n" +
                "| Переименование файлов в папке. |\n" +
                "+--------------------------------+\n\n" +
                "Как работать с данной программой:\n" +
                "1. Прописывайте путь к папке.\n" +
                "2. Воспользуйтесь специальной командой:\n" +
                "->  all : переименование всех файлов подряд и отдельно папок\n" +
                "-> each : переименование всех файлов по расширениям\n" +
                "->  add : добавляет к названиям файлов порядковый номер\n" +
                "-> none : преобразование по умолчанию, переименовывает по каждому " +
                "введенному расширению (поле ввода этой команды можно оставить пустым)\n" +
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

            commandIsEmpty = Util.isEmpty(command);
            if (commandIsEmpty ||
                command.equals("none") ||
                command.equals("add")) {
                //do nothing, because command is correct
            } else if (command.equals("all")) {
                allIsEnabled = true;
            } else if (command.equals("each")) {
                eachIsEnabled = true;
            } else {
                System.out.print("Некорректная команда. Повторите ввод заново, начиная с пути: ");
                continue;
            }


            if (!allIsEnabled && !eachIsEnabled) {
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
            case "all":
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
                                numberOfZerosToFolders = Util.changeNumberOfZeros(
                                        numberOfZerosToFolders,
                                        countDirectories),
                                ++countDirectories));
                    }
                }
                break;

            case "each":
                extensionsInDir = Util.putAllExtensions(listFiles);

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
                                    numberOfZerosToFolders = Util.changeNumberOfZeros(
                                            numberOfZerosToFolders,
                                            countDirectories),
                                    ++countDirectories));
                        }
                    }
                }
                break;

            case "add":
                extensionsInDir = Util.putExtensions(extensions, listFiles);

                for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
                    numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
                    if (!ex.getKey().equals("folders")) {
                        countFilesWithCurrentExtension = 0;
                        for (File file : listFiles) {
                            if (!file.isDirectory()) {
                                fileToString = file.toString();
                                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                                if (currentExtension.equals(ex.getKey())) {   //renameByAddingNumber()
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
                                        numberOfZerosToFolders = Util.changeNumberOfZeros(
                                                numberOfZerosToFolders,
                                                countDirectories),
                                        ++countDirectories,
                                        file.getName()));
                            }
                        }
                    }
                }
                break;

            default:
                extensionsInDir = Util.putExtensions(extensions, listFiles);

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
                                        numberOfZerosToFolders = Util.changeNumberOfZeros(
                                                numberOfZerosToFolders,
                                                countDirectories),
                                        ++countDirectories));
                            }
                        }
                    }
                }
            }

            System.out.println("Переименование завершено.");
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
