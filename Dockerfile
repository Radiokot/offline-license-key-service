FROM bellsoft/liberica-runtime-container:jdk-slim
COPY /build/libs/application.jar /application.jar
ENTRYPOINT java \
$(test -f /log4j.properties && echo "-Dlog4j.configuration=file:/log4j.properties") \
-jar /application.jar
