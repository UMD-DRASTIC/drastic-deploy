# Cloud Data Management Interface (CDMI)

CDMI is an industry standard way of interacting with file and folders stored in 'the Cloud'. While the cloud generally refers to the use of *someone else's data center*, in the case CDMI it can also be your own data center. CDMI is a non-proprietary protocol that has some similarities with Amazon's S3 storage protocol. Unlike [WEBDAV](WEBDAV.md), CDMI supports access to and modification of key-value metadata for folders and files. DRAS-TIC was originally designed to support CDMI as the primary web standard for data access and modification.

* [CDMI Specification](https://www.snia.org/cdmi)
* [CDMI for S3 Programmers](https://www.snia.org/sites/default/files/S3-like_CDMI_v1.0.pdf)

CDMI is a standard promulgated by the [Storage Networking Industry Association (SNIA)](https://www.snia.org).


## Using CDMI

If you want to write programs that interact with DRAS-TIC, CDMI libraries are  a convenient place to start. There is also the [DRAS-TIC Python library](PYTHONLIBRARY.md) which implements CDMI functions in Python. The CDMI endpoint is located here:

* Insecure URL: `http://<drastic host>/api/cdmi`
* Secure URL: `https://<drastic host>/api/cdmi`
