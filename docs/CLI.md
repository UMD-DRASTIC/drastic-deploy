# Drastic Command-Line Client

DRAS-TIC includes a command-line client tool that you can use to interact with any DRAS-TIC digital repository. While it can be used to interact with other CDMI-compliant repositories, but this is not the intended purpose.

**NOTE**: The commands in this document start with a `$`. That is the prompt on most Linux or Debian systems. Your own prompt may be different. The command you need to type is to the right of the prompt.


## Inline Help

If you type `drastic` alone without parameters, it will print a complete list of available commands in the terminal. You may find it most useful to consult the documentation that way.


## Login and creating a session
The client tool only talks to DRAS-TIC servers as needed over HTTP(S). However it will save your server and login information during a session.

* Connect without login:
```
$ drastic init --url=http://drastic.example.com
```

* Connect and authenticate:
```
$ drastic init --url=http://drastic.example.com --username=USER --password=PASS
```

You will be prompted for a password if you don't put that option on the command line as follows:
```
$ drastic init --url=http://drastic.example.com --username=USER
```

* Close the session to prevent unauthorized access:
```
$ drastic exit
```


## Browsing collections
* Show current working container:
```
$ drastic pwd
```

* Show current authenticated user:
```
$ drastic whoami
```

* List a container:
```
$ drastic ls <path>
```

* List a container wit ACL information:
```
$ drastic ls -a <path>
```

* Move to a new container:
```
$ drastic cd <path>
...
$ drastic cd ..  # back up to parent
```

* Fetch a data object from the archive to a local file:
```
$ drastic get <src>
```
or specify the local filename:
```
$ drastic get <src> <dst>
```
or, to overwrite an existing file:
```
$ drastic get --force <src>  # Overwrite an existing local file
```


## Modifying collections
* Create a new container:
```
$ drastic mkdir <path>
```

* Put a local file, optionally with a new name:
```
$ drastic put <src>
...
$ drastic put <src> <dst>
```

* Create a reference object, optionally include the MIME type of the object (if not supplied ``drastic put`` will attempt
to guess):
```
$ drastic put --ref <url> <dest>
...
$ drastic put --mimetype="text/plain" <src>
```

* Remove an object or a container:
```
$ drastic rm <src>
```

* Add or modify an access control list (ACL) to an object or a container:
```
$ drastic chmod <path> (read|write|null) <group>
```


## Using Metadata

* Get the CDMI json dict for an object or a container:
```
$ drastic cdmi <path>
```

* Set (overwrite) a metadata value for a field:
```
$ drastic meta set <path> "org.dublincore.creator" "S M Body"
$ drastic meta set . "org.dublincore.title" "My Collection"
```

* Add another value to an existing metadata field:
```
$ drastic meta add <path> "org.dublincore.creator" "A N Other"
```

* List metadata values for all fields:
```
$ drastic meta ls <path>
```

* List metadata value(s) for a specific field:
```
$ drastic meta ls <path> org.dublincore.creator
```

* Delete a metadata field:
```
$ drastic meta rm <path> "org.dublincore.creator"
```

* Delete a specific metadata field with a value:
```
$ drastic meta rm <path> "org.dublincore.creator" "A N Other"
```


## Administrative tasks

**NOTE:** All permissions in DRAS-TIC are assigned to groups, not individuals.

* List existing users:
```
$ drastic admin lu
```

* List information about a user:
```
$ drastic admin lu <name>
```

* List existing groups:
```
$ drastic admin lg
```

* List information about a group:
```
$ drastic admin lg <name>
```

* Create a user:
```
$ drastic admin mkuser [<name>]
```

* Modify a user:
```
$ drastic admin moduser <name> (email | administrator | active | password) [<value>]
```

* Remove a user:
```
$ drastic admin rmuser [<name>]
```

* Create a group:
```
$ drastic admin mkgroup [<name>]
```

* Remove a group:
```
$ drastic admin rmgroup [<name>]
```

* Add user(s) to a group:
```
$ drastic admin atg <name> <user> ...
```

* Remove user(s) from a group:
```
$ drastic admin rtg <name> <user> ...
```


## Installation

These steps work well on a Mac or on Linux. Windows users who are not Python users will have better luck with the Windows Installer, available on the [releases page](https://github.com/UMD-DRASTIC/drastic-cli/releases).


### Create And Activate A Virtual Environment

This step is optional. You can also install the DRAS-TIC command-line tool without a virtual environment.

    $ virtualenv ~/ve/drastic/cli<version>
    ...
    $ source ~/ve/drastic/cli/bin/activate


### Install DRAS-TIC Command

1. Download the DRAS-TIC Command-Line project *source code*
as a [release archive](https://github.com/UMD-DRASTIC/drastic-cli/releases) (ZIP) or clone the project with git:

    $ git clone https://github.com/UMD-DRASTIC/drastic-cli.git

1. if you downloaded the ZIP file, unzip the project source code:

    $ unzip 1.0.0.zip

1. Change to the project folder:

    $ cd drastic-cli

1. Install Dependencies:

    $ pip install -r requirements.txt

1. Install Drastic Client:
    $ pip install -e .

1. Try a Drastic command:

    $ drastic init --url=http://ciber.umd.edu --user=joe
