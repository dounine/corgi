#!/bin/bash
gradle clean build -x test
java -jar build/libs/sso-consumer-1.0.jar
