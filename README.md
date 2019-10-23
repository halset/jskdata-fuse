# jskdata-fuse

A [FUSE](https://github.com/libfuse/libfuse) (Filesystem in Userspace) for [jskdata](https://github.com/halset/jskdata) implemented with [jnr-fuse](https://github.com/SerCeMan/jnr-fuse).

## Warning

This is a very stupid idea, so please do not use this for anything.

## Example

```
jskdata-fuse % df -h /private/tmp/mnt-jskdata/
Filesystem      Size   Used  Avail Capacity iused ifree %iused  Mounted on
java@osxfuse0    0Bi    0Bi    0Bi   100%       0     0  100%   /private/tmp/mnt-jskdata
jskdata-fuse % ls -l /private/tmp/mnt-jskdata/
total 0
dr-xr-xr-x  0 halset  staff  0 Jan  1  1970 28c896d0-8a0d-4209-bf31-4931033b1082
dr-xr-xr-x  0 halset  staff  0 Jan  1  1970 aee42bb6-d0e9-4d70-86fe-6ea76c381055
jskdata-fuse % ls -l /private/tmp/mnt-jskdata/28c896d0-8a0d-4209-bf31-4931033b1082/
total 108424
-r--r--r--  0 halset  staff  41517273 Jan  1  1970 Samferdsel_0000_Norge_6173_Luftfartshindre_GML.zip
-r--r--r--  0 halset  staff  13990819 Jan  1  1970 Samferdsel_0000_Norge_6173_Luftfartshindre_SOSI.zip
jskdata-fuse % ls -l /private/tmp/mnt-jskdata/aee42bb6-d0e9-4d70-86fe-6ea76c381055/
total 218536
-r--r--r--  0 halset  staff  22582912 Jan  1  1970 Basisdata_0000_Norge_25833_N1000Kartdata_FGDB.zip
-r--r--r--  0 halset  staff  36951747 Jan  1  1970 Basisdata_0000_Norge_25833_N1000Kartdata_GML.zip
-r--r--r--  0 halset  staff  41534106 Jan  1  1970 Basisdata_0000_Norge_25833_N1000Kartdata_PostGIS.zip
-r--r--r--  0 halset  staff  10812976 Jan  1  1970 Basisdata_0000_Norge_25833_N1000Kartdata_SOSI.zip
```