package ru.wca.rf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static ru.wca.rf.Constants.*;
import static ru.wca.rf.Util.*;

/**
 * Renaming files in specified folder.
 *
 * <p> The program was developed to rename files in a specific folder.
 * Renaming files is performed in several ways, depending on the entered command number.
 * The description of each command is given immediately after starting the program.
 *
 * <p> Use the console to work with this program.
 *
 * <p> See <a href="https://github.com/WindCrowAya/sample/blob/master/src/main/java/my_examples/RenamingFilesClass.java">old</a>
 *     and <a href="https://github.com/WindCrowAya/renaming_files">new</a> histories of project change.
 *
 * @author <a href="https://github.com/WindCrowAya">WindCrowAya</a>
 */

public class RenamingService {
    private static final Logger log = LoggerFactory.getLogger(RenamingService.class);

    /**
     * The main method for renaming files. Here is the main work of the program.
     */
    static void renameFiles() throws IOException {
        //set the property to read Cyrillic in the console, if it's present in the directory (for Russian language)
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

        showStartupMessage();

        do {
            path = reader.readLine().trim();

            pathIsEmpty = isEmpty(path);
            if (pathIsEmpty) {
                System.out.print(BLANK_QUERY);
                continue;
            }

            folder = new File(path);
            listFiles = folder.listFiles();

            listFilesIsEmpty = isEmptyArray(listFiles);
            if (listFilesIsEmpty) {
                System.out.print(EMPTY_FOLDER_OR_WRONG_PATH);
                continue;
            }

            System.out.print(ENTER_COMMAND);
            command = reader.readLine().trim();

            if (RENAME.equals(command) ||
                ADD_NUM.equals(command) ||
                DELETE_NUM.equals(command)) {
                //do nothing, because the entered command processes all files in the folder
            } else if (isEmpty(command) || RENAME_ALL.equals(command)) {
                defIsEnabled = true;
            } else if (ALL.equals(command)) {
                allIsEnabled = true;
            } else if (DELETE_ALL.equals(command)) {
                delPlusIsEnabled = true;
            } else {
                System.out.print(WRONG_COMMAND);
                continue;
            }

            //if the entered command does not process all files, then we enter extensions of the necessary files
            if (!allIsEnabled && !defIsEnabled && !delPlusIsEnabled) {
                System.out.print(ENTER_EXTENSIONS);
                stringOfExtensions = reader.readLine().trim();

                stringOfExtensionsIsEmpty = isEmpty(stringOfExtensions);
                if (stringOfExtensionsIsEmpty) {
                    System.out.print(BLANK_QUERY);
                    continue;
                }

                extensions = stringOfExtensions.split(",");
                for (int i = 0; i < extensions.length; i++) {
                    extensions[i] = extensions[i].toLowerCase().trim();
                }
                extensions = removeDuplicates(extensions);

                if (extensions.length < 1) {
                    System.out.print(NO_EXTENSIONS_ENTERED);
                }
            } else {
                stringOfExtensionsIsEmpty = false;
            }
        } while (pathIsEmpty || listFilesIsEmpty || stringOfExtensionsIsEmpty);


        //count the number of folders, files and zeros in front of them
        for (File file : listFiles) {
            if (file.isDirectory()) {
                numberOfFolders++;
            }
        }
        numberOfFiles = listFiles.length - numberOfFolders;
        numberOfZerosToFolders = String.valueOf(numberOfFolders).length() - 1;
        numberOfZerosToFiles = String.valueOf(numberOfFiles).length() - 1;


        //processing the selected command
        switch (command) {
            case ALL:
                executeCommandAll(listFiles, folder, numberOfZerosToFiles, numberOfZerosToFolders);
                break;
            case RENAME:
                executeCommandRename(listFiles, folder, numberOfZerosToFolders, extensions);
                break;
            case ADD_NUM:
                executeCommandAdd(listFiles, folder, numberOfZerosToFolders, extensions);
                break;
            case DELETE_NUM:
                for (String ex : extensions) {
                    listFiles = folder.listFiles();
                    if (!FOLDERS.equals(ex)) {
                        executeCommandDel(false, true, listFiles, ex, folder);
                    } else {
                        executeCommandDel(true, false, listFiles, ex, folder);
                    }
                }
                break;
            case DELETE_ALL:
                executeCommandDel(true, true, listFiles, null, folder);
                break;
            default:
                executeCommandRenameAll(listFiles, folder, numberOfZerosToFolders);
                break;
            }

            System.out.println(RENAME_COMPLETED);
        }

