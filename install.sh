#!/bin/bash
if [[ ! -d "/usr/local/include/rapidjson" ]]; then
  git clone https://github.com/miloyip/rapidjson.git
  sudo cp -r rapidjson/include/rapidjson/ /usr/local/include/
  rm -rf ./rapidjson
fi

sudo cp src/main/resources/cpp/* /usr/local/include/
sudo cp -r src/main/resources/python/algohub/  /usr/local/lib/python3.5/dist-packages/
sudo mkdir -p /usr/local/lib/site_ruby/2.3.0
sudo cp src/main/resources/ruby/algohub.rb /usr/local/lib/site_ruby/2.3.0/

mvn clean package

if [ "$?" -ne 0 ]
then
  echo "mvn clean pacage failed!" >&2
  exit 1
else
  mvn install -DskipTests
fi

