default:
	javac keywordcounter.java
	javac Node.java
	javac Heap.java
clean:
	del keywordcounter.class
	del Node.class
	del Heap.class