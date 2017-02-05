# WikiPageReader

###This WikiReader project is sending X GET requests to wikipedia main page with X threads and calculate response time. Tested on Mac OS X 10.9.4


####To just run the project, please follow below steps:

1. On github page: https://github.com/zhanghuijun1988/WikiPageReader, go to 'src' folder, click 'WikiReader.jar'

2. Click 'View Raw' to download WikiReader.jar

3. Open a terminal, and 'cd' to the folder where WikiReader.jar is downloaded

4. run command 'java -jar WikiReader.jar'

5. Choose number of requests and whether print response page log: eg. 100, N

6. Response time of GET requests will be printed in terminal 


####To modify code and re-compile the project, please follow below steps:

1. Fork/Download project to your local computer

2. Unzip code if needed, open a terminal, and go to src/main folder where the code is located

3. Modify code as needed

4. To generate .class files, run command 'javac *.java'

5. Go back to src folder, run command 'cd ..'

6. Run generated .class files, run command 'java main.Reader'

7. To generate a new .jar file, run command 'jar -cvfm WikiReader.jar MANIFEST.txt main/*.class'

8. Run the new .jar file, run command 'java -jar WikiReader.jar'
