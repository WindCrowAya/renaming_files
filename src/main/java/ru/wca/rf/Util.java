package ru.wca.rf;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

class Util {
    static String[] removeDuplicates(String[] s) {
        Object[] o = Arrays.stream(s).distinct().toArray();
        return Arrays.copyOf(o, o.length, String[].class);
    }

    static boolean isEmpty(String s) {
            return (s == null) || s.equals("");
        }

    private static String addZeros(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("0");
        }
        return result.toString();
    }

    static int changeNumberOfZeros(int numberOfZeros, int countFilesWithSomeExtension) {
        return String.valueOf(countFilesWithSomeExtension + 1).length() ==
               String.valueOf(countFilesWithSomeExtension).length() + 1 ? numberOfZeros - 1 : numberOfZeros;
    }

    private static String setDirectoryZerosAndCount(File folder, int numberOfZeros, int countFiles) {
        return folder.toString() + "\\" + addZeros(numberOfZeros) + countFiles;
    }

    static File renameToNumbersFiles(File folder, int numberOfZeros, int countFiles, String extension) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countFiles) + "." + extension
                        ).toFile();
    }

    static File renameToNumbersFolders(File folder, int numberOfZeros, int countDirectories) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countDirectories)
                        ).toFile();
    }

    static File renameByAddingNumber(File folder, int numberOfZeros, int countFiles, String fileName) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countFiles) + " " + fileName
                        ).toFile();
    }

    static File renameByDeletingNumber(File folder, String fileName, int counter) {
        return Paths.get(
                folder.toString() + "\\" + fileName.substring(counter + 1)
                        ).toFile();
    }

    static Map<String, Integer> putExtensions(String[] extensions, File[] listFiles) {
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

    static boolean countOfPointMoreThanOne(String fileToString) {
        int count = 0;
        for (char ch : fileToString.toCharArray()) {
            if (String.valueOf(ch).equals(".")) {
                count++;
                if (count > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkForSeparators(String fileName, int counter) {
        return fileName.regionMatches(counter, "_",  0, 1) ||
               fileName.regionMatches(counter, " - ",0, 3) ||
               fileName.regionMatches(counter, " ",  0, 1) ||
               fileName.regionMatches(counter, "-",  0, 1) ||
               fileName.regionMatches(counter, ". ", 0, 2);
    }

    static boolean checkForSeparatorsInFile(String fileName, int counter) {
        return checkForSeparators(fileName, counter) ||
               (countOfPointMoreThanOne(fileName) && fileName.regionMatches(counter, ".", 0, 1));
    }

    private static boolean checkForSeparatorsInFolder(String fileName, int counter) {
        return checkForSeparators(fileName, counter) ||
               fileName.regionMatches(counter, ".", 0, 1);
    }

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

    private static boolean checkFile(File file, String fileName, int counter) {
        return !file.isDirectory() && Util.checkForSeparatorsInFile(fileName, counter);
    }

    private static boolean checkFolder(File file, String fileName, int counter) {
        return file.isDirectory() && Util.checkForSeparatorsInFolder(fileName, counter);
    }

    static void deletingNumbers(boolean isItFolders, boolean isItFiles, File[] listFiles, String extension, File folder) {
        String fileName,
               fileToString;

        char[] fileNameCharArray;

        boolean separatorIsFound = false,
                numberIsFound = false;

        for (File file : listFiles) {
            fileName = file.getName();
            fileNameCharArray = fileName.toCharArray();
            fileToString = file.toString();
            if ((isItFiles && fileToString.substring(fileToString.lastIndexOf(".") + 1).equals(extension)) ||
                (isItFiles && isItFolders) ||
                (isItFolders && file.isDirectory())) {
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
}
