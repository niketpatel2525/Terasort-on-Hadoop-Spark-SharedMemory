# this will mount your storage with instance
 Sudo su
 sudo apt-get install mdadm
 mdadm --create /dev/md0 --run --level=0 --name=RAID --force --raid-device=1 /dev/nvme0n1
 mkfs.ext4 -L RAID /dev/md0
 mkdir /data
 mount LABEL=RAID /data
 chmod 777 /data
