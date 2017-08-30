# Create Workflow with DRAS-TIC Jobs

DRAS-TIC Jobs is a messaging-based workflow engine, written in Python. It uses the [Celery task scheduling system](http://www.celeryproject.org/) to create and track asynchronous jobs that occur in response to changes in repository content. DRAS-TIC jobs may be installed on a separate server from Cassandra and the DRAS-TIC front-end and can scale to meet your needs by adding more "worker servers".

## Git Repository

https://github.com/UMD-DRASTIC/drastic-jobs


## Installing
DRAS-TIC Jobs has its own installation Ansible playbook, under the ["ansible" folder](https://github.com/UMD-DRASTIC/drastic-jobs) in its code repository. This playbook will make use of the drastic group configurations from your main drastic Ansible inventory. So once you have installed DRAS-TIC, you can proceed to install DRAS-TIC Jobs.

Add a drastic-jobs-hosts group to your inventory file:
```
[drastic-jobs-hosts]
drastic-jobs-1
```

Then configure these group variables:
```
drastic_url: https://drastic.example.com
drastic_host: drastic.example.com
worker_drastic_user: react_worker
worker_drastic_password: your_password
```

## Work in Progress

DRAS-TIC Jobs is in development. It's gradually evolving from a local workflow effort into a more general purpose system. Some of the workflow scripts provided relate to local projects and haven't been teased apart just yet. Thanks for your interest and please bear with us or get in touch.


## Writing Workflow scripts

The best way to get started is to look at an example. For that purpose the workflow in jobs/graph.py will do nicely. A workflow is established by registering as a listener for certain repository events. When the event occurs, the workflow script will do nothing immediately, but instead schedules a job in Celery. Multiple jobs may be defined in a single workflow script, representing different serial or parallel steps in a complex workflow. For an example of a more complex workflow, see the jobs/browndog.py example.


## Tracking Jobs

You may can use standard Celery tools to track progress on jobs. The DRAS-TIC Jobs playbook will install a Celery Flower web dashboard to help you track jobs. (In most network situations you will need an SSH tunnel to reach it.)

There is also some rudimentary progress reporting that we've tried to put into repository metadata, mostly for bulk ingests. This works okay, but still has some kinks to work out. Examples of this maybe found in the jobs/httpdir.py workflow script.


## Traversing the Repository (Gradually)

The last thing to mention is the availability of ad hoc workflow through a repository or collection traversal. This means that a particular job will be scheduled for every item under a given repository path. We use this to re-run workflows after making changes to them, such as re-indexing content in a search engine.

There is a script to help you begin a traverse, called begin_traverse.py. You can call that script on the command-line on the DRAS-TIC Jobs host, giving it the path to traverse and the specific job to run.

Traverse at this scale is a special thing. We had to rate-limit traverse jobs in order to prevent them from overwhelming Celery (or RabbitMQ) with too many scheduled jobs.
