[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a3090cdac212404d80d8d289dbc61870)](https://app.codacy.com/app/WindCrowAya/renaming_files)

Переименование файлов в папке
===============================
Программа предназначена для переименования файлов в заданной папке. 
Переименование происходит несколькими способами в зависимости от выбранной команды. 
##
На данный момент присутствуют шесть различных команд (в скобках указаны условные названия
команд и перед каждой командой отображен ее идентификационный номер):
1. Переименование всех файлов подряд и отдельно папок (all);
2. Переименование по каждому введенному расширению (rename);
3. Добавляет к названиям файлов порядковый номер (add num);
4. Удаляет нумерацию файлов по введенным расширениям (delete num);
5. Удаляет нумерацию каждого файла (delete all);
6. Переименование всех файлов по расширениям, является преобразованием 
по умолчанию (rename all).
##
Как работать с данной программой:
1. Прописывайте путь к папке;
2. Воспользуйтесь специальной командой, введя ее идентификационный номер;
3. (Для команд №1,№5 и №6) Прописывайте расширения через запятую, для переименования папок введите "folders".
##
Первые этапы развития проекта можно просмотреть [здесь](https://github.com/WindCrowAya/sample/blob/master/src/main/java/my_examples/RenamingFilesClass.java).
