#!/bin/bash
cd lakemart-server
mvn clean package -DskipTests
docker build -t lakemart-server:latest .