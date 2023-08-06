#!/bin/sh

if [ -z "${LOCAL_USER_ID}" ]; then 
    USER_ID=9001
else 
    USER_ID=${LOCAL_USER_ID}
fi

echo "Starting with UID : $USER_ID"
adduser -u $USER_ID -D user
export HOME=/home/user
chown user:user $HOME

exec /sbin/su-exec user "$@"
