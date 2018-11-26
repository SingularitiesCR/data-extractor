FROM maven:3.5-jdk-8
WORKDIR /opt/data-extractor
ARG MAVEN_SERVER_ID
ARG MAVEN_SERVER_USERNAME
ARG MAVEN_SERVER_PASSWORD
ENV MAVEN_SETTINGS /root/.m2/settings.xml
RUN mkdir -p /root/.m2 && \
    echo "<settings>" >> $MAVEN_SETTINGS && \
    echo "<servers>" >> $MAVEN_SETTINGS && \
    echo "<server>" >> $MAVEN_SETTINGS && \
    echo "<id>$MAVEN_SERVER_ID</id>" >> $MAVEN_SETTINGS && \
    echo "<username>$MAVEN_SERVER_USERNAME</username>" >> $MAVEN_SETTINGS && \
    echo "<password>$MAVEN_SERVER_PASSWORD</password>" >> $MAVEN_SETTINGS && \
    echo "</server>" >> $MAVEN_SETTINGS && \
    echo "</servers>" >> $MAVEN_SETTINGS && \
    echo "</settings>" >> $MAVEN_SETTINGS && \
    cat $MAVEN_SETTINGS
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src .
COPY codeship/Deploy/deploy.sh .
ENTRYPOINT [ "/bin/sh" ]
