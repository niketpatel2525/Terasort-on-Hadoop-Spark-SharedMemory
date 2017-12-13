# this script will generate 1 TB data
 cd /data
 wget http://www.ordinal.com/try.cgi/gensort-linux-1.5.tar.gz
 tar xvzf gensort-linux-1.5.tar.gz
 cd 64/
 ./gensort -a -t14 10995116277 sample1tb.txt
