#!/bin/bash

######
# UI #
######
#NOTE: Will need to refresh UI once core is loaded
open baltimore-crime-ui/webpage/index.html

########
# Core #
########
cd baltimore-crime-core/
gradle bootRun
cd ..

