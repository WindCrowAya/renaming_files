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

        //мапа с ключом - значением: "расширение - количество вхождений"
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

                extensions = stringOfExtensions.split(",");
                for (int i = 0; i < extensions.length; i++) {
                    extensions[i] = extensions[i].trim();
                }
                extensions = removeDuplicates(extensions);

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
            case "all":
                for (File file : listFiles) {   //renameToNumbersFiles()
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                        file.renameTo(renameToNumbersFiles(
                                numberOfZerosToFiles = changeNumberOfZeros(numberOfZerosToFiles, countFilesWithAnyExtension),
                                ++countFilesWithAnyExtension,
                                folder,
                                currentExtension));
                    } else {                    //renameToNumbersFolders()
                        file.renameTo(renameToNumbersFolders(
                                numberOfZerosToFolders = changeNumberOfZeros(numberOfZerosToFolders, countDirectories),
                                ++countDirectories,
                                folder));
                    }
                }
                break;

            case "each":
                extensionsInDir = putAllExtensions(listFiles);   //putAllExtensions()

                for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {   //renameToNumbersFiles()
                    countFilesWithCurrentExtension = 0;
                    numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
                    for (File file : listFiles) {
                        if (!file.isDirectory()) {
                            fileToString = file.toString();
                            currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                            if (currentExtension.equals(ex.getKey())) {
                                file.renameTo(renameToNumbersFiles(
                                        numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(
                                                numberOfZerosToFilesWithCurrentEx,
                                                countFilesWithCurrentExtension),
                                        ++countFilesWithCurrentExtension,
                                        folder,
                                        currentExtension));
                            }
                        } else { //возможно здесь много лишних раз переименуются папки ; renameToNumbersFolders()
                            file.renameTo(renameToNumbersFolders(
                                    numberOfZerosToFolders = changeNumberOfZeros(
                                            numberOfZerosToFolders,
                                            countDirectories),
                                    ++countDirectories,
                                    folder));
                        }
                    }
                }
                break;

            case "add":
                extensionsInDir = putExtensions(extensions, listFiles);   //putExtensions()

                for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
                    numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
                    if (!ex.getKey().equals("folders")) {
                        countFilesWithCurrentExtension = 0;
                        for (File file : listFiles) {
                            if (!file.isDirectory()) {
                                fileToString = file.toString();
                                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                                if (currentExtension.equals(ex.getKey())) {   //renameByAddingNumberFiles()
                                    file.renameTo(renameByAddingNumberFiles(
                                            numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(
                                                    numberOfZerosToFilesWithCurrentEx,
                                                    countFilesWithCurrentExtension),
                                            ++countFilesWithCurrentExtension,
                                            folder,
                                            file.getName()));
                                }
                            }
                        }
                    } else {   //renameByAddingNumberFolders()
                        for (File file : listFiles) {
                            if (file.isDirectory()) {
                                file.renameTo(renameByAddingNumberFolders(
                                        numberOfZerosToFolders = changeNumberOfZeros(
                                                numberOfZerosToFolders,
                                                countDirectories),
                                        ++countDirectories,
                                        folder,
                                        file.getName()));
                            }
                        }
                    }
                }
                break;

            default:
                extensionsInDir = putExtensions(extensions, listFiles);   //putExtensions()

                for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {   //renameToNumbersFiles()
                    numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
                    if (!ex.getKey().equals("folders")) {
                        countFilesWithCurrentExtension = 0;
                        for (File file : listFiles) {
                            if (!file.isDirectory()) {
                                fileToString = file.toString();
                                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                                if (currentExtension.equals(ex.getKey())) {
                                    file.renameTo(renameToNumbersFiles(
                                            numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(
                                                    numberOfZerosToFilesWithCurrentEx,
                                                    countFilesWithCurrentExtension),
                                            ++countFilesWithCurrentExtension,
                                            folder,
                                            currentExtension));
                                }
                            }
                        }
                    } else {   //renameToNumbersFolders()
                        for (File file : listFiles) {
                            if (file.isDirectory()) {
                                file.renameTo(renameToNumbersFolders(
                                        numberOfZerosToFolders = changeNumberOfZeros(
                                                numberOfZerosToFolders,
                                                countDirectories),
                                        ++countDirectories,
                                        folder));
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

    private static File renameToNumbersFiles(int numberOfZeros, int countFiles, File folder, String extension) {
        String result = folder.toString() + "\\" + addZeros(numberOfZeros) + countFiles + "." + extension;
        return Paths.get(result).toFile();
    }

    private static File renameToNumbersFolders(int numberOfZeros, int countDirectories, File folder) {
        String result = folder.toString() + "\\" + addZeros(numberOfZeros) + countDirectories;
        return Paths.get(result).toFile();
    }

    private static File renameByAddingNumberFiles(int numberOfZeros, int countFiles, File folder, String fileName) {
        String result = folder.toString() + "\\" + addZeros(numberOfZeros) + countFiles + " " + fileName;
        return Paths.get(result).toFile();
    }

    private static File renameByAddingNumberFolders(int numberOfZeros, int countDirectories, File folder, String fileName) {
        String result = folder.toString() + "\\" + addZeros(numberOfZeros) + countDirectories + " " + fileName;
        return Paths.get(result).toFile();
    }

    private static Map<String, Integer> putExtensions(String[] extensions, File[] listFiles) {
        Map<String, Integer> extensionsInDir = new HashMap<>();
        String fileToString,
               currentExtension;

        //добавляем в мапу введенные расширения
        for (String ex : extensions)
            extensionsInDir.put(ex, 0);

        //считаем количество вхождений для каждого расширения
        for (File file : listFiles) {
            if (!file.isDirectory()) {
                fileToString = file.toString();
                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                if (extensionsInDir.containsKey(currentExtension)) {
                    extensionsInDir.put(currentExtension, extensionsInDir.get(currentExtension) + 1);
                }
            } else {
                if (extensionsInDir.containsKey("folders")) {
                    extensionsInDir.put("folders", extensionsInDir.get("folders") + 1);
                }
            }
        }

        return extensionsInDir;
    }

    private static Map<String, Integer> putAllExtensions(File[] listFiles) {
        Map<String, Integer> extensionsInDir = new HashMap<>();
        String fileToString,
               currentExtension;

        //находим все расширения в папке и считаем их количество
        for (File file : listFiles) {
            if (!file.isDirectory()) {
                fileToString = file.toString();
                currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                if (extensionsInDir.containsKey(currentExtension)) {
                    extensionsInDir.put(currentExtension, extensionsInDir.get(currentExtension) + 1);
                } else {
                    extensionsInDir.put(currentExtension, 1);
                }
            }
        }

        return extensionsInDir;
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
