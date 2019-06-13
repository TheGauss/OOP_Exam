Examproject

To get started you need a JAVA IDE with SprinBoot installed.

This program answers to GET /metadata, GET /data and GET/stats?field=field (if the field doesn't exist will give a 404) on the 8080 door.

On the first startup, it'll download the data set from https://www.dati.gov.it/api/3/action/package_show?id=a1dee418-ddd7-40c6-ad6c-7b35aa31f61a and will save it in the file data.csv in root folder.

JAVAdoc: doc\index-files\index-1.html
UML: doc\UML