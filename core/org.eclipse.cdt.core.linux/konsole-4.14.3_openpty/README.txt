 

wget http://mirrors.kernel.org/fedora/releases/22/Everything/source/SRPMS/k/konsole-4.14.3-4.fc22.src.rpm

rpm2cpio konsole-4.14.3-4.fc22.src.rpm  | cpio -i 


yum -v -y install desktop-file-utils kdelibs4-devel kde-baseapps-devel
yum -v -y install pkgconfig


Fedora 22
yum -v -y install desktop-file-utils kdelibs-devel kde-baseapps-devel



./konsole_openpty_build/src/konsole
./konsole_openpty_build/lib/libkonsoleprivate.so
./konsole_openpty_build/lib/libkdeinit4_konsole.so

/lib/libkonq.so.5
/lib64/libkonq.so.5




OPENPTY=  ./output/konsole






Konsole - Development Information
https://www.kde.org/applications/system/konsole/development
Clone Konsole source code:
git clone git://anongit.kde.org/konsole