    /**
     * Command "all" renames all files and folders individually.
     *
     * @param listFiles              The list of files in the specified folder
     * @param folder                 Specified folder
     * @param numberOfZerosToFiles   The number of zeros added to the beginning of file numbering
     * @param numberOfZerosToFolders The number of zeros added to the beginning of folder numbering
     */
    public static void executeCommandAll(File[] listFiles, File folder, int numberOfZerosToFiles, int numberOfZerosToFolders) {
        String fileToString,
               currentExtension;
        int countFilesWithAnyExtension = 0,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: the change in the input parameter is undesirable; temp-variable is introduced
            countDirectories = 0;

        //pass through the file list
        for (File file : listFiles) {
            if (!file.isDirectory()) {
                fileToString = file.toString();
                currentExtension = fileToString.substring(fileToString.lastIndexOf(DOT) + 1);
                file.renameTo(renameToNumbersFiles(
                        folder,
                        numberOfZerosToFiles = changeNumberOfZeros(
                                numberOfZerosToFiles,
                                countFilesWithAnyExtension),
                        ++countFilesWithAnyExtension,
                        currentExtension));
            } else {
                file.renameTo(renameToNumbersFolders(
                        folder,
                        numberOfZerosToFoldersTemp = changeNumberOfZeros(
                                numberOfZerosToFoldersTemp,
                                countDirectories),
                        ++countDirectories));
            }
        }
    }

