import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        int countDirectories = 0,
            countFilesWithAnyExtension = 0,
            countFilesWithCurrentExtension;

        System.out.print(
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
                "3. Прописывайте расширения через запятую, для переименования папок введите \"folders\"\n" +
                "\n\n" +
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


        switch (command) {
            case "all":
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        resultString = folder.toString() + "\\" + ++countDirectories;
                        resultFile = Paths.get(resultString).toFile();
                        file.renameTo(resultFile);
                        continue;
                    }
                    fileToString = file.toString();
                    currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                    resultString = folder.toString() + "\\" + ++countFilesWithAnyExtension + "." + currentExtension;
                    resultFile = Paths.get(resultString).toFile();
                    file.renameTo(resultFile);
                }
                break;

            case "each": /*тесты пройдены*/
                List<String> extensionsInDir = new ArrayList<>();

                for (File file : listFiles) {
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                        extensionsInDir.add(currentExtension);
                    }
                }

                for (String ex : removeDuplicates(extensionsInDir.toArray(new String[extensionsInDir.size()]))) {
                    countFilesWithCurrentExtension = 0;
                    for (File file : listFiles) {
                        if (file.isDirectory()) {
                            resultString = folder.toString() + "\\" + ++countDirectories;
                            resultFile = Paths.get(resultString).toFile();
                            file.renameTo(resultFile);
                        } else {
                            fileToString = file.toString();
                            currentExtension = fileToString.substring(fileToString.lastIndexOf(".") + 1);
                            if (currentExtension.equals(ex)) {
                                resultString = folder.toString() + "\\" + ++countFilesWithCurrentExtension + "." + currentExtension;
                                resultFile = Paths.get(resultString).toFile();
                                file.renameTo(resultFile);
                            }
                        }
                    }
                }
                break;

            case "add": /*тесты пройдены*/
                for (String ex : extensions) {
                    if (ex.equals("folders")) {
                        for (File file : listFiles) {
                            if (file.isDirectory()) {
                                resultString = folder.toString() + "\\" + ++countDirectories + " " + file.getName();
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
                                    resultString = folder.toString() + "\\" + ++countFilesWithCurrentExtension + " " + file.getName();
                                    resultFile = Paths.get(resultString).toFile();
                                    file.renameTo(resultFile);
                                }
                            }
                        }
                    }
                }
                break;

            default:  /*тесты пройдены*/
                for (String ex : extensions) {
                    if (ex.equals("folders")) {
                        for (File file : listFiles) {
                            if (file.isDirectory()) {
                                resultString = folder.toString() + "\\" + ++countDirectories;
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
                                    resultString = folder.toString() + "\\" + ++countFilesWithCurrentExtension + "." + currentExtension;
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


    public static void main(String[] args) {
        try {
            renameFiles();
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода!");
            e.printStackTrace();
        }
    }
}
