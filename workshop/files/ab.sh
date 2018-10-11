#!/bin/bash

cat urls.txt | parallel "ab -n 300 -c 10 {}"
