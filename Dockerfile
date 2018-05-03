FROM 192.168.1.202/common/basejava
RUN mkdir /data
VOLUME /data
ADD ./target/dfs-proxy-1.0-SNAPSHOT.jar app.jar
EXPOSE 9334
ENTRYPOINT ["java","-javaagent:/data/pp-agent/pinpoint-bootstrap-1.6.0.jar","-Dpinpoint.agentId=dfs-100","-Dpinpoint.applicationName=dfs","-jar","/app.jar"]