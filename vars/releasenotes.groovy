import java.io.*;
import groovy.io.*;
import java.util.Calendar.*;
import java.text.SimpleDateFormat;
import hudson.model.*;

@NonCPS
def call(Map config=[:]) {
    def dir = new File(pwd());

    new File(dir.path + '/releasenotes.txt').withWriter('utf-8')
    {
        writer ->
            dir.eachFileRecurse(FileType.ANY) { file ->
                if (file.isDirectory()) {
                    writer.writeLine(file.name);
                }
                else
                {
                    writer.writeLine('\t' + file.name + '\t' + file.length());
                }
        }
        
        def date = new Date();
        def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        writer.writeLine("Date and time is: " + sdf.format(date));

        writer.writeLine("Build number is: ${BUILD_NUMBER}");

        def changeLogSets = currentBuild.changeSets;

        if (config.changes != "false")
        {
            for (change in changeLogSets)
            {
                def entries = change.items;
                for (entry in entries)
                {
                    writer.writeLine("${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}");
                    for (file in entry.affectedFiles) {
                        writer.writeLine("   ${file.editType.name} ${file.path}");
                    }

                }
            }
        } // end if changes to be logged
            
    }
        
    echo "Config contents:"
    for (element in config)
    {
        echo ("Key: ${element.key} Value: ${element.value}");
    }
}
