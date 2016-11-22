#!/bin/bash
git stash
git pull
git stash pop
gradle install -xtest
