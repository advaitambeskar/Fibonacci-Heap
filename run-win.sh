clear && make
rm output_file.txt
java keywordcounter input_1.txt
FC output_1.txt output_file.txt 
rm output_file.txt
java keywordcounter input_2.txt
FC output_2.txt output_file.txt
rm output_file.txt
java keywordcounter input_3.txt
FC output_3.txt output_file.txt