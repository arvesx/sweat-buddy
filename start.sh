#! bin/bash

# run from Sweat_Buddy_App directory

files="Server/sweat_buddy_server.jar Worker/sweat_buddy_worker.jar Client/sweat_buddy_client.jar"

for value in $files
do
    # substring="${value:31:6}"
    start call java -jar $value
    # sleep 2
done
