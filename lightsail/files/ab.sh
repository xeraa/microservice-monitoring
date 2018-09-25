#!/bin/bash

cat urls.txt | parallel "ab -n 1000 -c 30 {}"
