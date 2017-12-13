 ssh-keygen -t rsa -P ""
 cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
 chmod 0600 ~/.ssh/authorized_keys
