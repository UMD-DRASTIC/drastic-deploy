# DRAS-TIC Python Library

DRAS-TIC includes a Python library or module that provides convenient client API for most DRAS-TIC operations. The module may be built and install locally from cloned sources as part of the [DRAS-TIC Command Line Interface (CLI) project](https://github.com/UMD-DRASTIC/drastic-cli).

The client interface is found in the `cli.client.DrasticClient` class, which comes from [this source file](https://github.com/UMD-DRASTIC/drastic-cli/blob/master/cli/client.py).


# Installing

Installing the client is a straightforward two step process. First get the code:

`$ git clone https://github.com/UMD-DRASTIC/drastic-cli.git`

Then install the Python module where you want it, using the normal setuptools workflow:

```
$ cd drastic-cli
$ pip install .
```

# Example Code

Here is a quick Python example that includes authentication, folder creation, and an upload with metadata:

```
from cli.client import DrasticClient
from contextlib import closing

myclient = DrasticClient('https://example.com')
result = myclient.authenticate('my_user', 'my_password')
if not res.ok():
    throw Error('Authentication failed: {0}'.format(res.msg()))
result2 = myclient.mkdir('/foo')
if not result2.ok():
    throw new Error('Cannot make directory: {0}'.format('/foo'))
with closing(open('Smithers_Dossier.zip', 'rb')) as f:
    result3 = myclient.put('/foo/Smithers_Dossier.zip',
                           f,
                           metadata={'creator': 'Lisa Simpson'},
                           mimetype='application/zip')
    if not result3.ok():
        throw new Error('Cannot upload: {0}'.format(result3.msg()))
```
