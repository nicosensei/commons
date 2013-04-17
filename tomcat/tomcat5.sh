#!/usr/bin/env bash

export JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26
export CATALINA_HOME=/home/ngiraud/dev/java/apache-tomcat-5.5.26
export CATALINA_BASE=/home/ngiraud/dev/java/servers/tomcat5
export CATALINA_PID=$CATALINA_BASE/logs/catalina.pid

JAVA_MEM_OPTS="-Xmx1g -Xms512m"
RETVAL=$?

start() {
    if [  -f $CATALINA_HOME/bin/startup.sh  ]; then                
        export JAVA_OPTS="$JAVA_MEM_OPTS -XX:+PrintGCDetails -XX:+PrintGCTimeStamps 
-Xloggc:$CATALINA_BASE/logs/gc.log -Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.port=7654 -Dcom.sun.management.jmxremote.authenticate=false 
-Dcom.sun.management.jmxremote.ssl=false"
#-Djava.util.logging.config.file=logging.properties"
                
        echo $"Starting Tomcat"
        $CATALINA_HOME/bin/startup.sh
    fi    
    return 1
}

debug() {
    if [  -f $CATALINA_HOME/bin/startup.sh  ]; then
        export JAVA_OPTS="$JAVA_MEM_OPTS -Xdebug 
-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6000 -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
-Xloggc:$CATALINA_BASE/logs/gc.log -Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=7654 -Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false"
#-Djava.util.logging.config.file=logging.properties"

        echo $"Starting Tomcat in DEBUG mode"
        $CATALINA_HOME/bin/startup.sh
    fi	  
    return 1
}

stop() {
    if [  -f $CATALINA_HOME/bin/shutdown.sh  ]; then
        echo $"Stopping Tomcat"
        $CATALINA_HOME/bin/shutdown.sh
    fi
    return 1
}

cleanup() {
    echo $"Cleaning up..."
    rm -rf $CATALINA_BASE/work/*
    rm -rf $CATALINA_BASE/temp/*
#    rm -rf $CATALINA_BASE/webapps/*
    echo $"... done!"
}

case "$1" in
 start)
     cleanup;
     start;
     ;;
 stop)
     stop;
     ;;
 debug)
     cleanup;
     debug;
     ;;
 restart)
     stop;
     echo $"Waiting for Tomcat to stop..."
     sleep 5
     echo $"... done!"
     cleanup;
     start;
     ;;
redebug)
    stop;
    echo $"Waiting for Tomcat to stop..."
    sleep 5
    echo $"... done!"
    cleanup;
    debug;
    ;;
 *)
     echo $"Usage: $0 {start|stop|restart|debug|redebug}"
     exit 1
     ;;
esac

 
exit $RETVAL