    /**
     * Command "rename" renames files for each entered extension.
     *
     * @param listFiles              The list of files in the specified folder
     * @param folder                 Specified folder
     * @param numberOfZerosToFolders The number of zeros added to the beginning of the folder numbering
     * @param extensions             Array of entered extensions
     */
    static void executeCommandRename(File[] listFiles, File folder, int numberOfZerosToFolders, String[] extensions) {
        String fileToString,
               currentExtension;
        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: the change in the input parameter is undesirable; temp-variable is introduced
            countDirectories = 0;
        //map with key-value: extension-number of occurrences
        Map<String, Integer> extensionsInDir = putExtensions(extensions, listFiles);

        //pass through the map, for each entered extension
        for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
            numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
            if (!ex.getKey().equals(FOLDERS)) {
                countFilesWithCurrentExtension = 0;
                for (File file : listFiles) {
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(DOT) + 1);
                        if (currentExtension.equals(ex.getKey())) {
                            file.renameTo(renameToNumbersFiles(
                                    folder,
                                    numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(
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
                        file.renameTo(renameToNumbersFolders(
                                folder,
                                numberOfZerosToFoldersTemp = changeNumberOfZeros(
                                        numberOfZerosToFoldersTemp,
                                        countDirectories),
                                ++countDirectories));
                    }
                }
            }
        }
    }

    /**
     * Command "add num" removes numbering of files for each entered extension.
     *
     * @param listFiles              The list of files in the specified folder
     * @param folder                 Specified folder
     * @param numberOfZerosToFolders The number of zeros added to the beginning of the folder numbering
     * @param extensions             Array of entered extensions
     */
    static void executeCommandAdd(File[] listFiles, File folder, int numberOfZerosToFolders, String[] extensions) {
        String fileToString,
               currentExtension;
        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: the change in the input parameter is undesirable; temp-variable is introduced
            countDirectories = 0;
        //map with key-value: extension-number of occurrences
        Map<String, Integer> extensionsInDir = putExtensions(extensions, listFiles);

        //pass through the map, for each entered extension
        for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
            numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
            if (!ex.getKey().equals(FOLDERS)) {
                countFilesWithCurrentExtension = 0;
                for (File file : listFiles) {
                    if (!file.isDirectory()) {
                        fileToString = file.toString();
                        currentExtension = fileToString.substring(fileToString.lastIndexOf(DOT) + 1);
                        if (currentExtension.equals(ex.getKey())) {
                            file.renameTo(renameByAddingNumber(
                                    folder,
                                    numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(
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
                        file.renameTo(renameByAddingNumber(
                                folder,
                                numberOfZerosToFoldersTemp = changeNumberOfZeros(
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
     * Command "delete num" removes numbering of files for each entered extension.
     * <p> Used in a loop that bypasses extensions.
     * <p> Flags {@code isItFolder} and {@code isItFiles} must have different values.
     *
     * <p> Command "delete all" removes numbering for each file in the folder.
     * <p> Flags {@code isItFolder} and {@code isItFiles} must be {@code true}.
     *
     * @param isItFolders The flag indicates whether folders will be processed
     * @param isItFiles   The flag indicates whether files will be processed
     * @param listFiles   The list of files in the specified folder in the iteration
     * @param extension   The current extension in the iteration
     * @param folder      Specified folder
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
                fileNameWithoutEx = fileName.substring(0, fileName.lastIndexOf(DOT)).trim();    //file name without extension
                fileNameWithoutExCharArray = fileNameWithoutEx.toCharArray();
                for (int i = 0; i < fileNameWithoutExCharArray.length; i++) {
                    if (Character.isDigit(fileNameWithoutExCharArray[i])) {     //checks if there are numbers at the beginning of the file name
                        if (i + 1 == fileNameWithoutExCharArray.length) {       //if the file name consists only of digits, then the digits will not be deleted
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
            if ((isItFiles && fileToString.substring(fileToString.lastIndexOf(DOT) + 1).equals(extension)) ||   //files only
                (isItFiles && isItFolders) ||                                                                       //folders & files
                (isItFolders && file.isDirectory())) {                                                              //folders only
                for (int i = 0; !separatorIsFound; i++) {   //search for separator in the file name, if not, then stop search
                    if (Character.isDigit(fileNameCharArray[i])) {
                        numberIsFound = true;
                    } else if (numberIsFound && (checkFile(file, fileName, i) ||       //checking for the presence of a number to delete
                                                 checkFolder(file, fileName, i))) {    //and for the presence of a separator
                        file.renameTo(renameByDeletingNumber(
                                folder,
                                fileName,
                                (i - 1) + lengthOfSeparator(fileName, i)));  //(i - 1) is the pointer offset
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
     * Command "rename all" renames all files by extension.
     * <p> Default command.
     *
     * @param listFiles              The list of files in the specified folder
     * @param folder                 Specified folder
     * @param numberOfZerosToFolders The number of zeros added to the beginning of the folder numbering
     */
    static void executeCommandRenameAll(File[] listFiles, File folder, int numberOfZerosToFolders) {
        String fileToString,
               currentExtension;
        int countFilesWithCurrentExtension,
            numberOfZerosToFilesWithCurrentEx,
            numberOfZerosToFoldersTemp = numberOfZerosToFolders,  //Codacy: the change in the input parameter is undesirable; temp-variable is introduced
            countDirectories = 0;
        //map with key-value: extension-number of occurrences
        Map<String, Integer> extensionsInDir = putAllExtensions(listFiles);

        //pass through the map, for each entered extension
        for (Map.Entry<String, Integer> ex : extensionsInDir.entrySet()) {
            countFilesWithCurrentExtension = 0;
            numberOfZerosToFilesWithCurrentEx = String.valueOf(ex.getValue()).length() - 1;
            for (File file : listFiles) {
                if (!file.isDirectory()) {
                    fileToString = file.toString();
                    currentExtension = fileToString.substring(fileToString.lastIndexOf(DOT) + 1);
                    if (currentExtension.equals(ex.getKey())) {
                        file.renameTo(renameToNumbersFiles(
                                folder,
                                numberOfZerosToFilesWithCurrentEx = changeNumberOfZeros(
                                        numberOfZerosToFilesWithCurrentEx,
                                        countFilesWithCurrentExtension),
                                ++countFilesWithCurrentExtension,
                                currentExtension));
                    }
                } else {
                    file.renameTo(renameToNumbersFolders(
                            folder,
                            numberOfZerosToFoldersTemp = changeNumberOfZeros(
                                    numberOfZerosToFoldersTemp,
                                    countDirectories),
                            ++countDirectories));
                }
            }
        }
    }

    private static void showStartupMessage() {
        System.out.print(
                "\n" +
                "+------------------------------------+\n" +
                "| Renaming files in specified folder |\n" +
                "+------------------------------------+\n\n" +
                "1. Enter the path to the folder\n" +
                "2. Use the special command by entering id:\n" +
                "-> 1 :        (all) rename all files and folders individually\n" +
                "-> 2 :     (rename) rename for each entered extension\n" +
                "-> 3 :    (add num) add the serial number to the file names\n" +
                "-> 4 : (delete num) remove numbering of files for each entered extensions\n" +
                "-> 5 : (delete all) remove numbering for each file\n" +
                "-> 6 : (rename all) default, renaming all files by extension\n" +
                "                   (instead of entering this command, just press Enter, leaving the input field empty)\n" +
                "3. Write extensions separated by commas, to rename folders enter \"folders\"\n\n" +
                "Enter the path: ");
    }
}
