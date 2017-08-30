# Accessing Collections with WEBDAV

WEBDAV is a common protocol for remote file access, across all major operating systems. Most operating systems integrate WEBDAV into their file browsers, such that you can access WEBDAV folders much like a normal folder. WEBDAV provides an easy and familiar path for bulk import and export of collections. WEBDAV does not support metadata transfer. If you want to export collections with their metadata, we recommend using [CDMI](CDMI.md).

DRAS-TIC provides a WEBDAV service that enforces normal permissions on objects. However, it always requires a user login, unlike the web interface.


## Connection Information
We recommend using SSL to secure DRAS-TIC connections, including WEBDAV.

* Insecure URL: `dav://<drastic hostname>:80/api/webdav/`
* Secure URL: `davs://<drastic hostname>:443/api/webdav/`

Some operating systems may prefer `http:` and `https:` URLs instead of `dav:` and `davs:`. Please see the instructions for your operating system:

* [Windows instructions (third-party article)](https://www.webdavsystem.com/server/access/windows/map_drive)
* [Mac OSX: Connect to a WebDAV server](https://support.apple.com/kb/ph18514?locale=en_US)
* [Ubuntu and Linux clients](http://ubuntuguide.org/wiki/WebDAV#WebDAV_Clients)


## Troubleshooting

Some people may have trouble if their OS is older or certain conditions apply. WebDAV is a very useful way to interact with a repository. We encourage users to sort through these issues and get it working:

* [Windows Troubleshooting Steps](https://support.microsoft.com/en-us/help/912152/you-cannot-access-a-webdav-web-folder-from-a-windows-based-client-comp)
