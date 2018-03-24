import os

os.system('../Android/gradlew -p ../Android/ assembleRelease')
os.system('python angecrypt.py')
