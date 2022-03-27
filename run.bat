REM del *.class
REM javac InvadedSpace.java
REM java InvadedSpace
del ./bin/*.class
javac -d ./bin ./src/*.java
java ./bin/InvadedSpace

