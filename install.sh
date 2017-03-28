#!/bin/bash
if [[ ! -d "/usr/local/include/rapidjson" ]]; then
  git clone https://github.com/miloyip/rapidjson.git
  sudo cp -r rapidjson/include/rapidjson/ /usr/local/include/
  rm -rf ./rapidjson
fi

git clone https://github.com/algohub/judge-engine-languages-support.git /tmp/judge-engine-languages-support

if [[ ! -d "/usr/local/lib/python3.5/dist-packages/algohub/" ]]; then
  sudo cp -r /tmp/judge-engine-languages-support/judge-engine-python-support/algohub/ /usr/local/lib/python3.5/dist-packages/
fi

if [[ ! -f "/usr/local/lib/site_ruby/2.3.0/algohub.rb" ]]; then
  sudo mkdir -p /usr/local/lib/site_ruby/2.3.0
  sudo cp /tmp/judge-engine-languages-support/ruby/algohub.rb /usr/local/lib/site_ruby/2.3.0/
fi

command -v mvn >/dev/null 2>&1 || { echo >&2 "Maven is no installed, now start to install..."; sudo apt -y --no-install-recommends install maven; }

mvn clean package

if [ "$?" -ne 0 ]
then
  echo "mvn clean pacage failed!" >&2
  exit 1
else
  mvn install -DskipTests
fi

