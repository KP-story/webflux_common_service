FROM adoptopenjdk/openjdk11:jdk-11.0.11_9
RUN apt-get update && apt-get -yq install fonts-dejavu fontconfig
RUN apt-get update && apt-get -yq install --no-install-recommends vim-tiny

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=80.0 -XX:+CrashOnOutOfMemoryError"
ENV TZ="Asia/Ho_Chi_Minh"
COPY ./news-1.0.jar /app/
ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar", "/app/news-1.0.jar"]