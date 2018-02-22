package ru.wca.rf;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс содержит вспомогательные методы, в которых выполняется основная работа программы.
 *
 * @author <a href="https://github.com/WindCrowAya">WindCrowAya</a>
 */

class Util {

    /**
     * Удаляет повторяющиеся элементы в массиве строк.
     *
     * @param  s Массив строк
     *
     * @return   Массив строк без повторений
     */
    static String[] removeDuplicates(String[] s) {
        Object[] o = Arrays.stream(s).distinct().toArray();
        return Arrays.copyOf(o, o.length, String[].class);
    }

    /**
     * Проверяет, является ли введенная строка пустой.
     *
     * @param  s Проверяемая строка
     *
     * @return   {@code true}, если строка пустая
     */
    static boolean isEmpty(String s) {
            return (s == null) || s.equals("");
        }

    /**
     * Добавляет нули перед нумерующими числами.
     *
     * @param number Количество добавляемых нулей
     */
    private static String addZeros(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("0");
        }
        return result.toString();
    }

    /**
     * Изменяет число нулей перед нумерующими числами.
     *
     * @param   numberOfZeros                   Проверяемое количество нулей
     * @param   countFilesWithCurrentExtension  Количество файлов с текущим расширением
     *
     * @return  Измененное количество нулей
     */
    static int changeNumberOfZeros(int numberOfZeros, int countFilesWithCurrentExtension) {
        return String.valueOf(countFilesWithCurrentExtension + 1).length() ==
               String.valueOf(countFilesWithCurrentExtension).length() + 1 ? numberOfZeros - 1 : numberOfZeros;
    }

    /**
     * Задает новою директорию файла, соединяя последовательно директорию заданной папки,
     * нумерующими числами и нулями перед ними.
     *
     * @param   folder         Директория рассматриваемой папки
     * @param   numberOfZeros  Добавляемое количество нулей
     * @param   countFiles     Нумерующее число (счетчик файлов)
     *
     * @return  Новое полное имя файла в виде строки
     */
    private static String setDirectoryZerosAndCount(File folder, int numberOfZeros, int countFiles) {
        return folder.toString() + "\\" + addZeros(numberOfZeros) + countFiles;
    }

    /**
     * Переименовывает название файла в нумерующее число, используя {@link #setDirectoryZerosAndCount(File, int, int)}
     *
     * @param   folder         Директория рассматриваемой папки
     * @param   numberOfZeros  Добавляемое количество нулей
     * @param   countFiles     Нумерующее число (счетчик файлов)
     * @param   extension      Расширение файла
     *
     * @return  Новое полное имя файла
     */
    static File renameToNumbersFiles(File folder, int numberOfZeros, int countFiles, String extension) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countFiles) + "." + extension
                        ).toFile();
    }

    /**
     * Переименовывает название папки в нумерующее число, используя {@link #setDirectoryZerosAndCount(File, int, int)}
     *
     * @param   folder            Директория рассматриваемой папки
     * @param   numberOfZeros     Добавляемое количество нулей
     * @param   countDirectories  Нумерующее число (счетчик папок)
     *
     * @return  Новое полное имя папки
     */
    static File renameToNumbersFolders(File folder, int numberOfZeros, int countDirectories) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countDirectories)
                        ).toFile();
    }

    /**
     * Добавляет к названию файла нумерующее число, используя {@link #setDirectoryZerosAndCount(File, int, int)}
     *
     * @param   folder         Директория рассматриваемой папки
     * @param   numberOfZeros  Добавляемое количество нулей
     * @param   countFiles     Нумерующее число (счетчик файлов)
     * @param   fileName       Имя файла
     *
     * @return  Новое полное имя файла с добавленной нумерацией в начале его имени
     */
    static File renameByAddingNumber(File folder, int numberOfZeros, int countFiles, String fileName) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countFiles) + " " + fileName
                        ).toFile();
    }

    /**
     * Удаляет нумерацию из имени файла или папки.
     *
     * @param   folder    Директория рассматриваемой папки
     * @param   fileName  Имя файла или папки
     * @param   counter   Указатель, с какого места нужно брать подстроку для нового имени файла
     *
     * @return  Новое полное имя файла с добавленной нумерацией в начале его имени
     */
    static File renameByDeletingNumber(File folder, String fileName, int counter) {
        return Paths.get(
                folder.toString() + "\\" + fileName.substring(counter + 1)
                        ).toFile();
    }

    /**
     * Кладет в мапу введенные расширения (key) и количество их вхождений (value).
     *
     * @param   extensions  Массив введенных расширений
     * @param   listFiles   Список файлов, находящийся в заданной папке
     *
     * @return  Мапа с расширениями и количеством их вхождений
     */
    static Map<String, Integer> putExtensions(String[] extensions, File[] listFiles) {
        Map<String, Integer> extensionsInDir = new HashMap<>();
        String fileToString,
               currentExtension;

        //добавляем в мапу введенные расширения
        for (String ex : extensions) {
            extensionsInDir.put(ex, 0);
        }

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

    /**
     * Кладет в мапу все расширения (key) из папки и количество их вхождений (value).
     *
     * @param   listFiles  Список файлов, находящийся в заданной папке
     *
     * @return  Мапа с расширениями и количеством их вхождений
     */
    static Map<String, Integer> putAllExtensions(File[] listFiles) {
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

    /**
     * Проверяет есть ли в имени данного файла более одной точки.
     *
     * @param   fileName  Имя проверяемого файла
     *
     * @return  {@code true}, если количество точек более одной
     */
    static boolean countOfPointMoreThanOne(String fileName) {
        int count = 0;
        for (char ch : fileName.toCharArray()) {
            if (String.valueOf(ch).equals(".")) {
                count++;
                if (count > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверяет наличие разделителя в имени файла или папки.
     * <p> Поддерживаемые паттерны:
     * "_" , " - " , " " , "-" , ". "
     *
     * @param   fileName  Имя проверяемого файла или папки
     * @param   counter   Указатель, с какого места начинается проверка
     *
     * @return  {@code true}, если найден один из видов разделителей
     */
    private static boolean checkForSeparators(String fileName, int counter) {
        return fileName.regionMatches(counter, "_",  0, 1) ||
               fileName.regionMatches(counter, " - ",0, 3) ||
               fileName.regionMatches(counter, " ",  0, 1) ||
               fileName.regionMatches(counter, "-",  0, 1) ||
               fileName.regionMatches(counter, ". ", 0, 2);
    }

    /**
     * Проверяет наличие разделителя в имени файла.
     * <p> Помимо паттернов из {@link #checkForSeparators(String, int)} добавляется паттерн ".".
     *
     * @param   fileName  Имя проверяемого файла
     * @param   counter   Указатель, с какого места начинается проверка
     *
     * @return  {@code true}, если найден один из видов разделителей (в том числе и из {@link #checkForSeparators(String, int)})
     */
    static boolean checkForSeparatorsInFile(String fileName, int counter) {
        return checkForSeparators(fileName, counter) ||
               (countOfPointMoreThanOne(fileName) && fileName.regionMatches(counter, ".", 0, 1));
    }

    /**
     * Проверяет наличие разделителя в имени папки.
     * <p> Помимо паттернов из {@link #checkForSeparators(String, int)} добавляется паттерн ".".
     *
     * @param   folderName  Имя проверяемой папки
     * @param   counter     Указатель, с какого места начинается проверка
     *
     * @return  {@code true}, если найден один из видов разделителей (в том числе и из {@link #checkForSeparators(String, int)})
     */
    static boolean checkForSeparatorsInFolder(String folderName, int counter) {
        return checkForSeparators(folderName, counter) ||
               folderName.regionMatches(counter, ".", 0, 1);
    }

    /**
     * Подсчитывает длину разделителя в проверяемом файле или папке.
     *
     * @param   fileName  Имя проверяемого файла или папки
     * @param   counter   Указатель, с какого места начинается проверка
     *
     * @return  Длина разделителя
     */
    static int getLengthOfSeparator(String fileName, int counter) {
        List<String> separators = new ArrayList<>();

        separators.add("_");
        separators.add(" - ");
        separators.add(" ");
        separators.add("-");
        separators.add(". ");
        separators.add(".");

        int length;
        for (String separator : separators) {
            length = separator.length();
            if (fileName.regionMatches(counter, separator, 0, length)) {
                return length;
            }
        }

        //не должен срабатывать, поскольку в условии имеется проверка на разделители
        return 0;
    }

    /**
     * Проверяет, является ли данный файл файлом и если да, то есть ли у этого файла разделители.
     *
     * @param   file      Проверяемый файл
     * @param   fileName  Имя проверяемого файла
     * @param   counter   Указатель, с какого места начинается проверка
     *
     * @return  {@code true}, если это файл с разделителем
     */
    static boolean checkFile(File file, String fileName, int counter) {
        return !file.isDirectory() && Util.checkForSeparatorsInFile(fileName, counter);
    }

    /**
     * Проверяет, является ли данный файл папкой и если да, то есть ли у этой папки разделители.
     *
     * @param   file      Проверяемый файл
     * @param   fileName  Имя проверяемого файла
     * @param   counter   Указатель, с какого места начинается проверка
     *
     * @return  {@code true}, если это папка с разделителем
     */
    static boolean checkFolder(File file, String fileName, int counter) {
        return file.isDirectory() && Util.checkForSeparatorsInFolder(fileName, counter);
    }
}
