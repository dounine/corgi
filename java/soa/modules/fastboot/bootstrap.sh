#!/bin/bash
gradle clean build -x test
java -jar build/libs/fastboot-1.0.jar
