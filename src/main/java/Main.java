import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;

/**
 * Renaming files in specified folder.
 * Use the console to work with this program.
 *
 * Useful links:
 *   Arrays.copyOf() : https://stackoverflow.com/questions/1018750/how-to-convert-object-array-to-string-array-in-java
 *
 * View https://github.com/WindCrowAya/sample to see the history of the project change
 */
public class Main {

    private static void renameFiles() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean pathIsEmpty,
                commandIsEmpty,
                stringOfExtensionsIsEmpty = true,
                listFilesIsEmpty = true,
                allIsEnabled  = false,
                eachIsEnabled = false;

        File folder = null,
             resultFile;

        File[] listFiles = new File[0];

        String path,
               command = "none",
               stringOfExtensions,
               currentExtension,
               fileToString,
               resultString;

        String[] extensions = new String[0];

        int numberOfFiles,
            numberOfFolders = 0,
            numberOfZerosToFolders,
            numberOfZerosToFiles,
            countDirectories = 0,
            countFilesWithAnyExtension = 0,
            countFilesWithCurrentExtension;

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
                "введенному расширению (вместо ввода этой команды можно нажать Enter)\n" +
                "3. Прописывайте расширения через запятую, для переименования папок введите \"folders\"\n\n" +
                "Введите путь: ");
        do {
            path = reader.readLine().trim();

            pathIsEmpty = isEmpty(path);
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

            commandIsEmpty = isEmpty(command);
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

                stringOfExtensionsIsEmpty = isEmpty(stringOfExtensions);
                if (stringOfExtensionsIsEmpty) {
                    System.out.print("Пустой запрос. Повторите ввод заново, начиная с пути: ");
                    continue;
                }

                extensions = removeDuplicates(stringOfExtensions.split(","));

                if (extensions.length < 1) {
                    System.out.print("Не введены расширения. Повторите ввод заново, начиная с пути: ");
                }
            } else {
                stringOfExtensionsIsEmpty = false;
            }

            } while (pathIsEmpty || listFilesIsEmpty || stringOfExtensionsIsEmpty);


        for (File file : listFiles)
            if (file.isDirectory())
                numberOfFolders++;
        numberOfFiles = listFiles.length - numberOfFolders;
        numberOfZerosToFolders = String.valueOf(numberOfFolders).length() - 1;
        numberOfZerosToFiles = String.valueOf(numberOfFiles).length() - 1;


        switch (command) {
            case "all": //OK
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        numberOfZerosToFolders = changeNumberOfZeros(numberOfZerosToFolders, countDirectories);
                        resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFolders) + ++countDirectories;
                        resultFile = Paths.get(resultString).toFile();
                        file.renameTo(resultFile);
                    } else {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                        numberOfZerosToFiles = changeNumberOfZeros(numberOfZerosToFiles, countFilesWithAnyExtension);
                        resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFiles) + ++countFilesWithAnyExtension + "." + currentExtension;
                        resultFile = Paths.get(resultString).toFile();
                        file.renameTo(resultFile);
                    }
                }
                break;

            case "each": //OK
                //мапа с ключом - значением: "расширение - количество вхождений"
                Map<String, Integer> extensionsInDir = new HashMap<>();

                //находим все расширения в папке и считаем их количество
                for (File file : listFiles) {
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                        if (extensionsInDir.containsKey(currentExtension)) {
                            extensionsInDir.put(currentExtension, extensionsInDir.get(currentExtension) + 1); //можно ли оптимизировать так, чтобы не делать постоянно put?
                        } else {
                            extensionsInDir.put(currentExtension, 1);
                        }
                    }
                }

                int numberOfZerosToFilesWithCurrentEx;

                for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
                    countFilesWithCurrentExtension = 0;
                    numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
                    for (File file : listFiles) {
                        if (file.isDirectory()) {                                                             //возможно здесь много лишних раз переименуются папки
                            numberOfZerosToFolders = changeNumberOfZeros(numberOfZerosToFolders, countDirectories);
                            resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFolders) + ++countDirectories;
                            resultFile = Paths.get(resultString).toFile();
                            file.renameTo(resultFile);
                        } else {
                            fileToString = file.toString();
                            currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                            if (currentExtension.equals(ex.getKey())) {
                                numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(numberOfZerosToFilesWithCurrentEx, countFilesWithCurrentExtension);
                                resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFilesWithCurrentEx) +
                                        ++countFilesWithCurrentExtension + "." + currentExtension;
                                resultFile = Paths.get(resultString).toFile();
                                file.renameTo(resultFile);
                            }
                        }
                    }
                }
                break;

            case "add":
                for (String ex : extensions) {
                    if (ex.equals("folders")) {
                        for (File file : listFiles) {
                            if (file.isDirectory()) {
                                numberOfZerosToFolders = changeNumberOfZeros(numberOfZerosToFolders, countDirectories);
                                resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFolders) + ++countDirectories + " " + file.getName();
                                resultFile = Paths.get(resultString).toFile();
                                file.renameTo(resultFile);
                            }
                        }
                    } else {
                        countFilesWithCurrentExtension = 0;
                        for (File file : listFiles) {
                            if (!file.isDirectory()) {
                                fileToString = file.toString();
                                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                                if (currentExtension.equals(ex)) {
                                    numberOfZerosToFiles = changeNumberOfZeros(numberOfZerosToFiles, countFilesWithCurrentExtension);
                                    resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFiles) + ++countFilesWithCurrentExtension + " " + file.getName();
                                    resultFile = Paths.get(resultString).toFile();
                                    file.renameTo(resultFile);
                                }
                            }
                        }
                    }
                }
                break;

            default:
                for (String ex : extensions) {
                    if (ex.equals("folders")) {
                        for (File file : listFiles) {
                            if (file.isDirectory()) {
                                numberOfZerosToFolders = changeNumberOfZeros(numberOfZerosToFolders, countDirectories);
                                resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFolders) + ++countDirectories;
                                resultFile = Paths.get(resultString).toFile();
                                file.renameTo(resultFile);
                            }
                        }
                    } else {
                        countFilesWithCurrentExtension = 0;
                        for (File file : listFiles) {
                            if (!file.isDirectory()) {
                                fileToString = file.toString();
                                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                                if (currentExtension.equals(ex)) {
                                    numberOfZerosToFiles = changeNumberOfZeros(numberOfZerosToFiles, countFilesWithCurrentExtension);
                                    resultString = folder.toString() + "\\" + addZeros(numberOfZerosToFiles) + ++countFilesWithCurrentExtension + "." + currentExtension;
                                    resultFile = Paths.get(resultString).toFile();
                                    file.renameTo(resultFile);
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("Переименование завершено.");
        }

    private static String[] removeDuplicates(String[] s) {
        Object[] o = Arrays.stream(s).distinct().toArray();
        return Arrays.copyOf(o, o.length, String[].class);
    }

    private static boolean isEmpty(String s) {
            return (s == null) || s.equals("");
        }

    private static String addZeros(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("0");
        }
        return result.toString();
    }

    private static int changeNumberOfZeros(int numberOfZeros, int countFilesWithSomeExtension) {
        return String.valueOf(countFilesWithSomeExtension + 1).length() ==
               String.valueOf(countFilesWithSomeExtension).length() + 1 ? numberOfZeros - 1 : numberOfZeros;
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
