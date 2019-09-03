# FIXME: this script should be removed
gradle composeDown
# remove all images except mongo:2, adoptopenjdk:11-jre-hotspot
docker image rm $(docker image ls -q | grep -Ev "57c2f7e05108|6909824f92f1")
