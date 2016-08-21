#!/bin/bash
gradle clean build -x test &&
cd build/distributions
tar -zxvf sso-provider-1.0.tar
cd ../..
build/distributions/sso-provider-1.0/bin/sso-provider
