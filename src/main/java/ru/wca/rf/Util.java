package ru.wca.rf;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class contains util methods that perform the main work of the program.
 *
 * @author <a href="https://github.com/WindCrowAya">WindCrowAya</a>
 */

class Util {

    /**
     * List of available separators, without "."
     * <p> Supported patterns:
     * "_" , " - " , " " , "-" , ". "
     */
    private static final List<String> separators = new ArrayList<>(Arrays.asList("_", " - ", " ", "-", ". "));

    /**
     * Removes duplicate items in a string array.
     *
     * @param  s Array of strings
     *
     * @return   Array of strings without duplications
     */
    static String[] removeDuplicates(String[] s) {
        Object[] o = Arrays.stream(s).distinct().toArray();
        return Arrays.copyOf(o, o.length, String[].class);
    }

    /**
     * Checks whether the entered string is empty.
     *
     * @param  s String for check
     *
     * @return   {@code true}, if string {@code s} is empty
     */
    static boolean isEmpty(String s) {
            return (s == null) || s.equals("");
        }

    /**
     * Adds leading zeros before numbering numbers.
     *
     * @param number The number of zeros to add
     */
    private static String addZeros(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("0");
        }
        return result.toString();
    }

    /**
     * Changes the number of zeros before numbering numbers.
     *
     * @param   numberOfZeros                   Number of zeros for checking
     * @param   countFilesWithCurrentExtension  Count of files with current extension
     *
     * @return  Changed the number of zeros
     */
    static int changeNumberOfZeros(int numberOfZeros, int countFilesWithCurrentExtension) {
        return String.valueOf(countFilesWithCurrentExtension + 1).length() ==
               String.valueOf(countFilesWithCurrentExtension).length() + 1 ? numberOfZeros - 1 : numberOfZeros;
    }

    /**
     * Set the new directory of the file, sequentially linking the directory of the specified folder,
     * numbering numbers and zeros in front of them.
     *
     * @param   folder         Directory of specified folder
     * @param   numberOfZeros  Additional number of zeros
     * @param   countFiles     Numbering number (file counter)
     *
     * @return  New full file name as string
     */
    private static String setDirectoryZerosAndCount(File folder, int numberOfZeros, int countFiles) {
        return folder.toString() + "\\" + addZeros(numberOfZeros) + countFiles;
    }

    /**
     * Renames the file name to a numbering number using {@link #setDirectoryZerosAndCount(File, int, int)}.
     *
     * @param   folder         Directory of specified folder
     * @param   numberOfZeros  Additional number of zeros
     * @param   countFiles     Numbering number (file counter)
     * @param   extension      File extension
     *
     * @return  New full file name
     */
    static File renameToNumbersFiles(File folder, int numberOfZeros, int countFiles, String extension) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countFiles) + "." + extension
                        ).toFile();
    }

    /**
     * Renames the folder name to a numbering number using {@link #setDirectoryZerosAndCount(File, int, int)}.
     *
     * @param   folder            Directory of specified folder
     * @param   numberOfZeros     Additional number of zeros
     * @param   countDirectories  Numbering number (folder counter)
     *
     * @return  New full folder name
     */
    static File renameToNumbersFolders(File folder, int numberOfZeros, int countDirectories) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countDirectories)
                        ).toFile();
    }

    /**
     * Adds a numbering number to the file name, using {@link #setDirectoryZerosAndCount(File, int, int)}.
     *
     * @param   folder         Directory of specified folder
     * @param   numberOfZeros  Additional number of zeros
     * @param   countFiles     Numbering number (file counter)
     * @param   fileName       File name
     *
     * @return  New full filename with added numbering at the beginning of its name
     */
    static File renameByAddingNumber(File folder, int numberOfZeros, int countFiles, String fileName) {
        return Paths.get(
                setDirectoryZerosAndCount(folder, numberOfZeros, countFiles) + " " + fileName
                        ).toFile();
    }

    /**
     * Removes numbering from the file or folder name.
     *
     * @param   folder    Directory of specified folder
     * @param   fileName  File or folder name
     * @param   counter   A pointer from where you need to take a substring for a new file or folder name
     *
     * @return  New full filename with the numbering removed at the beginning of its name
     */
    static File renameByDeletingNumber(File folder, String fileName, int counter) {
        return Paths.get(
                folder.toString() + "\\" + fileName.substring(counter + 1)
                        ).toFile();
    }

    /**
     * Inserts entered extensions (key) and the number of their occurrences (value) into the map.
     *
     * @param   extensions  Array of entered extensions
     * @param   listFiles   The list of files in the specified folder
     *
     * @return  Map with entered extensions and the number of their occurrences
     */
    static Map<String, Integer> putExtensions(String[] extensions, File[] listFiles) {
        Map<String, Integer> extensionsInDir = new HashMap<>();
        String fileToString,
               currentExtension;

        //adds entered extensions into the map
        for (String ex : extensions) {
            extensionsInDir.put(ex, 0);
        }

        //count the number of occurrences for each extension
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
     * Inserts all extensions (key) from folder and the number of their occurrences (value) into the map.
     *
     * @param   listFiles  The list of files in the specified folder
     *
     * @return  Map with entered extensions and the number of their occurrences
     */
    static Map<String, Integer> putAllExtensions(File[] listFiles) {
        Map<String, Integer> extensionsInDir = new HashMap<>();
        String fileToString,
               currentExtension;

        //find all extensions in the folder and count their number
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
     * Checks if there is more than one point in the name of this file or folder.
     *
     * @param   fileName  File or folder name for checking
     *
     * @return  {@code true}, if the number of points is more than one (if not, then it's only folder)
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
     * Checks for a separator in a file or folder name.
     *
     * @param   fileName  File of folder name for checking
     * @param   counter   Pointer, where the checking starts
     *
     * @return  {@code true}, if one of the types of separators is found
     */
    private static boolean checkForSeparators(String fileName, int counter) {
        boolean hasSeparator = false;
        for (String separator: separators) {
            if (hasSeparator = fileName.regionMatches(counter, separator, 0, separator.length())) { // assigning, not equals!!!
                break;
            }
        }
        return hasSeparator;
    }

    /**
     * Checks for a separator in a file name.
     * <p> In addition to the patterns from {@link #separators}, a pattern ".".
     *
     * @param   fileName  File name for checking
     * @param   counter   Pointer, where the checking starts
     *
     * @return  {@code true}, if one of the types of separators is found (including from {@link #checkForSeparators(String, int)})
     */
    static boolean checkForSeparatorsInFile(String fileName, int counter) {
        return checkForSeparators(fileName, counter) ||
               (countOfPointMoreThanOne(fileName) && fileName.regionMatches(counter, ".", 0, 1));
    }

    /**
     * Checks for a separator in a folder name.
     * <p> In addition to the patterns from {@link #separators}, a pattern ".".
     *
     * @param   folderName  Folder name for checking
     * @param   counter     Pointer, where the checking starts
     *
     * @return  {@code true}, if one of the types of separators is found (including from {@link #checkForSeparators(String, int)})
     */
    static boolean checkForSeparatorsInFolder(String folderName, int counter) {
        return checkForSeparators(folderName, counter) ||
               folderName.regionMatches(counter, ".", 0, 1);
    }

    /**
     * Counts length of the separator in file or folder.
     *
     * @param   fileName  File or folder name
     * @param   counter   Pointer, where the checking starts
     *
     * @return  The length of separator
     */
    static int getLengthOfSeparator(String fileName, int counter) {
        separators.add(".");

        int length;
        for (String separator : separators) {
            length = separator.length();
            if (fileName.regionMatches(counter, separator, 0, length)) {
                return length;
            }
        }

        //it shouldn't work, since there is a condition for checking separator
        return 0;
    }

    /**
     * Checks whether the file is a file. If so, checks whether this file has a separator.
     *
     * @param   file      File for checking
     * @param   fileName  File name
     * @param   counter   Pointer, where the checking starts
     *
     * @return  {@code true}, if it's a file with separator
     */
    static boolean checkFile(File file, String fileName, int counter) {
        return !file.isDirectory() && Util.checkForSeparatorsInFile(fileName, counter);
    }

    /**
     * Checks whether the file is a folder. If so, checks whether this folder has a separator.
     *
     * @param   file      File for checking
     * @param   fileName  File name
     * @param   counter   Pointer, where the checking starts
     *
     * @return  {@code true}, if it's a folder with separator
     */
    static boolean checkFolder(File file, String fileName, int counter) {
        return file.isDirectory() && Util.checkForSeparatorsInFolder(fileName, counter);
    }
}
