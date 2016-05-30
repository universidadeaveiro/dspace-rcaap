#!/bin/bash

#First get the script dir
cd `dirname $0`
DIR_NAME=$(pwd)

#Second read the dspace.cfg file and retrieve item_export_time value
##SET PATH TO DSPACE_CFG
cd $DIR_NAME
cd ../config/
#pwd

#PATH_OF_FILES_TO_REMOVE=`cat dspace.cfg | grep "org.dspace.app.itemexport.download.dir" | cut -d" " -f3`
#TIME_TO_LIVE_HOURS=`cat dspace.cfg | grep "org.dspace.app.itemexport.life.span.hours" | cut -d" " -f3`

#THIS IS A BETTER OPTION
PATH_OF_FILES_TO_REMOVE=`cat dspace.cfg | grep "org.dspace.app.itemexport.download.dir" | cut -d"=" -f2 | sed -e 's/^[[:space:]]*//'`
TIME_TO_LIVE_HOURS=`cat dspace.cfg | grep "org.dspace.app.itemexport.life.span.hours" | cut -d"=" -f2 | sed -e 's/^[[:space:]]*//'`

cd $PATH_OF_FILES_TO_REMOVE

#TIME WILL BE IN MINUTES BUT IS DEFINED IN HOURS IN DSOACE.CFG
del_time_in_minutes=(60 * $TIME_TO_LIVE_HOURS)

#delete files that match criteria
find $PATH_OF_FILES_TO_REMOVE  -maxdepth 2 -mmin +$del_time_in_minutes -type f -name "*.zip" -exec rm -f {} \;


