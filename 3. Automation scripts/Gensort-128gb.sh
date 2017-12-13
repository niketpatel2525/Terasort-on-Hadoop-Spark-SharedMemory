# this script will generate 128 GB data
 cd /data
 wget http://www.ordinal.com/try.cgi/gensort-linux-1.5.tar.gz
 tar xvzf gensort-linux-1.5.tar.gz
 cd 64/
 ./gensort -a -t8 1374389534 sample128gb.txt
