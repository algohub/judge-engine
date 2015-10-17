# Algohub Judge Engine

A modern versatile online judge engine, which supports most of programming languages.

## Prerequisites

### 1. JDK 8

### 2. GCC 4.9+

### 3. Python 3

### 4. Ruby 2

These commands should be available in `$PAHT`: `java`, `javac`, `g++`, `python3`, `ruby`

## Compile on Ubuntu

### 1. Install libraries

1. Copy rapidjson headers to `/usr/local/include`

        git clone git@github.com:miloyip/rapidjson.git
        sudo cp -r rapidjson/include/rapidjson/ /usr/local/include/

2. Copy the Python `algohub` module  into one of Python's default module paths, i.e., `sys.path`

        sudo cp -r src/main/resources/python/algohub/  /usr/lib/python3.4

3. Copy the Ruby  `algohub`module into one of Ruby's default module search path(run `ruby -e 'puts $:'` to get all paths)

        cp  src/main/resources/ruby/algohub.rb ~/.rvm/rubies/ruby-2.2.2/lib/ruby/2.2.0

### 2. Compile

    mvn clean package

## Run

    java -jar target/judge-engine-1.0-SNAPSHOT.jar src/test/resources/problems/2-sum/2-sum.json PYTHON src/test/resources/problems/2-sum/solution.py

