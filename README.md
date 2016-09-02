# Mr. Gerkins

Mr. Gerkins is a  bot that automates the mundane tasks involved in managing your repository  and release process so  that the developers/ trusted committers can focus on the things that matter!

Mr. Gerkins can help you with the following `repository management` tasks:
* Generation of release notes
* Notifying pull request owners after tag creation
* Notifying distribution lists after release
* Identify and label pull requests as `InnerSource`

## Integrating with Mr. Gerkins
#### 1. Automated generation of Release Notes
Mr. Gerkins can generate release notes for you. It looks at the various pull requests (PRs) that are a part of the current tag and adds them to the release notes. You can sign up for this by including a pre build step in the job that you use for generating the tag build. There is a slight difference in the integration on Fusion vs. Jenkins

##### A. Jenkins
* Install the [Pre SCM Buildstep](https://wiki.jenkins-ci.org/display/JENKINS/pre-scm-buildstep) plugin in the Jenkins instance that your release job is located on.
* Now open up the configuration of your  maven release job and do the following:
Under *Build Environment* enable `Run buildstep before SCM runs` and add `Execute Shell Option`.
* Add the following script  to the command section
```
#!/bin/bash
curl -d "{ \"branch\": \"$BRANCH\", \"release_version\": \"$MVN_RELEASE_VERSION\", \"repository_full_name\": \"$REPONAME\" }" -H "Content-Type: application/json" -X POST "$HOST_DETAILS/api/v1/release-notes/create"
exit 0
```
where,
`$BRANCH`: Branch from where release tag is cut
`$MVN_RELEASE_VERSION`: Release version of the component
`$REPONAME`: full name of the repository. Ex: Payments-R/paymentapiplatformserv
`$HOST_DETAILS`: IP/Host/URL where the bot is hosted

##### B. Fusion
Fusion does not support addition of plugins out of the box. Therefore we need to create another job that executes the generation and commiting of release notes. This commit has to happen before SCM clone is done. The bot will take care of calling the fusion job to perform the maven release.
* In `application.properties` add `owner-or-org/owner-or-org:fusion_job` kvp to `fusion.repositories` paramter. `fusion_job` is your compoent's release job.
* Create a new fusion job and add the following to *Post Steps* -> Execute Shell:
```
#!/bin/bash
curl -d "{ \"branch\": \"$BRANCH\", \"release_version\": \"$RELEASE_VERSION\", \"repository_full_name\": \"$REPOSITORY\", \"development_version\": \"$DEVELOP_VERSION\", \"is_dry_run\": $IS_DRY_RUN, \"scm_tag\": \"$SCM_TAG\", \"parameter\": \"[{\\\"name\\\": \\\"BRANCH\\\", \\\"value\\\": \\\"$BRANCH\\\"}, {\\\"name\\\": \\\"SKIP_TESTS\\\", \\\"value\\\": $SKIP_TESTS}]\" }" -H "Content-Type: application/json" -X POST "$HOST_DETAILS/api/v1/tag/create"
exit 0
```

#### 2. Notify tag PR owners
Mr. Gerkins  can notify all contributors, whose pull requests were included in the tag. The integration is same for both Jenkins and fusion
* In *Post Steps*, include a `Execute Shell` step. This should be triggered only if the build was successful.
* Add the following script:
```
#!/bin/bash
curl -d "{ \"branch\": \"$BRANCH\", \"release_version\": \"$MVN_RELEASE_VERSION\", \"repository_full_name\": \"Payments-R/paymentsetupserv\" }" -H "Content-Type: application/json" -X POST "$HOST_DETAILS/api/v1/tag/notify-authors"
exit 0
```

#### 3. Notify DL after release
Mr. Gerkins can notify a DL, once a release has been drafted on Github will all the details included.
* In `application.properties`, add the `repository-full-name:dl-email` to `email.release_notify_dl` parameter.
* Under Settings of the repository, open the *Webhooks & services* tab.
* Add a Webhook  to recieve Release event. The Payload URL should be:
```
http://{$HOST-DETAILS}/webhook/release
```
#### 4. Labelling InnerSource PRs
Mr. Gerkins can identify and label pull requests raised by contributors outside the team. This helps if your team is involved in the inner sourcing program. There are two ways of achieving this:
* List the user id of your team members under `github.team-members` parameter in `application.properties`
* By specifying a team dl. Make the following changes in application.properties
```
dl-manager.token={dl manager access token}
dl-manager.executor={Authorized DL manager api caller user id}
dl-manager.service-url=https://dlmanager.mycompany.com
dl-manager.target-dl={your teams dl}
dl-manager.enabled=true
```
(`{}` is part of placeholder string)
Currently this approach doesn't work, since DL Manager API doesn't support calls from C3 instances. Need to work with them to support this.
Now subscribe to the PR webhooks notification.
* Under Settings of the repository, open the *Webhooks & services* tab.
* Add a Webhook  to recieve Pull Request event. The Payload URL should be:
```
http://{$HOST-DETAILS}/webhook/pull-request
```


## License
Mr. Gerkins is licensed under MIT license. The license can be found [here](LICENSE.md).
