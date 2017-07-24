# Authentication

The DRAS-TIC authentication system stores user data in the Cassandra cluster. New user logins may be created through the web interface, through the admin tool, or through the remote command-line client. A password is required, but only a hash of this password is saved in the database.

Administrators can also configure LDAP authentication in addition to the local Cassandra-stored password hashes. This means that a user may authenticate with either their LDAP password or their stored DRAS-TIC password. This is accomplished by adding a few settings for the Django application, but we recommend that you configure these settings via the Ansible playbook.

## Configure LDAP Authentication

The easy way to configure LDAP is to modify your Ansible variables to include the LDAP settings. Ansible will place the information in the required places on the server for you.

Modify these lines and add them to your Ansible group variables file (drastic.yaml usually):
```
LDAP_SERVER_URI: ldap://ldap.example.com
LDAP_USER_DN_TEMPLATE: uid=%(user)s,ou=people,dc=example,dc=com
LDAP_BIND_DN: uid=mybinddn,cn=auth,ou=ldap,dc=example,dc=com
LDAP_BIND_PASSWORD: lkjeojfkjafjk
```

Re-run the Ansible play for the front-end servers:
```
$ ansible-playbook webservers.yml -i ~/ansible/hosts --ask-become-pass
```

## Details
Authentication is handled in users/views.py. This code takes the LDAP server and DN pattern from drastic_ui/settings.py. In order to enable LDAP authentication via a simple bind, you must supply environment variables to the drastic web process:

* AUTH_LDAP_SERVER_URI - an LDAP server (ldap://ldap.example.com)
* AUTH_LDAP_USER_DN_TEMPLATE - a string formatting template for the DN to be used for BIND (uid=%(user)s,ou=users,dc=example,dc=com) - the user variable will be replaced.

If provisioning with Ansible, these LDAP settings will be included in the drastic-web service script and passed to the python process.
