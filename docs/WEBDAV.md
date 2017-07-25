# Accessing Collections with WEBDAV

WEBDAV is a common protocol for remote file access, across all major operating systems. Most operating systems integrate WEBDAV into their file browsers, such that you can access WEBDAV folders much like a normal folder. WEBDAV provides an easy and familiar path for bulk import and export of collections. WEBDAV does not support metadata transfer. If you want to export collections with their metadata, we recommend using [CDMI](CDMI.md).

DRAS-TIC provides a WEBDAV service that enforces normal permissions on objects. However, it always requires a user login, unlike the web interface.


## Connection Information
We recommend using SSL to secure DRAS-TIC connections, including WEBDAV.

* Insecure URL: `dav://<drastic hostname>:80/api/webdav/`
* Secure URL: `davs://<drastic hostname>:443/api/webdav/`

Some operating systems may prefer `http:` and `https:` URLs instead of `dav:` and `davs:`.
