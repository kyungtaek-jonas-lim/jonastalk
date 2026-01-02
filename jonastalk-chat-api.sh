#!/bin/bash
PROJECT_PATH="./"
PROJECT="jonastalk-chat-api"
LOG_PATH="../logs"
LOG_FILE="${LOG_PATH}/${PROJECT}.log"

mkdir -p "$LOG_PATH" || { echo "Cannot create log dir $LOG_PATH"; exit 1; }

cd "${PROJECT_PATH}${PROJECT}" || exit 1
./mvnw clean install || exit 1

# Background
nohup ./mvnw spring-boot:run >> "$LOG_FILE" 2>&1 &

# Background & No Logs
# nohup ./mvnw spring-boot:run >> /dev/null 2>&1 &

echo "Started $PROJECT (PID=$!)"
