# DRAS-TIC Users's Guide

This is a guide to the basic file and folder operations of DRAS-TIC. It covers the web interface. If you want to interact with DRAS-TIC on your local command-line, please refer to our [DRAS-TIC Command-Line Guide](CLI.md). If you need to manage user accounts and group membership, please see the [Administrator's Guide](ADMINISTRATION.md).


## Login

When you first visit a DRAS-TIC website, you will not be logged in. You may still have access to some public materials in the repository, even if you do not have an account.

To login, simply click on the **Login** link on the top-right of the page. You will be prompted for your user account and a password. If the repository is connected to an organizational login system (LDAP server), then you may enter that password. Otherwise, enter your DRAS-TIC repository password.

Once you login to the site, an account menu will replace the login link on the top-right of the page. You may use that menu to logout again.

## Browsing collections

To reach the root folder, where all of the top-level collections are listed, click on the **Archive** link on the top-left menu bar. From there you can navigate within the folder structure of the collections where you have permissions.

Downloads are available for items in a folder listing and from the file view page. If you need to download large collections, we recommend that you use client software, such as your operating system's [WEBDAV client](WEBDAV.md) or the [DRAS-TIC Command-Line software](CLI.md).

## Editing

For any collection or file, you can edit the name, metadata fields, and permissions. These actions are available by clicking the **Edit** button from the item view page. The changes you make on the Edit page are not saved until you click the **Save** button in the lower part of the page.

Permissions in DRAS-TIC are always assigned to groups of people. Simply click the box in the appropriate read or write column, next to the desired group name.

Metadata in DRAS-TIC is composed of key-value pairs. You can place whatever text you like in the key or the value text boxes. Use the **+** button to add more key-value pairs to the page.

## Deleting

Delete is a permanent step in DRAS-TIC. Items and their metadata will be removed completely from the repository. If you delete a folder, all items stored in that folder are also deleted. For this reason DRAS-TIC offers a confirmation prompt before proceeding with any delete operation.

You will find the **Delete** button next to the **Edit** button on any item view page where you have write permissions. You can also delete items in a folder listing, using the **(X)** icon button.
