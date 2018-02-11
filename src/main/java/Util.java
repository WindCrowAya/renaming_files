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
        return fileName.regionMatches(counter, " - ",0, 3) ||
               fileName.regionMatches(counter, " ",  0, 1) ||
               fileName.regionMatches(counter, "-",  0, 1) ||
               fileName.regionMatches(counter, ". ", 0, 2);
    }

    static boolean checkForSeparatorsInFile(String fileName, int counter) {
        return checkForSeparators(fileName, counter) ||
               (countOfPointMoreThanOne(fileName) && fileName.regionMatches(counter, ".", 0, 1));
    }

    static boolean checkForSeparatorsInFolder(String fileName, int counter) {
        return checkForSeparators(fileName, counter) ||
               fileName.regionMatches(counter, ".", 0, 1);
    }

    static int getLengthOfSeparator(String fileName, int counter) {
        List<String> separators = new ArrayList<>();

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
}
