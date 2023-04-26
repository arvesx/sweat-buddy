#! bin/bash

# run from Sweat_Buddy_App directory

files="Server/Server.jar Worker/Worker.jar Client/Client.jar"

for value in $files
do
    # substring="${value:31:6}"
    start call java -jar $value
    # sleep 2
done
