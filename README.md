[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c47945a49ffa4afdaa9f7e07627cbf66)](https://app.codacy.com/app/WindCrowAya/renaming_files?utm_source=github.com&utm_medium=referral&utm_content=WindCrowAya/renaming_files&utm_campaign=badger)

Renaming files in specified folder
==================================
The program is designed to rename files in specified folder.
Renaming occurs in several ways, depending on the selected command.
##
At the moment there are six different commands (in brackets are the conditional names 
of the commands and before each command its id number):
1. Rename all files and folders individually (all);
2. Rename for each entered extension (rename);
3. Add the serial number to the file names (add num);
4. Remove numbering of files for each entered extensions (delete num);
5. Remove numbering for each file (delete all);
6. Renaming all files by extension, default command (rename all).
##
How to work with this program:
1. Enter the path to the folder;
2. Use the special command by entering id;
3. (For commands №1,№5 и №6) Write extensions separated by commas, to rename the folders enter "folders".
##
The first stages of the project see [here](https://github.com/WindCrowAya/sample/blob/master/src/main/java/my_examples/RenamingFilesClass.java).
