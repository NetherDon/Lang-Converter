@echo off
IF "%1" EQU "-r" (
    java %2.%2
) ELSE IF "%1" NEQ "" (
    java Coco.Coco %* -frames Coco
) ELSE (
    ECHO "Unknown command"
)