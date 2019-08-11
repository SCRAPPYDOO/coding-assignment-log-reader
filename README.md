# coding-assignment-log-reader

### Path Param to Log File ###
I know that this should be in arg but i decided to put it in application properties

path to log file
will use default data set if not set

Windows example:

log:
  data:
    path: "E:/sample-data"

### Path Param to Log File ###

time difference between event start and finish timestamps to mark as alert

log:
  alert:
    difference_in_ms: 4


### Summary ###

I never used Spring batch before so took little longer then expected, thats why there is no tests. Lack of time.
Now i should add unit and integration tests, and will do in some free time.
For sure there is some code or logic enchantment, will appear when i will writing tests.